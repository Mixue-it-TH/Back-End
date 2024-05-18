package com.example.kanbanbackend.DTO;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class StatusSelectedDTO {
    private Integer id;

    @JsonProperty("name")
    private String statusName;

    @JsonProperty("description")
    private String statusDescription;

    @JsonProperty("statusColor")
    private String statusColor;

    private String createdOn;
    private String updatedOn;
}
