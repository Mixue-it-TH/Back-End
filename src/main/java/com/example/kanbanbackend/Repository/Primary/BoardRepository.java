package com.example.kanbanbackend.Repository.Primary;

import com.example.kanbanbackend.Entitites.Primary.Board;
import com.example.kanbanbackend.Entitites.Primary.CollaboratorKey;
import com.example.kanbanbackend.Entitites.Primary.Collaborator;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Integer> {

    Board findBoardById(String boardId);
}

