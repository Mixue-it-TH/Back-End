package com.example.kanbanbackend.Exception;

import lombok.Data;

@Data
public class ErrorResponseField {
    private String timestamp;
    private int status;
    private String error;
    private String field;
    private String message;
    private String path;

    public ErrorResponseField(String timestamp, int status, String error,String field, String message, String path) {
        this.timestamp = timestamp;
        this.status = status;
        this.error = error;
        this.field = field;
        this.message = message;
        this.path = path;
    }
}
