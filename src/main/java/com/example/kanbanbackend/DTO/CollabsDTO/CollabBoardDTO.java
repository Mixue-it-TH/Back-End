package com.example.kanbanbackend.DTO.CollabsDTO;

import com.example.kanbanbackend.DTO.OwnerDTO;
import com.example.kanbanbackend.Entitites.Primary.Visibility;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CollabBoardDTO {

    @JsonProperty("id")
    private String boardId;
    @JsonProperty("name")
    @Size(max = 120,min = 1)
    private String boardName;
    @JsonProperty("collab")
    private OwnerDTO user;

    @JsonProperty("owner")
    private String ownerName;
    @JsonProperty("accessRight")
    private String access_right;


}
