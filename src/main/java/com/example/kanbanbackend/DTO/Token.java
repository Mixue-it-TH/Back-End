package com.example.kanbanbackend.DTO;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Token {

    private String access_token;
    private String refresh_token;

    public Token(String token, String refreshToken){
        this.access_token = token;
        this.refresh_token = refreshToken;
    }
    public Token(String token){
        this.access_token = token;
    }

}
