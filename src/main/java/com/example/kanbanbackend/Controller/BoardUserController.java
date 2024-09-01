package com.example.kanbanbackend.Controller;

import com.example.kanbanbackend.DTO.BoardDTO;
import com.example.kanbanbackend.DTO.LimitFunc.LimitConfigDTO;
import com.example.kanbanbackend.DTO.StatusDTO.StatusDTO;
import com.example.kanbanbackend.DTO.TaskDTO.TaskAddEditDTO;
import com.example.kanbanbackend.Entitites.Primary.Config;
import com.example.kanbanbackend.Service.BoardService;
import com.example.kanbanbackend.Service.BoardUserService;
import com.example.kanbanbackend.Service.StatusService;
import com.example.kanbanbackend.Service.TaskService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = {"http://ip23sy2.sit.kmutt.ac.th", "http://intproj23.sit.kmutt.ac.th", "http://localhost:5173"})
@RequestMapping("/v3/boards")
public class BoardUserController {

    @Autowired
    private BoardUserService service;
    @Autowired
    private BoardService boardService;
    @Autowired
    private TaskService taskService;

    @Autowired
    private StatusService statusService;

    @GetMapping("")
    public ResponseEntity<Object> getAllBoardUser() {
        return ResponseEntity.ok(service.getAllBoardUser());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getBoardUserById(@PathVariable String id) {
        System.out.println(id);
        return ResponseEntity.ok(boardService.getBoardUserByBoardId(id));
    }

    @GetMapping("/{id}/tasks")
    public ResponseEntity<Object> getBoardTask(@PathVariable String id) {
        return ResponseEntity.ok(taskService.getTaskofBoard(id));
    }


    @GetMapping("/{id}/tasks/{taskId}")
    public ResponseEntity<Object> getBoardTaskByTaskId(@PathVariable String id, @PathVariable int taskId) {
        return ResponseEntity.ok(taskService.getTaskById(id, taskId));
    }

    @PostMapping("")
    public ResponseEntity<Object> createBoardUser(@RequestBody BoardDTO boardDTO, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(boardService.createBoardUser(boardDTO, request));
    }

    @PostMapping("/{id}/tasks")
    public ResponseEntity<Object> addTask(@PathVariable String id, @Valid @RequestBody TaskAddEditDTO newTaskDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(taskService.createTask(id, newTaskDTO));
    }

    @PutMapping("/{id}/tasks/{taskId}")
    public ResponseEntity<Object> updateTaskByBoardId(@PathVariable String id, @PathVariable Integer taskId, @Valid @RequestBody TaskAddEditDTO editedTask) {
        return ResponseEntity.ok(taskService.updateTask(id, taskId, editedTask));
    }

    @DeleteMapping("/{id}/tasks/{taskId}")
    public ResponseEntity<Object> deleteTask(@PathVariable String id,@PathVariable Integer taskId){
        return ResponseEntity.ok(taskService.deleteTask(id,taskId));
    }

    @GetMapping("{id}/statuses")
    public ResponseEntity<Object> getAllStatus(@PathVariable String id){
        return ResponseEntity.ok(statusService.getAllStatusByBoardId(id));
    }


    @GetMapping("{id}/statuses/{statusId}")
    public ResponseEntity<Object> getStatusById(@PathVariable String id,@PathVariable Integer statusId){
        return ResponseEntity.ok(statusService.getStatusByIdAndBoardId(id,statusId));
    }

    @PostMapping("{id}/statuses")
    public ResponseEntity<Object> addStatus(@PathVariable String id,@Valid @RequestBody StatusDTO newStatusDTO){
        return ResponseEntity.status(HttpStatus.CREATED).body(statusService.createStatus(id,newStatusDTO));
    }

    @PutMapping("/{id}/statuses/{statusId}")
    public ResponseEntity<Object> updateStatus(@PathVariable String id,@Valid @PathVariable Integer statusId,@Valid @RequestBody StatusDTO editedStatus){
        return ResponseEntity.ok(statusService.updateStatus(id,statusId, editedStatus));
    }

    @DeleteMapping("/{id}/statuses/{statusId}")
    public ResponseEntity<Object> deleteStatus(@PathVariable String id,@PathVariable Integer statusId){

        statusService.deleteStatus(id,statusId);
        return ResponseEntity.ok().body("{}");
    }

    @DeleteMapping("/{id}/statuses/{statusId}/{newStatusId}")
    public ResponseEntity<Object> deleteStatusAndTransferToStatus(@PathVariable String id,@PathVariable Integer statusId, @PathVariable Integer newStatusId){
        statusService.deleteStatusAndTransfer(id,statusId,newStatusId);
        return ResponseEntity.ok().body("{}");
    }
    @GetMapping("{id}/statuses/islimit")
    public ResponseEntity<Object> getLimitConfig(@PathVariable String id){
        return ResponseEntity.ok(statusService.getLimitConfig(id));
    }

    @PatchMapping("/{id}/statuses/maximum-task")
    public ResponseEntity<Object> statusLimitConfig(@PathVariable String id,@RequestBody Config config){
        return ResponseEntity.ok(statusService.checkExceedLimit(id,config));
    }



}
