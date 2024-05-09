package com.example.kanbanbackend.Repository;


import com.example.kanbanbackend.Entitites.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task,Integer> {
}
