package com.example.kanbanbackend.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class BoardDTO {
    @JsonProperty("id")
    private String boardId;

    @JsonProperty("name")
    @Size(max = 120,min = 1)
    private String boardName;

    @JsonProperty("owner")
    private OwnerDTO user;
}
