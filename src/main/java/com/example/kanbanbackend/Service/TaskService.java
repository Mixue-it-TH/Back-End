package com.example.kanbanbackend.Service;


import com.example.kanbanbackend.DTO.TaskDTO;
import com.example.kanbanbackend.Entitites.Task;
import com.example.kanbanbackend.Exception.ItemNotFoundException;
import com.example.kanbanbackend.Repository.TaskRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.List;

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

    public Task getTaskById(int id) throws ItemNotFoundException {
        Task task =  repository.findById(id).orElseThrow(() -> new ItemNotFoundException("Task "+ id +" does not exist !!!" ));
        ZoneId defaultZone = ZoneId.systemDefault();
        ZoneOffset offset = defaultZone.getRules().getOffset(java.time.LocalDateTime.now());
        if(offset.equals(ZoneOffset.ofHours(7))){
            task.setCreatedOn(task.getCreatedOn()+" ICT");
            task.setUpdatedOn(task.getUpdatedOn()+" ICT");
        }
        return task;
    }

}
