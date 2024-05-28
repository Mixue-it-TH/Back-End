package com.example.kanbanbackend.DTO.TaskDTO;

import com.example.kanbanbackend.DTO.StatusDTO.StatusListAllTaskDTO;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class TaskSelectedDTO {
    private Integer id;
    @JsonProperty("title")
    private String taskTitle;
    @JsonProperty("description")
    private String taskDescription;
    @JsonProperty("assignees")
    private String taskAssignees;
    @JsonProperty("status")
    private StatusListAllTaskDTO taskStatus;
    private String createdOn;
    private String updatedOn;

}