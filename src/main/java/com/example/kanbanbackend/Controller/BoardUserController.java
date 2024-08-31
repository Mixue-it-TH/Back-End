package com.example.kanbanbackend.Controller;

import com.example.kanbanbackend.DTO.BoardDTO;
import com.example.kanbanbackend.Entitites.Primary.BoardUser;
import com.example.kanbanbackend.Service.BoardService;
import com.example.kanbanbackend.Service.BoardUserService;
import com.example.kanbanbackend.Service.StatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@CrossOrigin(origins = {"http://ip23sy2.sit.kmutt.ac.th","http://intproj23.sit.kmutt.ac.th","http://localhost:5173"})
@RequestMapping("/v3/board")
public class BoardUserController {

    @Autowired
    private BoardUserService service;
    @Autowired
    private BoardService boardService;

    @GetMapping("")
    public ResponseEntity<Object> getAllStatus(){
        return ResponseEntity.ok(service.getAllBoardUser());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getBoardUserById(@PathVariable String id){
        return ResponseEntity.ok(service.getBoardUserById(id));
    }

    @PostMapping("")
    public ResponseEntity<Object> createBoardUser(@RequestBody BoardDTO boardDTO){
        return ResponseEntity.status(HttpStatus.CREATED).body(boardService.createBoardUser(boardDTO.getBoardName()));
    }
}
