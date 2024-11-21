package com.example.kanbanbackend.Controller;

import com.example.kanbanbackend.DTO.CollabsDTO.AccessDTO;
import com.example.kanbanbackend.DTO.CollabsDTO.CollabRequestDTO;
import com.example.kanbanbackend.DTO.PersonalBoardDTO;
import com.example.kanbanbackend.DTO.StatusDTO.StatusDTO;
import com.example.kanbanbackend.DTO.TaskDTO.TaskAddEditDTO;
import com.example.kanbanbackend.DTO.VisibilityDTO;
import com.example.kanbanbackend.Entitites.Primary.Config;
import com.example.kanbanbackend.Repository.Primary.TaskRepository;
import com.example.kanbanbackend.Service.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.mail.MessagingException;
import jakarta.mail.Multipart;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.List;

@RestController
@CrossOrigin(origins = {"http://ip23sy2.sit.kmutt.ac.th", "http://intproj23.sit.kmutt.ac.th", "https://ip23sy2.sit.kmutt.ac.th", "https://intproj23.sit.kmutt.ac.th", "http://localhost:5173"}, allowCredentials = "true")
@RequestMapping("/v3/boards")
public class BoardUserController {

    @Autowired
    FileService fileService;
    @Autowired
    private CollaboratorService collaboratorService;
    @Autowired
    private BoardService boardService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private InvitationService invitationService;
    @Autowired
    private StatusService statusService;
    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private ModelMapper modelMapper;

    //    @GetMapping("")
//    public ResponseEntity<Object> getAllBoardUser() {
//        return ResponseEntity.ok(collaboratorService.getAllCollaborator());
//    }

    // new version for PBI 24 ++
    @GetMapping("")
    public ResponseEntity<Object> getBoardUserByUserOID2(HttpServletRequest request) {
        return ResponseEntity.ok(collaboratorService.getPersonalAndColloboratorByToken(request));
    }


