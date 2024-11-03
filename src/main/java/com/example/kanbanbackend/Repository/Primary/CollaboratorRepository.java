package com.example.kanbanbackend.Repository.Primary;

import com.example.kanbanbackend.Entitites.Primary.Collaborator;
import com.example.kanbanbackend.Entitites.Primary.CollaboratorKey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CollaboratorRepository extends JpaRepository<Collaborator, CollaboratorKey> {
    List<Collaborator>findBoardUserByUser_Oid(String oid);

    List<Collaborator>findCollaboratorByUser_OidAndRole(String oid, String role);
    List<Collaborator> findCollaboratorByBoard_IdAndRole(String boardId, String role);

    Collaborator findCollaboratorByBoard_IdAndRoleAndUser_Oid(String boardId, String role, String userId);




    List<Collaborator> findCollabaratorByBoard_Id(String boardId);

    Collaborator findCollaboratorByBoard_IdAndUser_Oid(String boardId, String userId);
}
