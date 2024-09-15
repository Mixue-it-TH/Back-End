package com.example.kanbanbackend.DTO;

import lombok.Data;

@Data
public class Token {

    private String access_token;

    public Token(String token){
        this.access_token = token;
    }

}
