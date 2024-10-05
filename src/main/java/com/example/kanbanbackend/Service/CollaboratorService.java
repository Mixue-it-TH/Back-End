package com.example.kanbanbackend.Service;

import com.example.kanbanbackend.Auth.JwtTokenUtil;
import com.example.kanbanbackend.DTO.CollabsDTO.CollabBoardDTO;
import com.example.kanbanbackend.DTO.CollabsDTO.CollabDTO;
import com.example.kanbanbackend.DTO.CollabsDTO.CollabRequestDTO;
import com.example.kanbanbackend.DTO.CollabsDTO.EmailDTO;
import com.example.kanbanbackend.DTO.PersonalBoardDTO;
import com.example.kanbanbackend.Entitites.Primary.Board;
import com.example.kanbanbackend.Entitites.Primary.Collaborator;
import com.example.kanbanbackend.Entitites.Share.User;
import com.example.kanbanbackend.Exception.ConflictException;
import com.example.kanbanbackend.Exception.ForBiddenException;
import com.example.kanbanbackend.Exception.ItemNotFoundException;
import com.example.kanbanbackend.Repository.Primary.BoardRepository;
import com.example.kanbanbackend.Repository.Primary.CollaboratorRepository;
import com.example.kanbanbackend.Repository.Primary.PrimaryUserRepository;
import com.example.kanbanbackend.Repository.Share.UserRepository;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CollaboratorService {

    @Autowired
    ListMapper listMapper;
    @Autowired
    ModelMapper mapper;

    @Autowired
    private CollaboratorRepository repository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PrimaryUserRepository primaryUserRepository;

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;


    public List<Collaborator> getAllCollaborator() {
        return repository.findAll();
    }

    public Map<String, Object> getPersonalAndColloboratorByUser_Oid(String oid) {
        List<Collaborator> boardOwnerList = repository.findCollaboratorByUser_OidAndRole(oid, "OWNER");
        List<PersonalBoardDTO> personalBoardDTOs = listMapper.mapList(boardOwnerList, PersonalBoardDTO.class);

        List<Collaborator> boardCollabList = repository.findCollaboratorByUser_OidAndRole(oid, "COLLAB");
        List<CollabBoardDTO> collabBoardDTOs = listMapper.mapList(boardCollabList, CollabBoardDTO.class);


        Map<String, Object> result = new HashMap<>();
        result.put("owners", personalBoardDTOs);
        result.put("collabs", collabBoardDTOs);

        return result;
    }

    public CollabDTO getCollabByCollabId(String boardId, String collabId) {
        Collaborator collaborator = repository.findCollaboratorByBoard_IdAndRoleAndUser_Oid(boardId, "COLLAB", collabId);
        if (collaborator == null) {
            throw new ItemNotFoundException("collabId: " + collabId + " or boardId: " + boardId + " not found");
        }
        CollabDTO collabDTO = new CollabDTO();
        collabDTO.setOid(collaborator.getUser().getOid());
        collabDTO.setUserName(collaborator.getUser().getUserName());
        collabDTO.setEmail(collaborator.getUser().getEmail());
        collabDTO.setAccess_right(collaborator.getAccess_right());
        collabDTO.setAddedOn(collaborator.getAddedOn());
        return collabDTO;
    }

    public Collaborator addCollab(String boardId, CollabRequestDTO collab, HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7).trim();
        Claims claims = jwtTokenUtil.getAllClaimsFromToken(token);

        // CHECK token is owner of board 403
        Collaborator owner = repository.findCollaboratorByBoard_IdAndRoleAndUser_Oid(boardId, "OWNER", claims.get("oid").toString());
        if (owner == null) {
            throw new ForBiddenException("You do not have permission to operate this resource");
        }


        // CHECK email that exist in DB 404
        String email = collab.getEmail();
        User user = userRepository.findUsersByEmail(email);
        if (user == null) {
            throw new ItemNotFoundException("User not found");
        }

        // CHECK userId that exist in DB 404
        com.example.kanbanbackend.Entitites.Primary.User userPrimary = primaryUserRepository.findUsersByOid(user.getOid());
        if (userPrimary == null) {
            throw new ItemNotFoundException("User not found in Database");
        }

        // CHECK boardId that exist in DB 404
        Board board = boardRepository.findBoardById(boardId);

        // CHECK email that have a conflict 409
        List<Collaborator> collaboratorList = repository.findCollabaratorByBoard_Id(boardId);
        List<EmailDTO> emailDTOS = collaboratorList.stream()
                .map(collaborator -> {
                    EmailDTO emailDTO = new EmailDTO();
                    emailDTO.setEmail(collaborator.getUser().getEmail());
                    return emailDTO;
                })
                .collect(Collectors.toList());


        boolean emailExists = emailDTOS.stream()
                .anyMatch(emailDTO -> emailDTO.getEmail().equals(collab.getEmail()));

        if (emailExists) {
            throw new ConflictException("There are some conflicts with the email.");
        }

        // CREATE COLAB
        String username = claims.get("name").toString();
        Collaborator newCollab = new Collaborator(userPrimary, board, "COLLAB", collab.getAccess_right(), username);
        return repository.saveAndFlush(newCollab);
    }

    public Collaborator updateCollab(String boardId, String collabId, CollabRequestDTO collab, HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7).trim();
        Claims claims = jwtTokenUtil.getAllClaimsFromToken(token);

        // CHECK token is owner of board 403
        Collaborator owner = repository.findCollaboratorByBoard_IdAndRoleAndUser_Oid(boardId, "OWNER", claims.get("oid").toString());
        if (owner == null) {
            throw new ForBiddenException("You do not have permission to operate this resource");
        }


        // CHECK collab_oid is in board 404
        Collaborator oldCollaborator = repository.findCollaboratorByBoard_IdAndUser_Oid(boardId, collabId);
        if (oldCollaborator == null) {
            throw new ItemNotFoundException("collabId: " + collabId + " collaborator not found");
        }

        // CHECK boardId that exist in DB 404
        Board board = boardRepository.findBoardById(boardId);

        // CREATE COLAB
        oldCollaborator.setAccess_right(collab.getAccess_right());
        return repository.saveAndFlush(oldCollaborator);
    }

    public Collaborator deleteCollab(String boardId, String collabId, HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7).trim();
        Claims claims = jwtTokenUtil.getAllClaimsFromToken(token);
        String oid = (String) claims.get("oid");

        // CHECK collabId is request by owner of collabId and collabId is not Owner of Board 403
        Collaborator owner = repository.findCollaboratorByBoard_IdAndRoleAndUser_Oid(boardId, "OWNER", claims.get("oid").toString());

        // CASE collab try to delete other collab
        if (!oid.equalsIgnoreCase(collabId) && owner == null)
            throw new ForBiddenException("You don't have permission to delete other collaborator.");
        // CASE owner try to delete yourself
        if (oid.equalsIgnoreCase(collabId) && owner != null) throw new ForBiddenException("You can't delete owner.");

        // CHECK collab_oid is in board 404
        Collaborator oldCollaborator = repository.findCollaboratorByBoard_IdAndUser_Oid(boardId, collabId);
        if (oldCollaborator == null) {
            throw new ItemNotFoundException("collabId: " + collabId + " not collaborator");
        }
        // CHECK boardId that exist in DB 404
        Board board = boardRepository.findBoardById(boardId);

        // DELETE COLAB
        repository.delete(oldCollaborator);
        return oldCollaborator;
    }


}
