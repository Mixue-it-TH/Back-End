package com.example.kanbanbackend.Repository.Primary;

import com.example.kanbanbackend.Entitites.Primary.Board;
import com.example.kanbanbackend.Entitites.Primary.BoardUser;
import com.example.kanbanbackend.Entitites.Primary.BoardUserKey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardUserRepository extends JpaRepository<BoardUser, BoardUserKey> {
    List<BoardUser>findBoardUserByUser_Oid(String oid);

    List<BoardUser> findBoardUserByBoard_Id(String boardId);

   BoardUser findBoardUserByBoard_IdAndUser_Oid(String boardId, String userId);
}
