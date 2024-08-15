package com.example.kanbanbackend.Exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UnauthorizedException extends RuntimeException{

    public UnauthorizedException(String message) {super(message);}
}


