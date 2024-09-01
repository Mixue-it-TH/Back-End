package com.example.kanbanbackend.Service;

import com.example.kanbanbackend.Entitites.Primary.Board;
import com.example.kanbanbackend.Entitites.Primary.Config;
import com.example.kanbanbackend.Entitites.Primary.Task;
import com.example.kanbanbackend.Exception.BadRequestException;
import com.example.kanbanbackend.Exception.ItemNotFoundException;
import com.example.kanbanbackend.Repository.Primary.BoardRepository;
import com.example.kanbanbackend.Repository.Primary.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LimitService {

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private TaskRepository taskRepository;
    public Boolean CheckLimitTask(String boardId,Integer statusId) {
        Board board = boardRepository.findBoardById(boardId);
        if(board == null) throw new ItemNotFoundException("Board id "+ boardId +" doesn't exist!");
        Config LimitConfig = board.getConfigId();
        if (LimitConfig.getLimitmaximumTask() != 0) {
            List<Task> listTasks = taskRepository.findByTaskStatusId(statusId);
            if (listTasks.size() >= LimitConfig.getNoOfTasks()) {
                throw new BadRequestException("The Status has on the limit (" + LimitConfig.getNoOfTasks() + ")s You can't edit !!!");
            }
        }
        return true;
    }

}
