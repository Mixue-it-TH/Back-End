package com.example.kanbanbackend.DTO;

import com.example.kanbanbackend.Entitites.Status;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class TaskAddEditDTO {
    private Integer id;

    @JsonProperty("title")
    private String taskTitle;

    @JsonProperty("description")
    private String taskDescription;

    @JsonProperty("assignees")
    private String taskAssignees;

    @JsonProperty("status")
    private Status taskStatusId;

}
