package com.example.kanbanbackend.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class TaskDTO {
    private Integer id;

    @JsonProperty("title")
    private String taskTitle;

    @JsonProperty("assignees")
    private String taskAssignees;

    @JsonProperty("status")
    private String taskStatus;
}
