package com.example.kanbanbackend.DTO;

import com.example.kanbanbackend.Entitites.Primary.Board;
import com.example.kanbanbackend.Entitites.Primary.Visibility;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BoardDTO {
    @JsonProperty("id")
    private String boardId;
    @JsonProperty("name")
    @Size(max = 120,min = 1)
    private String boardName;
    @JsonProperty("visibility")
    private Visibility boardVisibility;
    @JsonProperty("owner")
    private OwnerDTO user;

}
