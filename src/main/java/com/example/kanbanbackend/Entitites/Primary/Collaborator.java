package com.example.kanbanbackend.Entitites.Primary;


import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;


@Entity
@Data
@Table(name = "collaborator")
public class Collaborator implements Serializable {

    @EmbeddedId
    private CollaboratorKey id;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "userId")
    private User user;

    @ManyToOne
    @MapsId("boardId")
    @JoinColumn(name = "boardId")
    private Board board;

    private String role;

    @Column(name = "accessRight", nullable = false)
    private String access_right;

    @Column(name = "addedOn", nullable = false, insertable = false, updatable = false)
    private Timestamp addedOn;

    @Column(name = "updatedOn", nullable = false, insertable = false, updatable = false)
    private Timestamp updatedOn;

    private String ownerName;

    public Collaborator(User user, Board board, String role, String accessRight, String ownerName) {
        this.user = user;
        this.board = board;
        this.role = role;
        this.access_right = accessRight;
        this.ownerName = ownerName;

        this.id = new CollaboratorKey();
        this.id.setUserId(user.getOid());
        this.id.setBoardId(board.getId());
    }

    public Collaborator() {

    }
}
