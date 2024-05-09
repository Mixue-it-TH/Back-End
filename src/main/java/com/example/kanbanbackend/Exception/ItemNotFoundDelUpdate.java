package com.example.kanbanbackend.Exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@JsonInclude(JsonInclude.Include.NON_NULL)
@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ItemNotFoundDelUpdate extends RuntimeException{
    public ItemNotFoundDelUpdate(String message){
        super(message);
    }
}