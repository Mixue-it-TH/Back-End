package com.example.kanbanbackend.Repository.Primary;

import com.example.kanbanbackend.Entitites.Primary.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StatusRepository extends JpaRepository<Status,Integer> {

    Status findStatusByStatusName(String name);

}