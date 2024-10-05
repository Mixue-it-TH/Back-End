package com.example.kanbanbackend.Config;

import com.example.kanbanbackend.Entitites.Primary.Collaborator;
import com.example.kanbanbackend.Repository.Primary.CollaboratorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Permission {
    @Autowired
    CollaboratorRepository boardUserRepository;

    public boolean getPermissionOfBoard(String boardId, String oid, String request) {
        Collaborator boardUser = boardUserRepository.findCollaboratorByBoard_IdAndUser_Oid(boardId, oid);
        if (boardUser == null) return false;
        if (boardUser.getRole().equalsIgnoreCase("owner")) return true;

        if (boardUser.getRole().equalsIgnoreCase("collab") && request.equalsIgnoreCase("DELETE")) return true;
        if (boardUser.getRole().equalsIgnoreCase("collab") && boardUser.getAccess_right().equalsIgnoreCase("write")) {
            return true;
        } else if (boardUser.getRole().equalsIgnoreCase("collab") && boardUser.getAccess_right().equalsIgnoreCase("read") && request.equalsIgnoreCase("GET")) {
            return true;
        }
        return false;
    }
}
