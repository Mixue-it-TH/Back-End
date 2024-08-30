package com.example.kanbanbackend.Controller;

import com.example.kanbanbackend.Service.BoardUserService;
import com.example.kanbanbackend.Service.StatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = {"http://ip23sy2.sit.kmutt.ac.th","http://intproj23.sit.kmutt.ac.th","http://localhost:5173"})
@RequestMapping("/v3/board")
public class BoardUserController {

    @Autowired
    private BoardUserService service;

    @GetMapping("")
    public ResponseEntity<Object> getAllStatus(){
        return ResponseEntity.ok(service.getAllBoardUser());
    }
}
