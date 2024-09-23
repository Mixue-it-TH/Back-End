package com.example.kanbanbackend.Service;

import com.example.kanbanbackend.Auth.JwtTokenUtil;
import com.example.kanbanbackend.Config.LimitConfig;
import com.example.kanbanbackend.Config.Permission;
import com.example.kanbanbackend.Config.VisibilityConfig;
import com.example.kanbanbackend.DTO.LimitFunc.LimitConfigDTO;
import com.example.kanbanbackend.DTO.LimitFunc.LimitDetailsDTO;
import com.example.kanbanbackend.DTO.LimitFunc.StatusTasksNumDTO;
import com.example.kanbanbackend.DTO.StatusDTO.StatusDTO;
import com.example.kanbanbackend.DTO.StatusDTO.StatusSelectedDTO;
import com.example.kanbanbackend.Entitites.Primary.Board;
import com.example.kanbanbackend.Entitites.Primary.Config;
import com.example.kanbanbackend.Entitites.Primary.Status;
import com.example.kanbanbackend.Entitites.Primary.Task;
import com.example.kanbanbackend.Exception.*;
import com.example.kanbanbackend.Repository.Primary.BoardRepository;
import com.example.kanbanbackend.Repository.Primary.ConfigRepository;
import com.example.kanbanbackend.Repository.Primary.StatusRepository;
import com.example.kanbanbackend.Repository.Primary.TaskRepository;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class StatusService {
    @Autowired
    ModelMapper mapper;
    @Autowired
    ListMapper listMapper;
    @Autowired
    private StatusRepository repository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private ConfigRepository configRepository;

    @Autowired
    private Permission permission;
    @Autowired
    private VisibilityConfig visibilityConfig;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;


    public List<StatusDTO> getAllStatus() {
        List<Status> statuses = repository.findAll();
        return listMapper.mapList(statuses, StatusDTO.class);
    }

    public List<StatusDTO> getAllStatusByBoardId(String boardId, HttpServletRequest request) {
        Claims claims = jwtTokenUtil.decodedToken(request);
        if(visibilityConfig.visibilityType(boardId) || permission.getRoleOfBoard(boardId, claims.get("oid").toString())){
            List<Status> status = repository.findStatusByBoard_Id(boardId);
            return listMapper.mapList(status, StatusDTO.class);
        }else{
            throw new ForBiddenException("You do not have permission to access this resource");
        }

    }

    public StatusSelectedDTO getStatusByIdAndBoardId(String boardId, int id, HttpServletRequest request) {
        Status status = repository.findStatusByBoard_IdAndId(boardId, id);
        if (status == null)
            throw new ItemNotFoundException("Status id " + id + " or Board id" + boardId + " does not exist");
        Claims claims = jwtTokenUtil.decodedToken(request);
        if(visibilityConfig.visibilityType(boardId)  || permission.getRoleOfBoard(boardId, claims.get("oid").toString())){
            // Assuming `getCreatedOn` and `getUpdatedOn` return `Timestamp`
            Timestamp createdOnTimestamp = status.getCreatedOn();
            Timestamp updatedOnTimestamp = status.getUpdatedOn();

            // Convert Timestamp to LocalDateTime
            LocalDateTime createdDateTime = createdOnTimestamp.toLocalDateTime();
            LocalDateTime updatedDateTime = updatedOnTimestamp.toLocalDateTime();

            // Convert LocalDateTime to ZonedDateTime in UTC
            ZonedDateTime createdUTC = createdDateTime.atZone(ZoneOffset.UTC);
            ZonedDateTime updatedUTC = updatedDateTime.atZone(ZoneOffset.UTC);

            // Convert ZonedDateTime to ISO 8601 format string
            String createdOnISO = createdUTC.format(DateTimeFormatter.ISO_INSTANT);
            String updatedOnISO = updatedUTC.format(DateTimeFormatter.ISO_INSTANT);


            StatusSelectedDTO dto = mapper.map(status, StatusSelectedDTO.class);

            dto.setCreatedOn(createdOnISO);
            dto.setUpdatedOn(updatedOnISO);

            return dto;
        }else{
            throw new ForBiddenException("You do not have permission to access this resource");
        }

    }

    @Transactional
    public StatusDTO createStatus(String boardId, StatusDTO newStatusDTO, HttpServletRequest request) {
        Status duplicate = repository.findStatusByStatusNameAndBoard_Id(newStatusDTO.getStatusName(), boardId);
        if (duplicate != null) throw new BadRequestWithFieldException("name", "must be unique");

        Board board = boardRepository.findBoardById(boardId);
        if (board == null) throw new ItemNotFoundException("Board id " + boardId + "doesn't exist!!");

        Claims claims = jwtTokenUtil.decodedToken(request);
        if(permission.getRoleOfBoard(boardId, claims.get("oid").toString())){
            Status status = mapper.map(newStatusDTO, Status.class);

            if (status.getStatusColor() == null || status.getStatusColor().isBlank()) {
                status.setStatusColor("#6b7280");
            }
            Optional.ofNullable(status.getStatusName())
                    .map(String::trim)
                    .ifPresent(status::setStatusName);
            Optional.ofNullable(status.getStatusDescription())
                    .map(String::trim)
                    .ifPresent(status::setStatusDescription);
            status.setBoard(board);
            repository.saveAndFlush(status);
            return mapper.map(status, StatusDTO.class);
        }else{
            throw new ForBiddenException("You do not have permission to access this resource");
        }
    }

    @Transactional
    public StatusDTO updateStatus(String boardId, Integer statusId, StatusDTO editedStatus,  HttpServletRequest request) {
//        if (!permission.canEditOrDelete(statusId)) {
//            throw new BadRequestException("No Status cannot be modified. and Done cannot be modified. respectively.");
//        }
        Claims claims = jwtTokenUtil.decodedToken(request);
        if(permission.getRoleOfBoard(boardId, claims.get("oid").toString())) {
            Status isDuplicate = repository.findStatusByStatusNameAndBoard_Id(boardId, editedStatus.getStatusName());

            Status oldStatus = repository.findStatusByBoard_IdAndId(boardId, statusId);
            if (oldStatus == null)
                throw new ItemNotFoundException("Status id " + statusId + " or Board id " + boardId + " does not exist");

            if (isDuplicate != null && (!oldStatus.getStatusName().equalsIgnoreCase(editedStatus.getStatusName()))) {
                throw new BadRequestWithFieldException("name", "must be unique");
            }
            oldStatus.setStatusName(editedStatus.getStatusName() != null ? editedStatus.getStatusName().trim() : oldStatus.getStatusName());
            oldStatus.setStatusDescription(editedStatus.getStatusDescription() == null ? null : editedStatus.getStatusDescription().trim());
            oldStatus.setStatusColor(editedStatus.getStatusColor() != null ? editedStatus.getStatusColor() : oldStatus.getStatusColor());
            repository.save(oldStatus);
            return mapper.map(oldStatus, StatusDTO.class);
        }else{
            throw new ForBiddenException("You do not have permission to access this resource");
        }

    }

    @Transactional
    public void deleteStatus(String boardId, Integer delId, HttpServletRequest request) {
        Status statusDel = repository.findStatusByBoard_IdAndId(boardId,delId);
        if(statusDel == null) throw new ItemNotFoundDelUpdate("Status id " + delId + " or Board id " + boardId + " does not exist");

        Claims claims = jwtTokenUtil.decodedToken(request);
        if(permission.getRoleOfBoard(boardId, claims.get("oid").toString())) {
            List<Task> taskStillUse = taskRepository.findByTaskStatus(statusDel);
            if (!taskStillUse.isEmpty()) {
                throw new BadRequestException("destination cannot be " + statusDel.getStatusName() + " for task transfer not specified.");
            }

            Config config = getLimitConfig(boardId,request);


            if (config.getLimitMaximumTask() == 1) {
                List<Task> listTasks = taskRepository.findByTaskStatus(statusDel);
                System.out.println(listTasks);
                if (listTasks.size() >= config.getNoOfTasks()) {
                    throw new BadRequestException("You can't delete" + statusDel.getStatusName() + "have on the limit");
                }
            }
            repository.delete(statusDel);
        }else{
            throw new ForBiddenException("You do not have permission to access this resource");
        }

    }

    @Transactional
    public void deleteStatusAndTransfer(String boardId,Integer delId, Integer tranferId,HttpServletRequest request) {
        if (delId.equals(tranferId)) {
            throw new BadRequestException("destination status for task transfer must be different from current status");
        }
        Status statusDel = repository.findStatusByBoard_IdAndId(boardId,delId);
        if(statusDel == null) throw new ItemNotFoundDelUpdate("Board id or Status id was NOT FOUND ");

        Status statusTranfer = repository.findStatusByBoard_IdAndId(boardId,tranferId);
        if(statusTranfer == null) throw new ItemNotFoundDelUpdate("the specified status for task transfer does not exist. ");

        Claims claims = jwtTokenUtil.decodedToken(request);
        if(permission.getRoleOfBoard(boardId, claims.get("oid").toString())) {
            Config config = getLimitConfig(boardId,request);
            if(config.getLimitMaximumTask() == 1) {
                List<Task> listTasks = taskRepository.findByTaskStatus(statusDel);
                List<Task> listTasksTransfer = taskRepository.findByTaskStatus(statusTranfer);
                if ((listTasks.size() + listTasksTransfer.size()) > config.getNoOfTasks()) {
                    throw new BadRequestException("You can't delete" + statusDel.getStatusName() + " have on the limit");
                }
            }
            if (statusDel != null && statusTranfer != null) {
                List<Task> taskList = taskRepository.findByTaskStatus(statusDel);
                taskList.forEach((task) -> {
                    task.setTaskStatus(statusTranfer);
                    taskRepository.save(task);
                });
                repository.delete(statusDel);
            }
        }else{
            throw new ForBiddenException("You do not have permission to access this resource");
        }
    }

    public Config getLimitConfig(String boardId, HttpServletRequest request) {
        Claims claims = jwtTokenUtil.decodedToken(request);
        if(visibilityConfig.visibilityType(boardId) || permission.getRoleOfBoard(boardId, claims.get("oid").toString())){
            Board board = boardRepository.findBoardById(boardId);
            if(board == null) throw new ItemNotFoundException("Board id "+ boardId +" doesn't exist!");

            return board.getConfigId();
        }else{
            throw new ForBiddenException("You do not have permission to access this resource");
        }

    }

    public LimitDetailsDTO checkExceedLimit(String boardId,Config config,HttpServletRequest request) {
        System.out.println(config);
        System.out.println(boardId);
        Claims claims = jwtTokenUtil.decodedToken(request);
        if(permission.getRoleOfBoard(boardId, claims.get("oid").toString())) {
            Config oldConfig = getLimitConfig(boardId,request);
            oldConfig.setLimitMaximumTask(config.getLimitMaximumTask());
            oldConfig.setNoOfTasks(config.getNoOfTasks());

            configRepository.saveAndFlush(oldConfig);

            List<Status> statusList = repository.findStatusByBoard_Id(boardId);

            List<Integer> numOfTasks = new ArrayList<>();
            LimitDetailsDTO statusTaskLimitDTO = new LimitDetailsDTO();
            statusList.removeIf(status -> {
                List<Task> tasks = taskRepository.findByTaskStatus(status);
                if (tasks.size() >= config.getNoOfTasks()) {
                    numOfTasks.add(tasks.size());
                }
                return tasks.size() < config.getNoOfTasks() ;
            });
            List<StatusTasksNumDTO> statusTasksNumDTO = listMapper.mapList(statusList, StatusTasksNumDTO.class);
            for (int i = 0; i < statusTasksNumDTO.size(); i++) {
                statusTasksNumDTO.get(i).setNumOfTasks(numOfTasks.get(i));
            }
            statusTaskLimitDTO.setStatusList(statusTasksNumDTO);
            statusTaskLimitDTO.setNoOfTasks(config.getNoOfTasks());
            statusTaskLimitDTO.setLimitMaximumTask(config.getLimitMaximumTask());
            return statusTaskLimitDTO;
        }else{
            throw new ForBiddenException("You do not have permission to access this resource");
        }
        }


}
