package com.example.kanbanbackend.Config;

import com.example.kanbanbackend.Entitites.Primary.Board;
import com.example.kanbanbackend.Entitites.Primary.Visibility;
import com.example.kanbanbackend.Exception.ItemNotFoundException;
import com.example.kanbanbackend.Repository.Primary.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class VisibilityConfig {
    @Autowired
    BoardRepository boardRepository;


    public boolean visibilityType(String boardId){
        Board board = boardRepository.findBoardById(boardId);

        if(board == null){
            throw new ItemNotFoundException("Board id '"+boardId+"' not found");
        }
        if(board.getVisibility() == Visibility.PUBLIC){
            return true;
        }else return false;
    }
}
