package com.example.kanbanbackend.DTO.LimitFunc;

import com.example.kanbanbackend.Entitites.Status;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class TaskMaximumDTO {
    private Integer id;

    @NotBlank
    @Size(max = 100)
    @JsonProperty("title")
    private String taskTitle;

    @JsonProperty("assignees")
    @Size(max = 30)
    private String taskAssignees;

    @JsonProperty("status")

    private String taskStatusStatusName;
}
