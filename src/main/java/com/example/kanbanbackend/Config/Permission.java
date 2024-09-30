package com.example.kanbanbackend.Config;

import com.example.kanbanbackend.Entitites.Primary.BoardUser;
import com.example.kanbanbackend.Exception.BadRequestException;
import com.example.kanbanbackend.Exception.ItemNotFoundException;
import com.example.kanbanbackend.Repository.Primary.BoardUserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class Permission {
    @Autowired
    BoardUserRepository boardUserRepository;
    private static List<Integer> exceptStatus = new ArrayList<>(Arrays.asList(1,7));



    public boolean getRoleOfBoard(String boardId, String oid){
        BoardUser boardUser = boardUserRepository.findBoardUserByBoard_IdAndUser_Oid(boardId, oid);
        if(boardUser == null){
            return false;
        }
        if(boardUser.getRole().equalsIgnoreCase("owner")){
            return true;
        }else {
            return false;
        }

    }
}
