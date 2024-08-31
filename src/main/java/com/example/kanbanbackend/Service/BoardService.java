package com.example.kanbanbackend.Service;

import com.example.kanbanbackend.DTO.BoardDTO;
import com.example.kanbanbackend.Entitites.Primary.Board;
import com.example.kanbanbackend.Entitites.Primary.Config;
import com.example.kanbanbackend.Entitites.Primary.DefaultStatus;
import com.example.kanbanbackend.Entitites.Primary.Status;
import com.example.kanbanbackend.Repository.Primary.*;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BoardService {
    @Autowired
    ModelMapper mapper;
    @Autowired
    ListMapper listMapper;
    @Autowired
    private BoardRepository repository;
    @Autowired
    private DefaultStatusRepository defaultStatusRepository;
    @Autowired
    private StatusRepository statusRepository;
    @Autowired
    ConfigRepository configRepository;


    @Transactional
    public BoardDTO createBoardUser(String name){
        Config config = configRepository.findTopByOrderByIdDesc();
        Board newBoard = new Board();
        newBoard.setBoardName(name);
        newBoard.setConfigId(config);
        newBoard =  repository.save(newBoard);
        List<DefaultStatus> defaultStatusList = defaultStatusRepository.findByIdBetween(1,4);
        for (DefaultStatus defaultStatus : defaultStatusList) {
            Status customStatus = new Status();
            customStatus.setStatusName(defaultStatus.getStatusName());
            customStatus.setStatusDescription(defaultStatus.getStatusDescription());
            customStatus.setStatusColor(defaultStatus.getStatusColor());
            customStatus.setCreatedOn(defaultStatus.getCreatedOn());
            customStatus.setUpdatedOn(defaultStatus.getUpdatedOn());
            customStatus.setBoardId(newBoard);// Associate CustomStatus with the new Board
            statusRepository.save(customStatus);
        }
        System.out.println(newBoard);
        return mapper.map(newBoard, BoardDTO.class);
    }
}
