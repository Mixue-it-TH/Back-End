package com.example.kanbanbackend.DTO.LimitFunc;

import lombok.Data;

@Data
public class LimitConfigDTO {
    private boolean limitMaximumTask;

    public boolean getlimitMaximumTask() {
        return limitMaximumTask;
    }
    private Integer noOfTasks;
}
