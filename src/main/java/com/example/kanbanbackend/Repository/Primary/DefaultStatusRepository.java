package com.example.kanbanbackend.Repository.Primary;

import com.example.kanbanbackend.Entitites.Primary.Board;
import com.example.kanbanbackend.Entitites.Primary.DefaultStatus;
import com.example.kanbanbackend.Entitites.Primary.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DefaultStatusRepository extends JpaRepository<DefaultStatus, Integer> {
    List<DefaultStatus> findByIdBetween(Integer minId, Integer maxId);
}
