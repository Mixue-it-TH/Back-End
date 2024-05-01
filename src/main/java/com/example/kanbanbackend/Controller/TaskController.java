package com.example.kanbanbackend.Controller;

import com.example.kanbanbackend.Service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = {"http://localhost:5173","http://ip23sy2.sit.kmutt.ac.th"})
@RequestMapping("/v1/tasks")
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
