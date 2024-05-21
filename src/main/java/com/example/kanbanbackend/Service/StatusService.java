package com.example.kanbanbackend.Service;

import com.example.kanbanbackend.DTO.LimitFunc.LimitConfigDTO;
import com.example.kanbanbackend.DTO.LimitFunc.LimitDetailsDTO;
import com.example.kanbanbackend.DTO.LimitFunc.StatusMaximum;
import com.example.kanbanbackend.DTO.LimitFunc.StatusTasksNumDTO;
import com.example.kanbanbackend.DTO.StatusDTO;
import com.example.kanbanbackend.DTO.StatusEditDTO;
import com.example.kanbanbackend.DTO.StatusSelectedDTO;
import com.example.kanbanbackend.Entitites.Status;
import com.example.kanbanbackend.Entitites.Task;
import com.example.kanbanbackend.Exception.BadRequestException;
import com.example.kanbanbackend.Exception.BadRequestWithFieldException;
import com.example.kanbanbackend.Exception.ItemNotFoundDelUpdate;
import com.example.kanbanbackend.Exception.ItemNotFoundException;
import com.example.kanbanbackend.Repository.StatusRepository;
import com.example.kanbanbackend.Repository.TaskRepository;
import com.example.kanbanbackend.Utils.LimitConfig;
import com.example.kanbanbackend.Utils.Permission;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
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
    private Permission permission;

    public List<StatusDTO> getAllStatus() {
        List<Status> status = repository.findAll();
        return listMapper.mapList(status, StatusDTO.class);
    }

    public StatusSelectedDTO getStatusById(int id) {
        Status status = repository.findById(id).orElseThrow(() -> new ItemNotFoundException("Status " + id + " does not exist !!!"));

        LocalDateTime createdDateTime = LocalDateTime.parse(status.getCreatedOn(), DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm:ss", Locale.ROOT));
        LocalDateTime updatedDateTime = LocalDateTime.parse(status.getUpdatedOn(), DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm:ss", Locale.ROOT));

        ZonedDateTime createdUTC = createdDateTime.atZone(ZoneOffset.UTC);
        ZonedDateTime updatedUTC = updatedDateTime.atZone(ZoneOffset.UTC);


        status.setCreatedOn(DateTimeFormatter.ISO_INSTANT.format(createdUTC));
        status.setUpdatedOn(DateTimeFormatter.ISO_INSTANT.format(updatedUTC));

        return mapper.map(status, StatusSelectedDTO.class);
    }

    public StatusDTO createStatus(StatusDTO newStatusDTO) {
        Status duplicate = repository.findStatusByStatusName(newStatusDTO.getStatusName());
        if(duplicate != null) throw new BadRequestWithFieldException("name","must be unique");

        Status status = mapper.map(newStatusDTO, Status.class);
        System.out.println("status:" + status);
        if (status.getStatusColor() == null || status.getStatusColor().isBlank()) {
            status.setStatusColor("#6b7280");
        }
        Optional.ofNullable(status.getStatusName())
                .map(String::trim)
                .ifPresent(status::setStatusName);
        Optional.ofNullable(status.getStatusDescription())
                .map(String::trim)
                .ifPresent(status::setStatusDescription);
        repository.saveAndFlush(status);
        return mapper.map(status, StatusDTO.class);
    }

    public StatusDTO updateStatus(Integer statusId, StatusEditDTO editedStatus) {
        if (!permission.canEditOrDelete(statusId)) {
            throw new BadRequestException("No Status cannot be modified. and Done cannot be modified. respectively.");
        }
        Status isDuplicate = repository.findStatusByStatusName(editedStatus.getStatusName());
        if(isDuplicate != null){
            throw new BadRequestWithFieldException("name","must be unique");
        }
        Status oldStatus = repository.findById(statusId).orElseThrow(() -> new ItemNotFoundDelUpdate(" NOT FOUND "));
        oldStatus.setStatusName(editedStatus.getStatusName() != null ? editedStatus.getStatusName().trim() : oldStatus.getStatusName());
        oldStatus.setStatusDescription(editedStatus.getStatusDescription() == null ? null : editedStatus.getStatusDescription().trim());
        oldStatus.setStatusColor(editedStatus.getStatusColor() != null ? editedStatus.getStatusColor() : oldStatus.getStatusColor());
        repository.save(oldStatus);
        return mapper.map(oldStatus, StatusDTO.class);
    }

    public void deleteStatus(Integer delId) {
        Status statusDel = repository.findById(delId).orElseThrow(() -> new ItemNotFoundDelUpdate("NOT FOUND "));
        List<Task> taskStillUse = taskRepository.findByTaskStatus(statusDel);
        if(!taskStillUse.isEmpty()){
            throw new BadRequestException("destination cannot be " + statusDel.getStatusName() +  "for task transfer not specified.");
        }
        if (!permission.canEditOrDelete(delId)) {
            throw new BadRequestException("No Status cannot be deleted. and Done cannot be deleted. respectively");
        } else if (LimitConfig.isLimit && permission.canEditOrDelete(delId)) {
            List<Task> listTasks = taskRepository.findByTaskStatus(statusDel);
            if (listTasks.size() >= LimitConfig.number) {
                throw new BadRequestException("You can't delete" + statusDel.getStatusName() + "have on the limit");
            }
        }
        repository.delete(statusDel);
    }

    public void deleteStatusAndTransfer(Integer delId, Integer tranferId) {
        if(delId.equals(tranferId) ){
            throw new BadRequestException("destination cannot be status for task transfer must be different from current status");
        }
        Status statusDel = repository.findById(delId).orElseThrow(() -> new ItemNotFoundDelUpdate("NOT FOUND"));
        Status statusTranfer = repository.findById(tranferId).orElseThrow(() -> new BadRequestException("the specified status for task transfer does not exist."));
        if (LimitConfig.isLimit) {

            List<Task> listTasks = taskRepository.findByTaskStatus(statusDel);
            List<Task> listTasksTransfer = taskRepository.findByTaskStatus(statusTranfer);

            if ((listTasks.size() + listTasksTransfer.size()) > LimitConfig.number) {
                throw new BadRequestException("You can't delete" + statusDel.getStatusName() + "have on the limit");
            }
        }
        Status statusTransfer = repository.findById(tranferId).orElseThrow(() -> new ItemNotFoundDelUpdate("NOT FOUND"));
        if (statusDel != null && statusTransfer != null) {
            List<Task> taskList = taskRepository.findByTaskStatus(statusDel);
            taskList.forEach((task) -> {
                task.setTaskStatus(statusTransfer);
                taskRepository.save(task);
            });
            repository.delete(statusDel);
        }
    }

    public LimitConfigDTO getLimitConfig() {
        LimitConfigDTO limitConfigDTO = new LimitConfigDTO();
        limitConfigDTO.setLimitMaximumTask(LimitConfig.isLimit);
        limitConfigDTO.setNoOfTasks(LimitConfig.number);
        return limitConfigDTO;
    }

    public LimitDetailsDTO checkExceedLimit(StatusMaximum statusConfig) {
        LimitConfig.isLimit = statusConfig.getlimitMaximumTask();
        LimitConfig.number = statusConfig.getNumber();
        List<Status> statusList = repository.findAll();
        List<Integer> numOfTasks = new ArrayList<>();
        LimitDetailsDTO statusTaskLimitDTO = new LimitDetailsDTO();
        statusList.removeIf(status -> {
            List<Task> tasks = taskRepository.findByTaskStatus(status);
            if (tasks.size() >= LimitConfig.number && permission.canEditOrDelete(status.getId())) {
                numOfTasks.add(tasks.size());
            }
            return tasks.size() < LimitConfig.number || !permission.canEditOrDelete(status.getId());
        });
        System.out.println(statusList);
        List<StatusTasksNumDTO> statusTasksNumDTO = listMapper.mapList(statusList, StatusTasksNumDTO.class);
        for (int i = 0; i < statusTasksNumDTO.size(); i++) {
            statusTasksNumDTO.get(i).setNumOfTasks(numOfTasks.get(i));
        }
        statusTaskLimitDTO.setStatusList(statusTasksNumDTO);
        statusTaskLimitDTO.setNoOfTasks(LimitConfig.number);
        statusTaskLimitDTO.setLimitMaximumTask(LimitConfig.isLimit);
        return statusTaskLimitDTO;
    }

}
