package com.example.kanbanbackend.DTO.CollabsDTO;

import lombok.Data;

@Data
public class CollaboratorResponseDTO {
    private String oid;
    private String boardId;
    private String collaboratorName;
    private String collaboratorEmail;
    private String accessRight;

    public CollaboratorResponseDTO(String oid, String boardId, String collaboratorName, String collaboratorEmail, String accessRight) {
        this.oid = oid;
        this.boardId = boardId;
        this.collaboratorName = collaboratorName;
        this.collaboratorEmail = collaboratorEmail;
        this.accessRight = accessRight;
    }


}


