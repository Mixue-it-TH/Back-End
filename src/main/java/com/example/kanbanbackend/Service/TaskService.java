package com.example.kanbanbackend.Service;


import com.example.kanbanbackend.Auth.JwtTokenUtil;
import com.example.kanbanbackend.Config.LimitConfig;
import com.example.kanbanbackend.Config.Permission;
import com.example.kanbanbackend.Config.VisibilityConfig;
import com.example.kanbanbackend.DTO.StatusDTO.StatusListAllTaskDTO;
import com.example.kanbanbackend.DTO.StatusDTO.StatusSelectedDTO;
import com.example.kanbanbackend.DTO.TaskDTO.TaskAddEditDTO;
import com.example.kanbanbackend.DTO.TaskDTO.TaskDTO;
import com.example.kanbanbackend.DTO.TaskDTO.TaskSelectedDTO;
import com.example.kanbanbackend.DTO.TaskDTO.TaskV3.TaskBoardDTO;
import com.example.kanbanbackend.DTO.TaskDTO.TaskV3.TaskBoardtestDTO;
import com.example.kanbanbackend.Entitites.Primary.Board;
import com.example.kanbanbackend.Entitites.Primary.Config;
import com.example.kanbanbackend.Entitites.Primary.Status;
import com.example.kanbanbackend.Entitites.Primary.Task;
import com.example.kanbanbackend.Entitites.Share.User;
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
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class TaskService {
    @Autowired
    ModelMapper mapper;
    @Autowired
    ListMapper listMapper;
    @Autowired
    private TaskRepository repository;

    @Autowired
    private ConfigRepository configRepository;

    @Autowired
    private StatusRepository statusRepository;

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private StatusService statusService;

    @Autowired
    private LimitService limitService;

    @Autowired
    private Permission permission;
    @Autowired
    private VisibilityConfig visibilityConfig;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;


    public List<TaskDTO> getAllTodo(List<String> filterStatuses, String sortBy, String sortDirection) {
        List<Task> taskList = new ArrayList<>();
        Sort.Direction direction = sortDirection.equalsIgnoreCase("ASC") ? Sort.Direction.ASC : Sort.Direction.DESC;

        if ("statusName".equals(sortBy)) {
            sortBy = "taskStatus.statusName";
        }
        Sort sort = Sort.by(direction, sortBy);
        if (filterStatuses != null) {
            taskList.addAll(repository.findAllByStatusNamesSorted(filterStatuses, sort));
            return listMapper.mapList(taskList, TaskDTO.class);
        }
        List<Task> tasks = repository.findAll(sort);
        return listMapper.mapList(tasks, TaskDTO.class);
    }

    public TaskSelectedDTO getTaskById(String boardId, int id, HttpServletRequest request) throws ItemNotFoundException {
//        Task task =  repository.findById(id).orElseThrow(() -> new ItemNotFoundException("Task "+ id +" does not exist !!!" ));
        Task task = repository.findTaskByBoard_IdAndId(boardId, id);
        if (task == null) throw new ItemNotFoundException("Task id: " + id + " or boardId: "+ boardId +" doesn't exist !!!");
        Claims claims = jwtTokenUtil.decodedToken(request);
        if(visibilityConfig.visibilityType(boardId)  || permission.getRoleOfBoard(boardId, claims.get("oid").toString())){
            // Assuming `getCreatedOn` and `getUpdatedOn` return `Timestamp`
            Timestamp createdOnTimestamp = task.getCreatedOn();
            Timestamp updatedOnTimestamp = task.getUpdatedOn();


            // Convert Timestamp to LocalDateTime
            LocalDateTime createdDateTime = createdOnTimestamp.toLocalDateTime();
            LocalDateTime updatedDateTime = updatedOnTimestamp.toLocalDateTime();

            // Convert LocalDateTime to ZonedDateTime in UTC
            ZonedDateTime createdUTC = createdDateTime.atZone(ZoneOffset.UTC);
            ZonedDateTime updatedUTC = updatedDateTime.atZone(ZoneOffset.UTC);
            System.out.println(createdUTC);

            // Convert ZonedDateTime to ISO 8601 format string
            String createdOnISO = createdUTC.format(DateTimeFormatter.ISO_INSTANT);
            String updatedOnISO = updatedUTC.format(DateTimeFormatter.ISO_INSTANT);

            TaskSelectedDTO dto = mapper.map(task, TaskSelectedDTO.class);
            dto.setCreatedOn(createdOnISO);
            dto.setUpdatedOn(updatedOnISO);


            return dto;

        }else{
            throw new ForBiddenException("You do not have permission to access this resource");
        }

    }

    @Transactional
    public TaskDTO createTask(String boardId,TaskAddEditDTO newTaskDTO, HttpServletRequest request) {
        Board board = boardRepository.findBoardById(boardId);
        if(board == null) {
            throw new ItemNotFoundException("Board id: " + boardId + " doesn't exist !!!");
        }
        Claims claims = jwtTokenUtil.decodedToken(request);
        if(permission.getRoleOfBoard(boardId, claims.get("oid").toString())){
            Status statusfind = statusRepository.findStatusByBoard_IdAndId(boardId,newTaskDTO.getTaskStatusId());
            if(statusfind == null) throw new BadRequestWithFieldException("status", "Status id "+newTaskDTO.getTaskStatusId()+" or Board id"+ boardId +" does not exist");

            limitService.CheckLimitTask(boardId,statusfind.getId());

            newTaskDTO.setBoardId(boardId);
            Task newTask = mapper.map(newTaskDTO, Task.class);
            newTask.setTaskTitle(newTaskDTO.getTaskTitle() == null ? null : newTaskDTO.getTaskTitle().trim());
            newTask.setTaskDescription(newTaskDTO.getTaskDescription() == null || newTaskDTO.getTaskDescription().isEmpty() ? null : newTaskDTO.getTaskDescription().trim());
            newTask.setTaskAssignees(newTaskDTO.getTaskAssignees() == null || newTaskDTO.getTaskAssignees().isEmpty() ? null : newTaskDTO.getTaskAssignees().trim());
            System.out.println(newTask);
            repository.saveAndFlush(newTask);
            Status status = mapper.map(statusfind, Status.class);
            newTask.setTaskStatus(status);
            return mapper.map(newTask, TaskDTO.class);
        }else {
            throw new ForBiddenException("You do not have permission to access this resource");
        }

    }

    @Transactional
    public TaskDTO updateTask(String boardId,Integer taskId, TaskAddEditDTO editedTask, HttpServletRequest request) {
        if (editedTask.getTaskTitle() == null) throw new BadRequestWithFieldException("titie", "must not be null");

        limitService.CheckLimitTask(boardId,editedTask.getTaskStatusId());

        Status isExited = statusRepository.findStatusByBoard_IdAndId(boardId,editedTask.getTaskStatusId());
        if(isExited == null) throw new BadRequestWithFieldException("status", "Status id "+editedTask.getTaskStatusId()+"or Board id"+ boardId +" does not exist");


        Task oldTask = repository.findTaskByBoard_IdAndId(boardId, taskId);
        if (oldTask == null) throw new ItemNotFoundException("Task id: " + taskId + " or boardId: "+ boardId +" doesn't exist !!!");

        Claims claims = jwtTokenUtil.decodedToken(request);
        if(permission.getRoleOfBoard(boardId, claims.get("oid").toString())) {
            Optional.ofNullable(editedTask.getTaskTitle())
                    .map(String::trim)
                    .ifPresent(editedTask::setTaskTitle);
            Optional.ofNullable(editedTask.getTaskDescription())
                    .map(String::trim)
                    .ifPresent(editedTask::setTaskDescription);
            Optional.ofNullable(editedTask.getTaskAssignees())
                    .map(String::trim)
                    .ifPresent(editedTask::setTaskAssignees);
            oldTask.setId(editedTask.getId() != null ? editedTask.getId() : oldTask.getId());
            oldTask.setTaskTitle(editedTask.getTaskTitle() != null ? editedTask.getTaskTitle() : oldTask.getTaskTitle());
            oldTask.setTaskAssignees(editedTask.getTaskAssignees() != null ? editedTask.getTaskAssignees() : oldTask.getTaskAssignees());
            oldTask.setTaskStatus(editedTask.getTaskStatusId() != null ? isExited : oldTask.getTaskStatus());
            oldTask.setTaskDescription(editedTask.getTaskDescription() != null ? editedTask.getTaskDescription() : oldTask.getTaskDescription());
            Date now = new Date();
            Timestamp timestamp = new Timestamp(now.getTime());
            oldTask.setUpdatedOn(timestamp);

            repository.saveAndFlush(oldTask);
            StatusSelectedDTO newStatus = statusService.getStatusByIdAndBoardId(boardId,oldTask.getTaskStatus().getId(),request);
            TaskBoardtestDTO dto = mapper.map(oldTask,TaskBoardtestDTO.class);
            dto.setTaskStatus(newStatus);

            return mapper.map(dto, TaskDTO.class);
        }else {
            throw new ForBiddenException("You do not have permission to access this resource");
        }

    }

    @Transactional
    public TaskDTO deleteTask(String boardId,Integer delId, HttpServletRequest request) {
        Task delTask = repository.findTaskByBoard_IdAndId(boardId, delId);
        if (delTask == null) throw new ItemNotFoundException("Task id: " + delId + " or boardId: "+ boardId +" doesn't exist !!!");
        Claims claims = jwtTokenUtil.decodedToken(request);
        if(permission.getRoleOfBoard(boardId, claims.get("oid").toString())) {
            repository.delete(delTask);
            return mapper.map(delTask, TaskDTO.class);
        }else {
            throw new ForBiddenException("You do not have permission to access this resource");
        }

    }

    // Version 3

    public List<TaskBoardDTO> getTaskofBoard(String boardId, HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7).trim();
        Claims claims = jwtTokenUtil.getAllClaimsFromToken(token);
        if(visibilityConfig.visibilityType(boardId)  || permission.getRoleOfBoard(boardId, claims.get("oid").toString())){
            List<Task> taskBoard = repository.findTaskByBoard_Id(boardId);
            if (taskBoard.isEmpty()) {
                throw new BadRequestException("This board has no task !!");
            }
            return listMapper.mapList(taskBoard, TaskBoardDTO.class);
        }else{
            throw new ForBiddenException("You do not have permission to access this resource");
        }

    }

}