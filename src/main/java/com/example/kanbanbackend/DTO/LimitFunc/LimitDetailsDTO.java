package com.example.kanbanbackend.DTO.LimitFunc;

import lombok.Data;

import java.util.List;

@Data
public class LimitDetailsDTO {
    private boolean limitMaximumTask;

    public boolean getlimitMaximumTask() {
        return limitMaximumTask;
    }
    private Integer noOfTasks;
    private List<StatusTasksNumDTO> statusList;
}
