package com.example.kanbanbackend.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class BoardDTO {
    @JsonProperty("id")
    private Integer boardId;
    @JsonProperty("name")
    private String boardName;
    @JsonProperty("owner")
    private OwnerDTO user;
}
