package com.example.kanbanbackend.Service;


import com.example.kanbanbackend.DTO.TaskDTO;
import com.example.kanbanbackend.DTO.TaskSelectedDTO;
import com.example.kanbanbackend.Entitites.Task;
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

}
