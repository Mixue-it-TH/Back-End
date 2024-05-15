package com.example.kanbanbackend.Repository;

import com.example.kanbanbackend.DTO.StatusListTaskUsedDTO;
import com.example.kanbanbackend.Entitites.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface StatusRepository extends JpaRepository<Status,Integer> {

//    @Query("SELECT (s.statusName, COUNT(t.id)) " +
//            "FROM Status s  JOIN Task t on t.taskStatus.id = s.id " +
//            "WHERE s.id = :id " +
//            "GROUP BY s.statusName")
//    Status findStatusWithTaskCount(Integer id);
}
