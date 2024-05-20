package com.example.kanbanbackend.DTO.LimitFunc;

import com.example.kanbanbackend.Utils.LimitConfig;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class LimitConfigDTO {
    private boolean limitMaximumTask;

    public boolean getlimitMaximumTask() {
        return limitMaximumTask;
    }
    private Integer noOfTasks;
}
