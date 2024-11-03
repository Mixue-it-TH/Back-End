package com.example.kanbanbackend.DTO.InvitationDTO;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data

public class InvitationResDTO {
    private String oid;
    @JsonProperty("accessRight")
    private String access_right;
    private String inviterName;
    private String boardName;
}
