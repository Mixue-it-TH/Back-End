package com.example.kanbanbackend.Controller;

import com.example.kanbanbackend.DTO.LimitFunc.LimitConfigDTO;
import com.example.kanbanbackend.DTO.StatusDTO.StatusDTO;
import com.example.kanbanbackend.Service.StatusService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = {"http://ip23sy2.sit.kmutt.ac.th","http://intproj23.sit.kmutt.ac.th","http://localhost:5173"})
@RequestMapping("/v2/statuses")
public class StatusController {
//    @Autowired
//    private StatusService service;
//
//    @GetMapping("")
//    public ResponseEntity<Object> getAllStatus(){
//        return ResponseEntity.ok(service.getAllStatus());
//    }
//
//    @GetMapping("/islimit")
//    public ResponseEntity<Object> getLimitConfig(){
//        return ResponseEntity.ok(service.getLimitConfig());
//    }
//
//    @GetMapping("/{id}")
//    public ResponseEntity<Object> getStatusById(@PathVariable Integer id){
//        return ResponseEntity.ok(service.getStatusById(id));
//    }
//
//    @PostMapping("")
//    public ResponseEntity<Object> addStatus(@Valid @RequestBody StatusDTO newStatusDTO){
//        return ResponseEntity.status(HttpStatus.CREATED).body(service.createStatus(newStatusDTO));
//    }
//
//    @PutMapping("/{id}")
//    public ResponseEntity<Object> updateStatus(@Valid @PathVariable Integer id,@Valid @RequestBody StatusDTO editedStatus){
//        return ResponseEntity.ok(service.updateStatus(id, editedStatus));
//    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Object> deleteStatus(@PathVariable Integer id){
//
//        service.deleteStatus(id);
//        return ResponseEntity.ok().body("{}");
//    }
//
//    @DeleteMapping("/{id}/{newId}")
//    public ResponseEntity<Object> deleteStatusAndTransferToStatus(@PathVariable Integer id,@PathVariable Integer newId){
//        service.deleteStatusAndTransfer(id,newId);
//        return ResponseEntity.ok().body("{}");
//    }
//    @PatchMapping("/maximum-task")
//    public ResponseEntity<Object> statusLimitConfig(@RequestBody LimitConfigDTO status){
//        return ResponseEntity.ok(service.checkExceedLimit(status));
//    }
//

}
