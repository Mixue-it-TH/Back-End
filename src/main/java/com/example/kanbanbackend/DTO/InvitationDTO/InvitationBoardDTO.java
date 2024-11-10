package com.example.kanbanbackend.DTO.InvitationDTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class InvitationBoardDTO {

    private String status;
    @JsonProperty("id")
    private String boardId;
    @JsonProperty("name")
    private String boardName;
    @JsonProperty("accessRight")
    private String access_right;
    @JsonProperty("owner")
    private String inviterName;
}
