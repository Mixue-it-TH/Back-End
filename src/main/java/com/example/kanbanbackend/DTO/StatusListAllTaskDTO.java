package com.example.kanbanbackend.DTO;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.validator.constraints.UniqueElements;

@Data
public class StatusListAllTaskDTO {
    @NotBlank
    private Integer id;

    @JsonProperty("name")
    @Size(max = 50)
    @NotBlank
    @UniqueElements
    private String statusName;

    @JsonIgnore
    @JsonProperty("description")
    @Size(max = 50)
    private String statusDescription;

    @JsonProperty("statusColor")
    @NotBlank
    private String statusColor;
}
