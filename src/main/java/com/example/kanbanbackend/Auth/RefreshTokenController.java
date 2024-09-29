package com.example.kanbanbackend.Auth;


import com.example.kanbanbackend.DTO.Token;
import com.example.kanbanbackend.Entitites.Share.User;
import com.example.kanbanbackend.Service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = {"http://ip23sy2.sit.kmutt.ac.th", "http://intproj23.sit.kmutt.ac.th","https://ip23sy2.sit.kmutt.ac.th", "https://intproj23.sit.kmutt.ac.th", "http://localhost:5173"})
@RequestMapping("/token")
public class RefreshTokenController {

    @Autowired
    private UserService service;

    @PostMapping("")
    public ResponseEntity<Token> refreshTokenLogin(HttpServletRequest request) {
        return ResponseEntity.ok(service.refreshLogin(request));
    }


}
