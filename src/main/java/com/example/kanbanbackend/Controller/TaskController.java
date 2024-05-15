package com.example.kanbanbackend.Controller;

import com.example.kanbanbackend.DTO.TaskAddEditDTO;
import com.example.kanbanbackend.DTO.TaskDTO;
import com.example.kanbanbackend.Entitites.Task;
import com.example.kanbanbackend.Service.TaskService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = {"http://localhost:5173","http://ip23sy2.sit.kmutt.ac.th"})
@RequestMapping("/v1/tasks")
public class TaskController {
    @Autowired
    private TaskService service;

    @Autowired
    private ModelMapper mapper;


    @GetMapping("")
    public ResponseEntity<Object> getAllTask(@RequestParam(required = false) List<String> statusName){
        return ResponseEntity.ok(service.getAllTodo(statusName));
    }


    @GetMapping("/{id}")
    public ResponseEntity<Object> getTaskById(@PathVariable Integer id){
        return ResponseEntity.ok(service.getTaskById(id));
    }

    @PostMapping("")
    public ResponseEntity<Object> addTask(@RequestBody TaskAddEditDTO newTaskDTO){
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createTask(newTaskDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateTask(@PathVariable Integer id, @RequestBody TaskAddEditDTO editedTask){
        return ResponseEntity.ok(service.updateTask(id, editedTask));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteTask(@PathVariable Integer id){
        TaskDTO taskDetail = service.getTaskByIdForDel(id);
        service.deleteTask(id);
        return ResponseEntity.ok(taskDetail);

    }
}
