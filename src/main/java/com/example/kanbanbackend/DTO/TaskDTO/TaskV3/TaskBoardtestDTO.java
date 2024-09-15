package com.example.kanbanbackend.DTO.TaskDTO.TaskV3;


import com.example.kanbanbackend.DTO.StatusDTO.StatusListAllTaskDTO;
import com.example.kanbanbackend.DTO.StatusDTO.StatusSelectedDTO;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class TaskBoardtestDTO {
    private Integer id;
    @JsonProperty("title")
    private String taskTitle;
    @JsonProperty("description")
    private String taskDescription;
    @JsonProperty("assignees")
    private String taskAssignees;

    @JsonProperty("status")
    private StatusSelectedDTO taskStatus;
    private String createdOn;
    private String updatedOn;
}
