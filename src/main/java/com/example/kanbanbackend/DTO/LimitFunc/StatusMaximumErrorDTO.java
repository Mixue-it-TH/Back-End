package com.example.kanbanbackend.DTO.LimitFunc;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class StatusMaximumErrorDTO {
    @NotNull
    private Integer id;

    @JsonProperty("name")
    @Size(max = 50)
    @NotBlank
    private String statusName;

    @JsonIgnore
    @JsonProperty("description")
    @Size(max = 200)
    @NotEmpty
    private String statusDescription;

    private boolean limitMaximumTask;

    public boolean getlimitMaximumTask() {
        return limitMaximumTask;
    }
    private Integer noOfTasks;

    private List<TaskMaximumDTO> tasks;
}
