package com.example.kanbanbackend.Service;

import com.example.kanbanbackend.DTO.BoardDTO;
import com.example.kanbanbackend.Entitites.Primary.*;
import com.example.kanbanbackend.Exception.ItemNotFoundException;
import com.example.kanbanbackend.Repository.Primary.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BoardUserService {
    @Autowired
    ModelMapper mapper;
    @Autowired
    ListMapper listMapper;
    @Autowired
    private BoardUserRepository repository;
    @Autowired
    private StatusRepository statusRepository;
    @Autowired
    ConfigRepository configRepository;




    public List<BoardUser> getAllBoardUser(){
        return repository.findAll();
    }

    public List<BoardDTO> getBoardUserByUserOid(String  oid){
            List<BoardUser> boardUserList = repository.findBoardUserByUser_Oid(oid);
            return listMapper.mapList(boardUserList, BoardDTO.class);
    }


}
