package com.example.kanbanbackend.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;


@Data
public class VisibilityDTO {
    @NotBlank(message = "visibility must not be null")
    private String visibility;
}
