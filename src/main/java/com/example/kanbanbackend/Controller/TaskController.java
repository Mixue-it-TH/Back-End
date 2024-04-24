package com.example.kanbanbackend.Controller;

import com.example.kanbanbackend.Service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/itb-kk/v1/tasks")
public class TaskController {
    @Autowired
    private TaskService service;


    @GetMapping("")
    public ResponseEntity<Object> getAllTask(){
        return ResponseEntity.ok(service.getAllTodo());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getTaskById(@PathVariable Integer id){
        return ResponseEntity.ok(service.getTaskById(id));
    }

}
