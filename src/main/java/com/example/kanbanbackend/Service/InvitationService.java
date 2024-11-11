package com.example.kanbanbackend.Service;


import com.example.kanbanbackend.DTO.CollabsDTO.AccessDTO;
import com.example.kanbanbackend.DTO.CollabsDTO.CollabRequestDTO;
import com.example.kanbanbackend.DTO.CollabsDTO.EmailDTO;
import com.example.kanbanbackend.DTO.InvitationDTO.InvitationResDTO;
import com.example.kanbanbackend.DTO.InvitationDTO.InvitationResponseDTO;
import com.example.kanbanbackend.Email.EmailService;
import com.example.kanbanbackend.Entitites.Primary.Board;
import com.example.kanbanbackend.Entitites.Primary.Collaborator;
import com.example.kanbanbackend.Entitites.Primary.Invitation;
import com.example.kanbanbackend.Entitites.Share.User;
import com.example.kanbanbackend.Exception.ConflictException;
import com.example.kanbanbackend.Exception.ForBiddenException;
import com.example.kanbanbackend.Exception.ItemNotFoundException;
import com.example.kanbanbackend.Repository.Primary.BoardRepository;
import com.example.kanbanbackend.Repository.Primary.CollaboratorRepository;
import com.example.kanbanbackend.Repository.Primary.InvitationRepository;
import com.example.kanbanbackend.Repository.Primary.PrimaryUserRepository;
import com.example.kanbanbackend.Repository.Share.UserRepository;
import com.example.kanbanbackend.Util.ClaimsUtil;
import io.jsonwebtoken.Claims;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service

public class InvitationService {

    @Autowired
    private InvitationRepository invitationRepository;

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private CollaboratorRepository collaboratorRepository;

    @Autowired
    private PrimaryUserRepository primaryUserRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private ClaimsUtil claimsUtil;

    public InvitationResDTO getUserInvitedByBoardId(String boardId, HttpServletRequest request) {
        Claims claims = claimsUtil.getClaims(request);
        String oid = (String) claims.get("oid");

        Invitation invitation = invitationRepository.findInvitationByBoard_IdAndUser_Oid(boardId, oid);

        // 404 IF NOT FOUND
        if (invitation == null) {
            throw new ItemNotFoundException("Board id: " + boardId + " or User not invited");
        }

        InvitationResDTO invitationResDTO = new InvitationResDTO();
        invitationResDTO.setOid(invitation.getUser().getOid());
        invitationResDTO.setAccess_right(invitation.getAccess_right());
        invitationResDTO.setInviterName(invitation.getInviterName());
        invitationResDTO.setBoardName(invitation.getBoard().getBoardName());

        return invitationResDTO;
    }

    public InvitationResponseDTO createInvitation(String boardId, CollabRequestDTO collabRequestDTO, HttpServletRequest request,String origin) throws MessagingException, UnsupportedEncodingException {
        Claims claims = claimsUtil.getClaims(request);
        String oid = (String) claims.get("oid");

        // CHECK THAT BOARD NOT EXIST 404
        Board board = boardRepository.findBoardById(boardId);
        if (board == null) {
            throw new ItemNotFoundException("Board id: " + boardId + " not found");
        }

        // CHECK token is owner of board 403
        Collaborator owner = collaboratorRepository.findCollaboratorByBoard_IdAndRoleAndUser_Oid(boardId, "OWNER", claims.get("oid").toString());

        if (owner == null) {
            throw new ForBiddenException("You can't add yourself to collaborator");
        }

        // CHECK email that exist in DB 404
        String email = collabRequestDTO.getEmail();
        User user = userRepository.findUsersByEmail(email);
        if (user == null) {
            throw new ItemNotFoundException("User not found");
        }



        // CHECK userId that exist in DB 404
        com.example.kanbanbackend.Entitites.Primary.User userPrimary = primaryUserRepository.findUsersByOid(user.getOid());
        if (userPrimary == null) {
            userPrimary = new com.example.kanbanbackend.Entitites.Primary.User(user.getOid(), user.getName(), user.getEmail());
            primaryUserRepository.save(userPrimary);
        }

        // CHECK email that have a conflict 409
        List<Collaborator> collaboratorList = collaboratorRepository.findCollabaratorByBoard_Id(boardId);
        List<EmailDTO> emailDTOS = collaboratorList.stream()
                .map(collaborator -> {
                    EmailDTO emailDTO = new EmailDTO();
                    emailDTO.setEmail(collaborator.getUser().getEmail());
                    return emailDTO;
                })
                .collect(Collectors.toList());


        boolean emailExists = emailDTOS.stream()
                .anyMatch(emailDTO -> emailDTO.getEmail().equals(collabRequestDTO.getEmail()));

        Invitation invitation = invitationRepository.findInvitationByBoard_IdAndUser_Oid(boardId, user.getOid());

        if (invitation != null) {
            throw new ConflictException("There are some conflicts with the email.");
        }

        if (emailExists) {
            throw new ConflictException("There are some conflicts with the email.");
        }

        // CREATE COLAB
        String username = claims.get("name").toString();
        Invitation newCollab = new Invitation(collabRequestDTO.getAccess_right(), "PENDING", username, userPrimary, board);
        Invitation savedCollab = invitationRepository.saveAndFlush(newCollab);

        String url = origin+"/board/"+ boardId + "/collab/invitations";

    emailService.sendInvitationEmail("golfpopmei14@gmail.com", username, board.getBoardName(), collabRequestDTO.getAccess_right(),url);

        return new InvitationResponseDTO(
                savedCollab.getUser().getOid(),
                savedCollab.getStatus(),
                savedCollab.getBoard().getId(),
                savedCollab.getUser().getUserName(),
                savedCollab.getUser().getEmail(),
                savedCollab.getAccess_right()
        );


    }

    public Invitation declineInvitation(String boardId, String userOid, HttpServletRequest request) {
        Claims claims = claimsUtil.getClaims(request);
        String oid = (String) claims.get("oid");

        Invitation invitation = invitationRepository.findInvitationByBoard_IdAndUser_Oid(boardId, userOid);

        // 404 IF NOT FOUND
        if (invitation == null) {
            throw new ItemNotFoundException("Board id: " + boardId + " or User not found");
        }


        invitationRepository.delete(invitation);
        return invitation;
    }

    public Map<String, String> updateInvitation(String boardId, String Useroid, AccessDTO accessRight, HttpServletRequest request) {

        Claims claims = claimsUtil.getClaims(request);
        String oid = (String) claims.get("oid");

        Invitation invitation = invitationRepository.findInvitationByBoard_IdAndUser_Oid(boardId, Useroid);

        // 404 IF NOT FOUND
        if (invitation == null) {
            throw new ItemNotFoundException("Board id: " + boardId + " or User not found");
        }

        invitation.setAccess_right(accessRight.getAccessRight());
        invitationRepository.saveAndFlush(invitation);


        Map<String, String> result = new HashMap<>();
        result.put("accessRight", invitation.getAccess_right());

        return result;
    }
}
