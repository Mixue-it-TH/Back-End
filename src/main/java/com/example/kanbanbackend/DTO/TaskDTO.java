package com.example.kanbanbackend.DTO;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class TaskDTO {
    private Integer id;

    @NotBlank
    @Size(max = 100)
    @JsonProperty("title")
    private String taskTitle;

    @Size(max = 100)
    @JsonProperty("description")
    private String taskDescription;

    @JsonProperty("assignees")
    @Size(max = 30)
    private String taskAssignees;

    @JsonProperty("status")
    private StatusListAllTaskDTO taskStatus;


}
