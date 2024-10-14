package com.example.kanbanbackend.DTO.CollabsDTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;

@Data
public class CollabRequestDTO {
    @NotNull(message = "Board id must not be null.")
    private String email;

    @Pattern(regexp = "WRITE|READ", message = "Access right must be either 'WRITE' or 'READ'.")
    @NotNull(message = "Access right must not be null.")
    @Value("${default.access.right:READ}")
    @JsonProperty("accessRight")
    private String access_right;
}

