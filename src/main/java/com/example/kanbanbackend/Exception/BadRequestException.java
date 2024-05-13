package com.example.kanbanbackend.Exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BadRequestException extends RuntimeException{
    public BadRequestException(String message){
        super(message);
    }
}
