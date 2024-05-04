package com.example.kanbanbackend.Exception;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class delException {
    private final Timestamp timestamp;
    private final int status;
    private final  String message;
    private final String instance;
}
