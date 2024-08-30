package com.example.kanbanbackend.Repository.Primary;

import com.example.kanbanbackend.Entitites.Primary.BoardUser;
import com.example.kanbanbackend.Entitites.Primary.BoardUserKey;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardUserRepository extends JpaRepository<BoardUser, BoardUserKey> {
}
