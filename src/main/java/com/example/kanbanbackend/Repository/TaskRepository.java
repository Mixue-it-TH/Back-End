package com.example.kanbanbackend.Repository;


import com.example.kanbanbackend.DTO.StatusListTaskUsedDTO;
import com.example.kanbanbackend.Entitites.Status;
import com.example.kanbanbackend.Entitites.Task;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task,Integer> {

    List<Task> findByTaskStatus(Status taskStatus);
//    @Query("SELECT t FROM Task t JOIN t.taskStatus s ORDER BY s.statusName ASC")
//    List<Task> findAllTasksSortedByStatusName();
@Query("SELECT t FROM Task t JOIN t.taskStatus s WHERE s.statusName IN :statusNames ORDER BY s.statusName ASC")
List<Task> findAllByStatusNamesSorted(@Param("statusNames") String statusNames);
}
