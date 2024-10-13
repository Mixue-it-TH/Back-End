package com.example.kanbanbackend.DTO.CollabsDTO;

import com.example.kanbanbackend.Entitites.Primary.Collaborator;
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

    public CollaboratorResponseDTO getCollaboratorInfo(Collaborator collaborator) {
        return new CollaboratorResponseDTO(
                collaborator.getId().getUserId(),
                collaborator.getBoard().getId(),
                collaborator.getUser().getUserName(),
                collaborator.getUser().getEmail(),
                collaborator.getAccess_right()
        );
    }

}


