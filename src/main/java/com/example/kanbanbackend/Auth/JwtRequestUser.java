package com.example.kanbanbackend.Auth;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class JwtRequestUser {

    @Size(max = 50,message = "Username or Password is invalid")
    @NotEmpty(message = "Please enter your username")
    private String username;
    @Size(max = 14,message = "Username or Password is invalid")
    @NotEmpty(message = "Please enter you password")
    private String password;
}
