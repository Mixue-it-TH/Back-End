package com.example.kanbanbackend.Service;


import com.example.kanbanbackend.DTO.TaskAddEditDTO;
import com.example.kanbanbackend.DTO.TaskDTO;
import com.example.kanbanbackend.DTO.TaskSelectedDTO;
import com.example.kanbanbackend.Entitites.Task;
import com.example.kanbanbackend.Exception.ItemNotFoundDelUpdate;
import com.example.kanbanbackend.Exception.ItemNotFoundException;
import com.example.kanbanbackend.Repository.TaskRepository;
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
public class TaskService {
    @Autowired
    ModelMapper mapper;
    @Autowired
    ListMapper listMapper;
    @Autowired
    private TaskRepository repository;

    public List<TaskDTO> getAllTodo(){
        List<Task> tasks = repository.findAll();
        return  listMapper.mapList(tasks,TaskDTO.class);
    }

    public TaskSelectedDTO getTaskById(int id) throws ItemNotFoundException {
        Task task =  repository.findById(id).orElseThrow(() -> new ItemNotFoundException("Task "+ id +" does not exist !!!" ));

        LocalDateTime createdDateTime = LocalDateTime.parse(task.getCreatedOn(), DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm:ss", Locale.ROOT));
        LocalDateTime updatedDateTime = LocalDateTime.parse(task.getUpdatedOn(), DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm:ss", Locale.ROOT));

        ZonedDateTime createdUTC = createdDateTime.atZone(ZoneOffset.UTC);
        ZonedDateTime updatedUTC = updatedDateTime.atZone(ZoneOffset.UTC);

        System.out.println(createdUTC);
        System.out.println(updatedUTC);

        task.setCreatedOn(DateTimeFormatter.ISO_INSTANT.format(createdUTC));
        task.setUpdatedOn(DateTimeFormatter.ISO_INSTANT.format(updatedUTC));

        return mapper.map(task, TaskSelectedDTO.class);
    }

    public TaskAddEditDTO createTask(TaskAddEditDTO newTask){
        Task task = mapper.map(newTask,Task.class);
        if(task.getTaskStatus() == null || task.getTaskStatus().isBlank()){
            task.setTaskStatus("NO_STATUS");
        }
        Optional.ofNullable(task.getTaskTitle())
                .map(String::trim)
                .ifPresent(task::setTaskTitle);
        Optional.ofNullable(task.getTaskDescription())
                .map(String::trim)
                .ifPresent(task::setTaskDescription);
        Optional.ofNullable(task.getTaskAssignees())
                .map(String::trim)
                .ifPresent(task::setTaskAssignees);
        repository.saveAndFlush(task);
        return mapper.map(task,TaskAddEditDTO.class);
    }

    public TaskDTO updateTask(Integer taskId, Task editedTask ){
        Task oldTask = repository.findById(taskId).orElseThrow(() -> new ItemNotFoundDelUpdate( "NOT FOUND "));
        oldTask.setId(editedTask.getId() != null ? editedTask.getId() : oldTask.getId());
        oldTask.setTaskTitle(editedTask.getTaskTitle() != null ? editedTask.getTaskTitle() : oldTask.getTaskTitle());
        oldTask.setTaskAssignees(editedTask.getTaskAssignees() != null ? editedTask.getTaskAssignees() : oldTask.getTaskAssignees());
        oldTask.setTaskStatus(editedTask.getTaskStatus() != null ? editedTask.getTaskStatus() : oldTask.getTaskStatus());
        oldTask.setTaskDescription(editedTask.getTaskDescription() != null ? editedTask.getTaskDescription() : oldTask.getTaskDescription());
        System.out.println(oldTask);
//        oldTask.setUpdatedOn(formattedDate);
        repository.save(oldTask);
        return mapper.map(oldTask, TaskDTO.class);
    }

    public void deleteTask(Integer delId){
        Task delTask = repository.findById(delId).orElseThrow(() -> new ItemNotFoundDelUpdate( "He's already gone " + delId));
        repository.delete(delTask);
    }

    public TaskDTO getTaskByIdForDel(Integer id){
         Task task = repository.findById(id).orElseThrow(() -> new ItemNotFoundDelUpdate( "I don't have this shit " + id));
         return mapper.map(task, TaskDTO.class);
    }
}
