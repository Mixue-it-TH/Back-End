package com.example.kanbanbackend.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class StatusEditDTO {
    private Integer id;

    @JsonProperty("name")
    @Size(max = 50)
    @NotBlank(message = "must not be null")
    private String statusName;

    @JsonProperty("description")
    @Size(min=1, max = 200)
    private String statusDescription;

    @JsonProperty("statusColor")
    private String statusColor;
}
