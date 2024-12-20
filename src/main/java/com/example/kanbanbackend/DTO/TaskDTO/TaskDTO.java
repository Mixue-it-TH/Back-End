package com.example.kanbanbackend.DTO.TaskDTO;

import com.example.kanbanbackend.DTO.FilesDTO.FileDTO;
import com.example.kanbanbackend.DTO.StatusDTO.StatusListAllTaskDTO;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class TaskDTO {
    private Integer id;

    @NotBlank
    @Size(max = 100)
    @JsonProperty("title")
    private String taskTitle;

    @Size(max = 500)
    @JsonProperty("description")
    private String taskDescription;

    @JsonProperty("assignees")
    @Size(max = 30)
    private String taskAssignees;

    @JsonProperty("status")
    private StatusListAllTaskDTO taskStatus;
    private List<FileDTO> newFiles;
    private List<FileDTO> excessFiles;
    private List<FileDTO> deleteFiles;

}
