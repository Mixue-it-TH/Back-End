package com.example.kanbanbackend.Controller;

import com.example.kanbanbackend.DTO.TaskDTO.TaskAddEditDTO;
import com.example.kanbanbackend.Service.TaskService;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = {"http://ip23sy2.sit.kmutt.ac.th","http://intproj23.sit.kmutt.ac.th","http://localhost:5173"})
@RequestMapping("/v3/boards")
public class TaskController {
    @Autowired
    private TaskService service;


//    @GetMapping("")
//    public ResponseEntity<Object> getAllTask(@RequestParam(required = false) List<String> filterStatuses,
//                                             @RequestParam(defaultValue = "id")String sortBy,
//                                             @RequestParam(defaultValue = "ASC")String sortDirection){
//        return ResponseEntity.ok(service.getAllTodo(filterStatuses,sortBy,sortDirection));
//    }
//
//
//
//    @GetMapping("/{id}")
//    public ResponseEntity<Object> getTaskById(@PathVariable Integer id){
//        return ResponseEntity.ok(service.getTaskById(id));
//    }
//
//    @PostMapping("")
//    public ResponseEntity<Object> addTask(@Valid @RequestBody TaskAddEditDTO newTaskDTO){
//        return ResponseEntity.status(HttpStatus.CREATED).body(service.createTask(newTaskDTO)   );
//    }
//
//    @PutMapping("/{id}")
//    public ResponseEntity<Object> updateTask(@Valid @PathVariable Integer id, @Valid @RequestBody TaskAddEditDTO editedTask){
//        return ResponseEntity.ok(service.updateTask(id, editedTask));
//    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Object> deleteTask(@PathVariable Integer id){
//        return ResponseEntity.ok(service.deleteTask(id));
//    }

    // Version 3


}
