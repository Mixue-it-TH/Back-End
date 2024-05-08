package com.example.kanbanbackend.DTO;

import com.example.kanbanbackend.Entitites.Status;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class TaskDTO {
    private Integer id;

    @JsonProperty("title")
    private String taskTitle;

    @JsonIgnore
    @JsonProperty("description")
    private String taskDescription;

    @JsonProperty("assignees")
    private String taskAssignees;

    @JsonProperty("status")
    private String taskStatusName;


}
