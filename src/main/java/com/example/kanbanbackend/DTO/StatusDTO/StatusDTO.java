package com.example.kanbanbackend.DTO.StatusDTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class StatusDTO {
    private Integer id;

    @JsonProperty("name")
    @Size(max = 50)
    @NotBlank(message = "must not be null")
    private String statusName;

    @JsonProperty("description")
    @Size(max = 200)
    private String statusDescription;

    @JsonProperty("statusColor")
    @Size(max = 7)
    private String statusColor;


}
