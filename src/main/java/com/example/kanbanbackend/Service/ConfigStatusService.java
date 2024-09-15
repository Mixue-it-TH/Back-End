package com.example.kanbanbackend.Service;

import com.example.kanbanbackend.Entitites.Primary.Config;
import com.example.kanbanbackend.Exception.BadRequestException;
import com.example.kanbanbackend.Repository.Primary.ConfigRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ConfigStatusService {
    @Autowired
    ModelMapper mapper;
    @Autowired
    ListMapper listMapper;
    @Autowired
    ConfigRepository repository;

    public Config getConfigById(int id) {
        return repository.findById(id).orElseThrow(() -> new BadRequestException("No config found with id: " + id));
    }
}
