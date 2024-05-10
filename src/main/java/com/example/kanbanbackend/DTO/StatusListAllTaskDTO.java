package com.example.kanbanbackend.DTO;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class StatusListAllTaskDTO {
    private Integer id;

    @JsonProperty("name")
    private String statusName;

    @JsonIgnore
    @JsonProperty("description")
    private String statusDescription;

    @JsonProperty("statusColor")
    private String statusColor;
}
