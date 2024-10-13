package com.example.kanbanbackend.DTO.CollabsDTO;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;

@Data
public class AccessDTO {
    @Pattern(regexp = "WRITE|READ", message = "Access right must be either 'WRITE' or 'READ'.")
    @NotNull(message = "Access right must not be null.")
    @Value("${default.access.right:READ}")
    private String access_right;
}
