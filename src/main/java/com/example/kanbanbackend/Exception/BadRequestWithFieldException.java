package com.example.kanbanbackend.Exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class BadRequestWithFieldException extends RuntimeException{

    private String field;
    public BadRequestWithFieldException(String field, String message){
        super(message);
        this.field = field;
    }
}

