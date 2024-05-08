package com.example.kanbanbackend.Controller;

import com.example.kanbanbackend.DTO.TaskDTO;
import com.example.kanbanbackend.Entitites.Task;
import com.example.kanbanbackend.Service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
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

    @PostMapping("")
    public ResponseEntity<Object> addTask(@RequestBody Task newTask){
        return ResponseEntity.ok(service.createTask(newTask));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateTask(@PathVariable Integer id, @RequestBody Task editedTask){
        return ResponseEntity.ok(service.updateTask(id, editedTask));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteTask(@PathVariable Integer id){
        TaskDTO taskDetail = service.getTaskByIdForDel(id);
        service.deleteTask(id);
        return ResponseEntity.ok(taskDetail);

    }
}
