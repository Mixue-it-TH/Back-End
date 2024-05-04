package com.example.kanbanbackend.Service;


import com.example.kanbanbackend.DTO.TaskDTO;
import com.example.kanbanbackend.DTO.TaskSelectedDTO;
import com.example.kanbanbackend.Entitites.Task;
import com.example.kanbanbackend.Exception.ItemNotFoundDelUpdate;
import com.example.kanbanbackend.Exception.ItemNotFoundException;
import com.example.kanbanbackend.Repository.TaskRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

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

    public TaskDTO createTask(Task newTask){
        System.out.println(newTask);
        if(newTask.getTaskStatus() == null || newTask.getTaskStatus().isBlank()){
            newTask.setTaskStatus("NO_STATUS");
        }
        repository.saveAndFlush(newTask);
        return mapper.map(newTask, TaskDTO.class);
    }

    public TaskDTO updateTask(Integer taskId, Task editedTask ){//เดี๋ยวกลับมาแก้ปัญหาเรื่อง null เวลา createOn updateOn
        Task oldTask = repository.findById(taskId).orElseThrow(() -> new ItemNotFoundDelUpdate( "NOT FOUND "));
        oldTask.setId(editedTask.getId());
        oldTask.setTaskTitle(editedTask.getTaskTitle());
        oldTask.setTaskAssignees(editedTask.getTaskAssignees());
        oldTask.setTaskStatus(editedTask.getTaskStatus());
        oldTask.setTaskDescription(editedTask.getTaskDescription());
        oldTask.setCreatedOn(oldTask.getCreatedOn());
//        oldTask.setUpdatedOn(formattedDate);
        repository.save(oldTask);
        return mapper.map(oldTask, TaskDTO.class);
    }

    public void deleteTask(Integer delId){
        Task delTask = repository.findById(delId).orElseThrow(() -> new ItemNotFoundDelUpdate( "He's already gone " + delId));
        repository.delete(delTask);
    }

    public TaskDTO getTaskByIdForDel(Integer id){
         Task task = repository.findById(id).orElseThrow(() -> new ItemNotFoundDelUpdate( "NOT FOUND "));
         return mapper.map(task, TaskDTO.class);
    }
}
