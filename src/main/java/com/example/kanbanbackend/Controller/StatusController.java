package com.example.kanbanbackend.Controller;

import com.example.kanbanbackend.DTO.StatusDTO;
import com.example.kanbanbackend.DTO.TaskAddEditDTO;
import com.example.kanbanbackend.DTO.TaskDTO;
import com.example.kanbanbackend.Entitites.Task;
import com.example.kanbanbackend.Service.StatusService;
import com.example.kanbanbackend.Service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = {"http://localhost:5173","http://ip23sy2.sit.kmutt.ac.th"})
@RequestMapping("/v2/statuses")
public class StatusController {
    @Autowired
    private StatusService service;

    @GetMapping("")
    public ResponseEntity<Object> getAllStatus(){
        return ResponseEntity.ok(service.getAllStatus());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getStatusById(@PathVariable Integer id){
        return ResponseEntity.ok(service.getStatusById(id));
    }

    @PostMapping("")
    public ResponseEntity<Object> addStatus(@RequestBody StatusDTO newStatusDTO){
        System.out.println(newStatusDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createStatus(newStatusDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateStatus(@PathVariable Integer id, @RequestBody StatusDTO editedStatus){
        return ResponseEntity.ok(service.updateStatus(id, editedStatus));
    }

    @DeleteMapping("/{id}")
    public void deleteStatus(@PathVariable Integer id){
        service.deleteStatus(id);
    }

    @DeleteMapping("/{id}/{newId}")
    public ResponseEntity<Object> deleteStatusAndTransferToStatus(@PathVariable Integer id,@PathVariable Integer newId){
        service.deleteStatusAndTransfer(id,newId);
        return ResponseEntity.ok().build();
    }


}
