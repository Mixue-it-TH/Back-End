package com.example.kanbanbackend.DTO;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;
@Data
public class StatusListTaskUsedDTO {
    private Integer id;

    @JsonProperty("name")
    private String statusName;

    private boolean limitMaximumTask;

    private Integer noOfTasks;

    private List<TaskDTO> tasks;

}
