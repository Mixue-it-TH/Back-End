package com.example.kanbanbackend.Repository.Primary;

import com.example.kanbanbackend.Entitites.Primary.Board;
import com.example.kanbanbackend.Entitites.Primary.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StatusRepository extends JpaRepository<Status,Integer> {

    List<Status> findStatusByBoard_Id(String boardId);
    Status findStatusByStatusName(String name);


    Status findStatusByBoard_IdAndId(String boardId, Integer id);

    Status findStatusByStatusNameAndBoard_Id(String name,String boardId);



}