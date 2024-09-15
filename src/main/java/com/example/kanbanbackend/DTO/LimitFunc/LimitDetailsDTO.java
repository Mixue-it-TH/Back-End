package com.example.kanbanbackend.DTO.LimitFunc;

import lombok.Data;

import java.util.List;

@Data
public class LimitDetailsDTO {
    private Integer limitMaximumTask;

    private Integer noOfTasks;
    private List<StatusTasksNumDTO> statusList;
}
