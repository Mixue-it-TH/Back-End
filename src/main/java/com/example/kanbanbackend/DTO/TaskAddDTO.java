package com.example.kanbanbackend.DTO;

import com.example.kanbanbackend.Entitites.Status;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class TaskAddDTO {
    private Integer id;

    @JsonProperty("title")
    @NotBlank(message = "Your title can't not be null")
    @Size(max = 100)
    private String taskTitle;

    @JsonProperty("description")
    @Size(max = 500)
    private String taskDescription;

    @JsonProperty("assignees")
    @Size(max = 30)
    private String taskAssignees;

    @JsonProperty("status")
    private Integer taskStatusId;
}
