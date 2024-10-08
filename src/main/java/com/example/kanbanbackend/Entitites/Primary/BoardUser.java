package com.example.kanbanbackend.Entitites.Primary;


import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;


@Entity
@Data
public class BoardUser implements Serializable {

    @EmbeddedId
    private BoardUserKey id;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "userId")
    private User user;

    @ManyToOne
    @MapsId("boardId")
    @JoinColumn(name = "boardId")
    private Board board;

    private String role;

    public BoardUser(User user, Board board, String role) {
        this.user = user;
        this.board = board;
        this.role = role;

        this.id = new BoardUserKey();
        this.id.setUserId(user.getOid());
        this.id.setBoardId(board.getId());
    }

    public BoardUser() {

    }
}
