package com.example.kanbanbackend.Repository.Primary;

import com.example.kanbanbackend.Entitites.Primary.Board;
import com.example.kanbanbackend.Entitites.Primary.Invitation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InvitationRepository extends JpaRepository<Invitation, Integer> {

    List<Invitation> findInvitationByBoard_Id(String boardId);

    List<Invitation> findInvitationByUserOid(String oid);

    Invitation findInvitationByBoard_IdAndId(String boardId, Integer id);
    Invitation findInvitationByBoard_IdAndUser_Oid(String boardId, String oid);
}
