package com.example.kanbanbackend.DTO.LimitFunc;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class StatusTasksNumDTO {
    private Integer id;

    @JsonProperty("name")
    @Size(max = 50)
    @NotBlank
    private String statusName;

    @JsonProperty("description")
    @Size(max = 200)
    private String statusDescription;

    @JsonProperty("statusColor")
    private String statusColor;

    private Integer numOfTasks;
}
