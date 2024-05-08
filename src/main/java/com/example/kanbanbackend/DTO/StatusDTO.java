package com.example.kanbanbackend.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class StatusDTO {
    private Integer id;

    @JsonProperty("name")
    private String statusName;

    @JsonProperty("description")
    private String statusDescription;
}
