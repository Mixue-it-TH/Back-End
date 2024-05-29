package com.example.kanbanbackend.Service;


import com.example.kanbanbackend.DTO.StatusDTO.StatusSelectedDTO;
import com.example.kanbanbackend.DTO.TaskDTO.TaskAddEditDTO;
import com.example.kanbanbackend.DTO.TaskDTO.TaskDTO;
import com.example.kanbanbackend.DTO.TaskDTO.TaskSelectedDTO;
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
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
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
public class TaskService {
    @Autowired
    ModelMapper mapper;
    @Autowired
    ListMapper listMapper;
    @Autowired
    private TaskRepository repository;

    @Autowired
    private StatusRepository statusRepository;

    @Autowired
    private StatusService statusService;

    @Autowired
    private Permission permission;


    public List<TaskDTO> getAllTodo(List<String> filterStatuses, String sortBy, String sortDirection){

        List<Task> taskList = new ArrayList<>();
        Sort.Direction direction = sortDirection.equalsIgnoreCase("ASC") ? Sort.Direction.ASC : Sort.Direction.DESC;

        if ("statusName".equals(sortBy)) {
            sortBy = "taskStatus.statusName";
        }
        Sort sort = Sort.by(direction,sortBy);
        if(filterStatuses != null){
                taskList.addAll(repository.findAllByStatusNamesSorted(filterStatuses,sort)) ;
            return  listMapper.mapList(taskList, TaskDTO.class);
        }
        List<Task> tasks = repository.findAll(sort);
        return  listMapper.mapList(tasks,TaskDTO.class);
    }

    public TaskSelectedDTO getTaskById(int id) throws ItemNotFoundException {
        Task task =  repository.findById(id).orElseThrow(() -> new ItemNotFoundException("Task "+ id +" does not exist !!!" ));

        LocalDateTime createdDateTime = LocalDateTime.parse(task.getCreatedOn(), DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm:ss", Locale.ROOT));
        LocalDateTime updatedDateTime = LocalDateTime.parse(task.getUpdatedOn(), DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm:ss", Locale.ROOT));

        ZonedDateTime createdUTC = createdDateTime.atZone(ZoneOffset.UTC);
        ZonedDateTime updatedUTC = updatedDateTime.atZone(ZoneOffset.UTC);



        task.setCreatedOn(DateTimeFormatter.ISO_INSTANT.format(createdUTC));
        task.setUpdatedOn(DateTimeFormatter.ISO_INSTANT.format(updatedUTC));

        return mapper.map(task, TaskSelectedDTO.class);


    }

    @Transactional
    public TaskDTO createTask(TaskAddEditDTO newTaskDTO) {
        Status statusfind = statusRepository.findById(newTaskDTO.getTaskStatusId()).orElseThrow(() -> new BadRequestException("Status "+ newTaskDTO.getTaskStatusId() +" does not exist !!!" ));
        if(LimitConfig.isLimit && permission.canEditOrDelete(newTaskDTO.getTaskStatusId())){
            List<Task> listTasks = repository.findByTaskStatus(statusfind);
            if(listTasks.size() >= LimitConfig.number) {
                throw new BadRequestException("The Status has on the limit ("+ LimitConfig.number +")s");
            }
        }
        Task newTask = mapper.map(newTaskDTO,Task.class);
        newTask.setTaskTitle(newTaskDTO.getTaskTitle() == null ? null : newTaskDTO.getTaskTitle().trim());
        newTask.setTaskDescription(newTaskDTO.getTaskDescription() == null || newTaskDTO.getTaskDescription().isEmpty()  ? null  : newTaskDTO.getTaskDescription().trim());
        newTask.setTaskAssignees(newTaskDTO.getTaskAssignees() == null || newTaskDTO.getTaskAssignees().isEmpty() ? null : newTaskDTO.getTaskAssignees().trim());
        repository.saveAndFlush(newTask);
        Status status = mapper.map(statusfind,Status.class);
        newTask.setTaskStatus(status);
        return mapper.map(newTask, TaskDTO.class);
    }
    @Transactional
    public TaskDTO updateTask(Integer taskId, TaskAddEditDTO editedTask ){
        if(editedTask.getTaskTitle() == null) throw new BadRequestWithFieldException("titie","must not be null");
        if(LimitConfig.isLimit && permission.canEditOrDelete(editedTask.getTaskStatusId())){

            List<Task> listTasks = repository.findByTaskStatusId(editedTask.getTaskStatusId());
            if(listTasks.size() >= LimitConfig.number) {
                throw new BadRequestException("The Status has on the limit (" + LimitConfig.number + ")s You can't edit !!!");
            }
        }
       Status isExited = statusRepository.findById(editedTask.getTaskStatusId()).orElseThrow(() -> new BadRequestWithFieldException("status","does not exist"));
        Task oldTask = repository.findById(taskId).orElseThrow(() -> new ItemNotFoundDelUpdate( "NOT FOUND "));
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
        repository.save(oldTask);
        StatusSelectedDTO newStatus = statusService.getStatusById(oldTask.getTaskStatus().getId());
        Status status = mapper.map(newStatus,Status.class);
        oldTask.setTaskStatus(status);
        return mapper.map(oldTask, TaskDTO.class);
    }

    @Transactional
    public TaskDTO deleteTask(Integer delId){
        Task delTask = repository.findById(delId).orElseThrow(() -> new ItemNotFoundDelUpdate( "NOT FOUND"));
        repository.delete(delTask);
        return mapper.map(delTask, TaskDTO.class);
    }





}