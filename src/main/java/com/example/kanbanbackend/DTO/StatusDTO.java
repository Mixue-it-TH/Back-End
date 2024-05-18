package com.example.kanbanbackend.DTO;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.validator.constraints.UniqueElements;

@Data
public class StatusDTO {
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