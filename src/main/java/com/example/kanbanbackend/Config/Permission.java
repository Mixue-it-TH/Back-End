package com.example.kanbanbackend.Config;

import com.example.kanbanbackend.Entitites.Primary.Collaborator;
import com.example.kanbanbackend.Repository.Primary.CollaboratorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Permission {
    @Autowired
    CollaboratorRepository boardUserRepository;

    public boolean getPermissionOfBoard(String boardId, String oid, String request, String requestURI) {
        Collaborator boardUser = boardUserRepository.findCollaboratorByBoard_IdAndUser_Oid(boardId, oid);
        // If the user is not a collaborator of the board, return false
        if (boardUser == null) return false;

        // If the user is the owner of the board, return true
        if (boardUser.getRole().equalsIgnoreCase("owner")) return true;

        String regex = "^/v3/boards/([a-zA-Z0-9_-]{10})/collabs(?:/([a-f0-9-]{36}))?$";

        // If the user is a collaborator of the board, check the access rights
        if (boardUser.getRole().equalsIgnoreCase("collab") && request.equalsIgnoreCase("DELETE") && requestURI.matches(regex)) return true;

        // If the user is a collaborator of the board and has write access, return true
        if (boardUser.getRole().equalsIgnoreCase("collab") && boardUser.getAccess_right().equalsIgnoreCase("write") ) {
            if(requestURI.matches(regex) && (request.equalsIgnoreCase("POST")) || request.equalsIgnoreCase("PATCH")) return false;
            return true;
        } else if (boardUser.getRole().equalsIgnoreCase("collab") && boardUser.getAccess_right().equalsIgnoreCase("read") && request.equalsIgnoreCase("GET")) {
            return true;
        }
        return false;
    }
}
