package com.example.kanbanbackend.Service;

import com.example.kanbanbackend.Entitites.Primary.BoardUser;
import com.example.kanbanbackend.Repository.Primary.BoardUserRepository;
import com.example.kanbanbackend.Repository.Primary.StatusRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BoardUserService {
    @Autowired
    ModelMapper mapper;
    @Autowired
    ListMapper listMapper;
    @Autowired
    private BoardUserRepository repository;


    public List<BoardUser> getAllBoardUser(){
        return repository.findAll();
    }

}
