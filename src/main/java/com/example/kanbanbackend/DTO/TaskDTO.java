package com.example.kanbanbackend.DTO;

import lombok.Data;

@Data
public class TaskDTO {
    private Integer id;
    private String taskTitle;
    private String taskAssignees;
    private String taskStatus;
}
