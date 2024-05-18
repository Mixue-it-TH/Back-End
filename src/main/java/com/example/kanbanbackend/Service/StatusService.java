package com.example.kanbanbackend.Service;

import com.example.kanbanbackend.DTO.StatusDTO;
import com.example.kanbanbackend.DTO.StatusEditDTO;
import com.example.kanbanbackend.DTO.StatusSelectedDTO;
import com.example.kanbanbackend.Entitites.Status;
import com.example.kanbanbackend.Entitites.Task;
import com.example.kanbanbackend.Exception.BadRequestException;
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

        Status status = mapper.map(newStatusDTO, Status.class);
        if (status.getStatusColor() == null || status.getStatusColor().isBlank()) {
            status.setStatusColor("#6b7280");
        }
        System.out.println(status);
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
            throw new BadRequestException("You can't edit No Status or Done");
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
        if (!permission.canEditOrDelete(delId)) {
            throw new BadRequestException("You can't delete" + statusDel.getStatusName());
        } else if (LimitConfig.isLimit && permission.canEditOrDelete(delId)) {
            List<Task> listTasks = taskRepository.findByTaskStatus(statusDel);
            if (listTasks.size() >= LimitConfig.number) {
                throw new BadRequestException("You can't delete" + statusDel.getStatusName() + "have on the limit");
            }
        }
        repository.delete(statusDel);
    }

    public void deleteStatusAndTransfer(Integer delId, Integer tranferId) {
        Status statusDel = repository.findById(delId).orElseThrow(() -> new ItemNotFoundDelUpdate("NOT FOUND"));
        if (LimitConfig.isLimit && permission.canEditOrDelete(delId)) {
            List<Task> listTasks = taskRepository.findByTaskStatus(statusDel);
            if (listTasks.size() >= LimitConfig.number) {
                throw new BadRequestException("You can't delete" + statusDel.getStatusName() + "have on the limit");
            }
        }
        Status statusTransfer = repository.findById(tranferId).orElseThrow(() -> new ItemNotFoundDelUpdate("NOT FOUND"));
        if (statusDel != null && statusTransfer != null) {
            List<Task> taskList = taskRepository.findByTaskStatus(statusDel);
            System.out.println(taskList);
            taskList.forEach((task) -> {
                task.setTaskStatus(statusTransfer);
                taskRepository.save(task);
            });
            repository.delete(statusDel);
        }
    }

}
