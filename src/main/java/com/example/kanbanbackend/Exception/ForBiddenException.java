package com.example.kanbanbackend.Exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FORBIDDEN)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ForBiddenException extends RuntimeException {
    public ForBiddenException(String message) {
        super(message);
    }
}
