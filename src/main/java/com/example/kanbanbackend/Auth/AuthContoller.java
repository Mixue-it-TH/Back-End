package com.example.kanbanbackend.Auth;


import com.example.kanbanbackend.DTO.Token;
import com.example.kanbanbackend.Repository.Primary.PrimaryUserRepository;
import com.example.kanbanbackend.Service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@CrossOrigin(origins = {"http://ip23sy2.sit.kmutt.ac.th", "http://intproj23.sit.kmutt.ac.th", "https://ip23sy2.sit.kmutt.ac.th", "https://intproj23.sit.kmutt.ac.th", "http://localhost:5173"}, allowCredentials = "true")
@RequestMapping("/login")
public class AuthContoller {

    @Autowired
    private UserService service;


    @GetMapping("")
    public void redirectToMicrosoftLogin(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String originalUrl = request.getRequestURL().toString();
        if (!originalUrl.contains("/sy2")) {
            String newUrl = "/sy2" + request.getRequestURI();
            response.sendRedirect(newUrl);
        } else {
            response.sendRedirect("/oauth2/authorization/azure-dev");
        }
    }

    @PostMapping("")
    public ResponseEntity<Token> Login(@Valid @RequestBody JwtRequestUser user) {
        return ResponseEntity.ok(service.login(user));
    }

    @GetMapping("/get-token")
    public ResponseEntity<Object> getToken(Authentication authentication) {
        return ResponseEntity.ok(service.getTokenByMicrosoftAuthen(authentication));
    }


}
