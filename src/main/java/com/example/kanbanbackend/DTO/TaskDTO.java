package com.example.kanbanbackend.DTO;

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
    private StatusListAllTaskDTO taskStatus;


}
