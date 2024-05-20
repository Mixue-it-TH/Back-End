package com.example.kanbanbackend.Exception;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;


@Data

public class ErrorResponse {
    private String timestamp;
    private int status;
    private String error;
    private String message;
    private String path;

    private Map<String, Object> additionalFields = new HashMap<>();

    public void addField(String key, Object value) {
        this.additionalFields.put(key, value);
    }

    public ErrorResponse(String timestamp, int status, String error, String message, String path) {
        this.timestamp = timestamp;
        this.status = status;
        this.error = error;
        this.message = message;
        this.path = path;
    }




}

