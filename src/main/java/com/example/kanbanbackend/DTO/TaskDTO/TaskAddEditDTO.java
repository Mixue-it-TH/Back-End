package com.example.kanbanbackend.DTO.TaskDTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class TaskAddEditDTO {
    private Integer id;

    @JsonProperty("title")
    @NotBlank(message = "title must not be null")
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

    private String boardId;
}