    @GetMapping("/user/{oid}")
    public ResponseEntity<Object> getBoardUserByUserOID(@PathVariable String oid) {
        return ResponseEntity.ok(collaboratorService.getPersonalAndColloboratorByUser_Oid(oid));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getBoardUserByBoardId(@PathVariable String id, HttpServletRequest request) {

        return ResponseEntity.ok(boardService.getBoardUserByBoardId(id, request));
    }

    @GetMapping("/{id}/tasks")
    public ResponseEntity<Object> getBoardTask(@PathVariable String id, HttpServletRequest request) {
        return ResponseEntity.ok(taskService.getTaskofBoard(id, request));
    }


    @GetMapping("/{id}/tasks/{taskId}")
    public ResponseEntity<Object> getBoardTaskByTaskId(@PathVariable String id, @PathVariable int taskId, HttpServletRequest request) {
        return ResponseEntity.ok(taskService.getTaskById(id, taskId, request));
    }

    @PostMapping("")
    public ResponseEntity<Object> createBoardUser(@Valid @RequestBody PersonalBoardDTO boardDTO, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(boardService.createBoardUser(boardDTO, request));
    }

    @PostMapping("/{id}/tasks")
    public ResponseEntity<Object> addTask(@PathVariable String id, @Valid @RequestBody TaskAddEditDTO newTaskDTO, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(taskService.createTask(id, newTaskDTO, request));
    }
@PutMapping("/{id}/tasks/{taskId}")
public ResponseEntity<Object> updateTaskByBoardId(
        @PathVariable String id,
        @PathVariable Integer taskId,
        @RequestParam("editedTask") String taskData,
        @RequestParam(value = "attachments", required = false) List<MultipartFile> attachments,
        HttpServletRequest request) throws IOException {

    ObjectMapper objectMapper = new ObjectMapper();
    TaskAddEditDTO editedTask = objectMapper.readValue(taskData, TaskAddEditDTO.class);

    return ResponseEntity.ok(taskService.updateTask(id, taskId, editedTask, attachments, request));
}

    @DeleteMapping("/{id}/tasks/{taskId}")
    public ResponseEntity<Object> deleteTask(@PathVariable String id, @PathVariable Integer taskId, HttpServletRequest request) throws Exception {
        return ResponseEntity.ok(taskService.deleteTask(id, taskId, request));
    }

    @GetMapping("{id}/statuses")
    public ResponseEntity<Object> getAllStatus(@PathVariable String id, HttpServletRequest request) {
        return ResponseEntity.ok(statusService.getAllStatusByBoardId(id, request));
    }


    @GetMapping("{id}/statuses/{statusId}")
    public ResponseEntity<Object> getStatusById(@PathVariable String id, @PathVariable Integer statusId, HttpServletRequest request) {
        return ResponseEntity.ok(statusService.getStatusByIdAndBoardId(id, statusId, request));
    }

    @PostMapping("{id}/statuses")
    public ResponseEntity<Object> addStatus(@PathVariable String id, @Valid @RequestBody StatusDTO newStatusDTO, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(statusService.createStatus(id, newStatusDTO, request));
    }

    @PutMapping("/{id}/statuses/{statusId}")
    public ResponseEntity<Object> updateStatus(@PathVariable String id, @Valid @PathVariable Integer statusId, @Valid @RequestBody StatusDTO editedStatus, HttpServletRequest request) {
        return ResponseEntity.ok(statusService.updateStatus(id, statusId, editedStatus, request));
    }

    @DeleteMapping("/{id}/statuses/{statusId}")
    public ResponseEntity<Object> deleteStatus(@PathVariable String id, @PathVariable Integer statusId, HttpServletRequest request) {

        statusService.deleteStatus(id, statusId, request);
        return ResponseEntity.ok().body("{}");
    }

    @DeleteMapping("/{id}/statuses/{statusId}/{newStatusId}")
    public ResponseEntity<Object> deleteStatusAndTransferToStatus(@PathVariable String id, @PathVariable Integer statusId, @PathVariable Integer newStatusId, HttpServletRequest request) {
        statusService.deleteStatusAndTransfer(id, statusId, newStatusId, request);
        return ResponseEntity.ok().body("{}");
    }

    @GetMapping("{id}/statuses/islimit")
    public ResponseEntity<Object> getLimitConfig(@PathVariable String id, HttpServletRequest request) {
        return ResponseEntity.ok(statusService.getLimitConfig(id, request));
    }

    @PatchMapping("/{id}/statuses/maximum-task")
    public ResponseEntity<Object> statusLimitConfig(@PathVariable String id, @RequestBody Config config, HttpServletRequest request) {
        return ResponseEntity.ok(statusService.checkExceedLimit(id, config, request));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> handleVisibility(@PathVariable String id, @Valid @RequestBody VisibilityDTO visibilityDTO, HttpServletRequest request) {
        return ResponseEntity.ok(boardService.setVisibility(id, visibilityDTO, request));
    }

    // COLABS ENDPOINT
    @GetMapping("/{id}/collabs")
    public ResponseEntity<Object> getAllCollabsByBoardId(@PathVariable String id) {
        return ResponseEntity.ok(boardService.getAllCollabsByBoardId(id));
    }

    @GetMapping("/{id}/collabs/{collabId}")
    public ResponseEntity<Object> getCollabByCollabId(@PathVariable String id, @PathVariable String collabId) {
        return ResponseEntity.ok(collaboratorService.getCollabByCollabId(id, collabId));
    }

    @PostMapping("/{id}/collabs")
    public ResponseEntity<Object> addCollab(@PathVariable String id, HttpServletRequest request, Authentication authentication) {
        return ResponseEntity.status(HttpStatus.CREATED).body(collaboratorService.addCollab(id, request,authentication));
    }

    @PatchMapping("/{id}/collabs/{collabId}")
    public ResponseEntity<Object> updateCollab(@PathVariable String id, @PathVariable String collabId, @Valid @RequestBody AccessDTO access, HttpServletRequest request) {
        return ResponseEntity.ok(collaboratorService.updateCollab(id, collabId, access, request));
    }

    @DeleteMapping("/{id}/collabs/{collabId}")
    public ResponseEntity<Object> deleteCollab(@PathVariable String id, @PathVariable String collabId, HttpServletRequest request) {
        return ResponseEntity.ok(collaboratorService.deleteCollab(id, collabId, request));
    }

    // SPRINT 5 ENDPOINTS
    @GetMapping("/{id}/collabs/invitations")
    public ResponseEntity<Object> getUserInvitedByBoardId(@PathVariable String id, HttpServletRequest request) {
        return ResponseEntity.ok(invitationService.getUserInvitedByBoardId(id, request));
    }

    @PostMapping("/{id}/collabs/invitations")
    public ResponseEntity<Object> createInvitation(@PathVariable String id, @Valid @RequestBody CollabRequestDTO collab, HttpServletRequest request, @RequestHeader(value = "Origin", required = false) String origin,Authentication authentication) throws MessagingException, UnsupportedEncodingException {
        return ResponseEntity.status(HttpStatus.CREATED).body(invitationService.createInvitation(id, collab, request, origin,authentication));
    }

    @PatchMapping("/{id}/collabs/invitations/{oid}")
    public ResponseEntity<Object> updateInvitation(@PathVariable String id, @PathVariable String oid, @Valid @RequestBody AccessDTO access, HttpServletRequest request) {
        return ResponseEntity.ok(invitationService.updateInvitation(id, oid, access, request));
    }

    @DeleteMapping("/{id}/collabs/invitations/{oid}")
    public ResponseEntity<Object> declineInvitation(@PathVariable String id, @PathVariable String oid, HttpServletRequest request) {
        return ResponseEntity.ok(invitationService.declineInvitation(id, oid, request));
    }


}
