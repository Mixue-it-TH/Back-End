package com.example.kanbanbackend.DTO.TaskDTO.TaskV3;

import com.example.kanbanbackend.DTO.FilesDTO.FileDTO;
import com.example.kanbanbackend.DTO.StatusDTO.StatusListAllTaskDTO;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class TaskBoardDTO {
    private Integer id;
    @JsonProperty("title")
    private String taskTitle;
    @JsonProperty("description")
    private String taskDescription;
    @JsonProperty("assignees")
    private String taskAssignees;
    // เพิ่มมาให้เลือก
    @JsonProperty("files")
    private List<FileDTO> files;

    @JsonProperty("status")
    private StatusListAllTaskDTO taskStatus;
    private String createdOn;
    private String updatedOn;
}
