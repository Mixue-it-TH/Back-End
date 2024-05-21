package com.example.kanbanbackend.Controller;

import com.example.kanbanbackend.DTO.TaskAddDTO;
import com.example.kanbanbackend.DTO.TaskEditDTO;
import com.example.kanbanbackend.DTO.TaskDTO;
import com.example.kanbanbackend.Service.TaskService;
import com.example.kanbanbackend.Utils.LimitConfig;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin(origins = {"http://localhost:5173","http://ip23sy2.sit.kmutt.ac.th","https://intproj23.sit.kmutt.ac.th"})
@RequestMapping("/v2/tasks")
public class TaskController {
    @Autowired
    private TaskService service;


    @GetMapping("")
    public ResponseEntity<Object> getAllTask(@RequestParam(required = false) List<String> filterStatuses,
                                             @RequestParam(defaultValue = "id")String sortBy,
                                             @RequestParam(defaultValue = "ASC")String sortDirection){
        return ResponseEntity.ok(service.getAllTodo(filterStatuses,sortBy,sortDirection));
    }



    @GetMapping("/{id}")
    public ResponseEntity<Object> getTaskById(@PathVariable Integer id){
        return ResponseEntity.ok(service.getTaskById(id));
    }

    @PostMapping("")
    public ResponseEntity<Object> addTask(@Valid @RequestBody TaskAddDTO newTaskDTO){
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createTask(newTaskDTO)   );
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateTask(@Valid @PathVariable Integer id, @Valid @RequestBody TaskEditDTO editedTask){
        System.out.println(id);
        System.out.println(editedTask);
        return ResponseEntity.ok(service.updateTask(id, editedTask));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteTask(@PathVariable Integer id){
        TaskDTO taskDetail = service.getTaskByIdForDel(id);
        service.deleteTask(id);
        return ResponseEntity.ok(taskDetail);
    }

}
