package com.example.kanbanbackend.DTO.InvitationDTO;

import lombok.Data;

@Data
public class InvitationResponseDTO {
    private String oid;
    private String status;
    private String boardId;
    private String collaboratorName;
    private String collaboratorEmail;
    private String accessRight;

    public InvitationResponseDTO(String oid, String status, String boardId, String collaboratorName, String collaboratorEmail, String accessRight) {
        this.oid = oid;
        this.status = status;
        this.boardId = boardId;
        this.collaboratorName = collaboratorName;
        this.collaboratorEmail = collaboratorEmail;
        this.accessRight = accessRight;
    }
}
