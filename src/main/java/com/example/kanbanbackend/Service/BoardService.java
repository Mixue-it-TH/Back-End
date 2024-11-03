package com.example.kanbanbackend.Service;

import com.example.kanbanbackend.Auth.JwtTokenUtil;
import com.example.kanbanbackend.Config.Permission;
import com.example.kanbanbackend.Config.VisibilityConfig;
import com.example.kanbanbackend.DTO.CollabsDTO.CollabDTO;
import com.example.kanbanbackend.DTO.InvitationDTO.InvitationDTO;
import com.example.kanbanbackend.DTO.PersonalBoardDTO;
import com.example.kanbanbackend.DTO.VisibilityDTO;
import com.example.kanbanbackend.Entitites.Primary.*;
import com.example.kanbanbackend.Exception.BadRequestException;
import com.example.kanbanbackend.Exception.ItemNotFoundException;
import com.example.kanbanbackend.Repository.Primary.*;
import io.jsonwebtoken.Claims;
import io.viascom.nanoid.NanoId;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BoardService {
    @Autowired
    ModelMapper mapper;
    @Autowired
    ListMapper listMapper;
    @Autowired
    ConfigRepository configRepository;
    @Autowired
    PrimaryUserRepository primaryUserRepository;
    @Autowired
    private BoardRepository boardRepository;
    @Autowired
    private CollaboratorRepository boardUserRepository;
    @Autowired
    private DefaultStatusRepository defaultStatusRepository;
    @Autowired
    private StatusRepository statusRepository;
    @Autowired
    private InvitationRepository invitationRepository;

    @Autowired
    private JwtTokenUtil tokenUtil;
    @Autowired
    private Permission permission;
    @Autowired
    private VisibilityConfig visibilityConfig;

    @Transactional
    public PersonalBoardDTO createBoardUser(PersonalBoardDTO boardDTO, HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7).trim();
        ;

        Claims claims = tokenUtil.getAllClaimsFromToken(token);

        Config config = configRepository.saveAndFlush(new Config());

        String oid = claims.get("oid", String.class);
        String email = claims.get("email", String.class);
        String name = claims.get("name", String.class);
        String role = "OWNER";
        User user = primaryUserRepository.saveAndFlush(new User(oid, name, email));


        Board newBoard = boardRepository.saveAndFlush(new Board(NanoId.generate(10), boardDTO.getBoardName(), config, Visibility.PRIVATE));

        Collaborator boardUser = boardUserRepository.saveAndFlush(new Collaborator(user, newBoard, role, "WRITE", name));

        List<DefaultStatus> defaultStatusList = defaultStatusRepository.findByIdBetween(1, 4);
        for (DefaultStatus defaultStatus : defaultStatusList) {
            Status customStatus = new Status();
            customStatus.setStatusName(defaultStatus.getStatusName());
            customStatus.setStatusDescription(defaultStatus.getStatusDescription());
            customStatus.setStatusColor(defaultStatus.getStatusColor());
            customStatus.setCreatedOn(Timestamp.valueOf(defaultStatus.getCreatedOn().toLocalDateTime()));
            customStatus.setUpdatedOn(Timestamp.valueOf(defaultStatus.getUpdatedOn().toLocalDateTime()));
            customStatus.setBoard(newBoard);// Associate CustomStatus with the new Board
            statusRepository.save(customStatus);
        }


        return mapper.map(boardUser, PersonalBoardDTO.class);
    }

    public PersonalBoardDTO getBoardUserByBoardId(String boardId, HttpServletRequest request) {
        List<Collaborator> boardUserList = boardUserRepository.findCollaboratorByBoard_IdAndRole(boardId, "OWNER");
        if (boardUserList.isEmpty()) {
            throw new ItemNotFoundException("Board id '" + boardId + "' not found");
        }
        return mapper.map(boardUserList.get(0), PersonalBoardDTO.class);


    }

    public VisibilityDTO setVisibility(String boardId, VisibilityDTO visibilityDTO, HttpServletRequest request) {
        Board board = boardRepository.findBoardById(boardId);
        if (board == null) {
            throw new ItemNotFoundException("Board id '" + boardId + "' not found");
        }

        try {
            // แปลง String เป็น Enum ถ้าค่าไม่ถูกต้องจะเกิด IllegalArgumentException
            Visibility visibility = Visibility.valueOf(visibilityDTO.getVisibility().toUpperCase());
            board.setVisibility(visibility);
            boardRepository.saveAndFlush(board);
            VisibilityDTO visibilityDTO2 = new VisibilityDTO();
            visibilityDTO2.setVisibility(visibility.toString().toUpperCase());
            return visibilityDTO2;
        } catch (IllegalArgumentException e) {
            // Handle ค่า visibility ที่ไม่ถูกต้อง และโยน BadRequestException (400)
            throw new BadRequestException("Invalid visibility value: " + visibilityDTO.getVisibility());
        }

    }

    public Map<String, Object> getAllCollabsByBoardId(String boardId) {
        List<Collaborator> collaborators = boardUserRepository.findCollaboratorByBoard_IdAndRole(boardId, "COLLAB");

        // CHECK 404
        if (collaborators == null) {
            throw new ItemNotFoundException("Board Id: " + boardId + " not found or COLLAB not found");
        }
        List<CollabDTO> collabDTOList = new ArrayList<>();
        for (Collaborator collaborator : collaborators) {
            CollabDTO collabDTO = new CollabDTO();
            collabDTO.setOid(collaborator.getUser().getOid());
            collabDTO.setUserName(collaborator.getUser().getUserName());
            collabDTO.setEmail(collaborator.getUser().getEmail());
            collabDTO.setAccess_right(collaborator.getAccess_right());
            collabDTO.setAddedOn(collaborator.getAddedOn());
            collabDTOList.add(collabDTO);
        }

        List<Invitation> invitations = invitationRepository.findInvitationByBoard_Id(boardId);
        List<InvitationDTO> invitationDTOS = new ArrayList<>();
        for (Invitation invitation : invitations) {
            InvitationDTO invitationDTO = new InvitationDTO();
            invitationDTO.setOid(invitation.getUser().getOid());
            invitationDTO.setUserName(invitation.getUser().getUserName());
            invitationDTO.setEmail(invitation.getUser().getEmail());
            invitationDTO.setAccess_right(invitation.getAccess_right());
            invitationDTO.setStatus(invitation.getStatus());
            invitationDTOS.add(invitationDTO);

        }

        Map<String, Object> response = new HashMap<>();
        response.put("collaborators", collabDTOList);
        response.put("invitations", invitationDTOS);

        return response;
    }
}
