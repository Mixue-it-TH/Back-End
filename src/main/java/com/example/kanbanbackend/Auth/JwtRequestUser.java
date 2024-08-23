package com.example.kanbanbackend.Auth;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class JwtRequestUser {

    @Size(max = 50,message = "Username or Password is invalid")
    @NotEmpty(message = "username can not be empty")
    private String userName;
    @Size(max = 14,message = "Username or Password is invalid")
    @NotEmpty(message = "password can not be empy")
    private String password;
}
