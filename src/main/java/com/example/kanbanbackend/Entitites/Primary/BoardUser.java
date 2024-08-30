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
    private  Board board;

    private String role;

}
