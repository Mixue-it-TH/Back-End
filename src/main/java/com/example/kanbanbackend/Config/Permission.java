package com.example.kanbanbackend.Config;

import com.example.kanbanbackend.Entitites.Primary.Board;
import com.example.kanbanbackend.Entitites.Primary.Collaborator;
import com.example.kanbanbackend.Entitites.Primary.Invitation;
import com.example.kanbanbackend.Exception.ItemNotFoundException;
import com.example.kanbanbackend.Repository.Primary.BoardRepository;
import com.example.kanbanbackend.Repository.Primary.CollaboratorRepository;
import com.example.kanbanbackend.Repository.Primary.InvitationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Permission {
    @Autowired
    CollaboratorRepository boardUserRepository;
    @Autowired
    InvitationRepository invitationRepository;
    @Autowired
    BoardRepository boardRepository;

    public boolean getPermissionOfBoard(String boardId, String oid, String request, String requestURI) {
        Collaborator boardUser = boardUserRepository.findCollaboratorByBoard_IdAndUser_Oid(boardId, oid);
        // If the user is not a collaborator of the board, return false
        if (boardUser == null) return false;

        // If the user is the owner of the board, return true
        if (boardUser.getRole().equalsIgnoreCase("owner")) return true;

        String regex = "^/v3/boards/([a-zA-Z0-9_-]{10})/collabs(?:/([a-f0-9-]{36}))?$";

        //Check accept invitation and get Invitaion
        if(requestURI.matches(regex) && requestURI.equalsIgnoreCase("GET")) return true;

        boolean isInvited = invitationRepository.findInvitationByBoard_IdAndUser_Oid(boardId,oid) != null;
        if(requestURI.matches(regex) && requestURI.equalsIgnoreCase("POST") && isInvited) return true;

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


    public boolean getPermissionOfInvitation(String boardId, String oid, String request, String requestURI) {
        // handle 404 if not handle it will throw 403 in authFilter
        Board board = boardRepository.findBoardById(boardId);
        if (board == null)throw new ItemNotFoundException("Board id '"+boardId+"' not found ");

        Invitation listInviations = invitationRepository.findInvitationByBoard_IdAndUser_Oid(boardId, oid);
        String regexInvitation = "^/v3/boards/([a-zA-Z0-9_-]{10})/collabs/invitations(?:/([a-f0-9-]{36}))?$";
        if(listInviations == null) return false;
        if(requestURI.matches(regexInvitation) && (request.equalsIgnoreCase("GET") || request.equalsIgnoreCase("DELETE"))) {
            return true;
        }else return false;
    }

    public boolean getNewPermissionCollab(String boardId, String oid, String request, String requestURI) {
        String regex = "/v3/boards/[a-zA-Z0-9_-]{10}/collabs";
        boolean isInvited = invitationRepository.findInvitationByBoard_IdAndUser_Oid(boardId,oid) != null;
        if(requestURI.matches(regex) && request.equalsIgnoreCase("GET") && isInvited) return true;
        if(requestURI.matches(regex) && request.equalsIgnoreCase("POST") && isInvited) return true;

        return false;
    }


}
