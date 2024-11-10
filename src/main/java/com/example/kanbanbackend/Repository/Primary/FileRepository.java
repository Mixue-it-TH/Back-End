package com.example.kanbanbackend.Repository.Primary;

import com.example.kanbanbackend.Entitites.Primary.File;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FileRepository extends JpaRepository<File, String> {

    List<File> findFileByTasks_IdIn(List<Integer> id);
    List<File> findByTasks_Id(Integer taskId);

    File findFileByIdAndTasks_Id(String id, Integer tasks_id);

    File findFileByTasks_IdAndName(Integer taskId, String fileName);
}
