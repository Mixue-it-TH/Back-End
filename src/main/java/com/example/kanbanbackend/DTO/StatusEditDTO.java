package com.example.kanbanbackend.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class StatusEditDTO {
    @NotNull
    private Integer id;

    @JsonProperty("name")
    @Size(max = 50)
    @NotBlank
    private String statusName;

    @JsonProperty("description")
    @Size(max = 200)
    @NotEmpty
    private String statusDescription;

    @JsonProperty("statusColor")
    private String statusColor;
}
