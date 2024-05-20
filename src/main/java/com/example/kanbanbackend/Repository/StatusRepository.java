package com.example.kanbanbackend.Repository;

import com.example.kanbanbackend.Entitites.Status;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StatusRepository extends JpaRepository<Status,Integer> {

    Status findStatusByStatusName(String name);
}