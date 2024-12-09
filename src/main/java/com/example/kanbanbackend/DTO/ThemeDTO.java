package com.example.kanbanbackend.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ThemeDTO {
    @NotBlank(message = "theme must not be null")
    private String theme;
}
