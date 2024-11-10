package com.example.kanbanbackend.Entitites.Primary;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@Entity
@Table(name = "invitations")
public class Invitation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "invitationId", nullable = false)
    private Integer id;

    @Column(name = "accessRight", nullable = false)
    @JsonProperty("accessRight")
    private String access_right;

    private String status;

    private String inviterName;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "oid")
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "boardId")
    private Board board;


    public Invitation(String access_right,String status,String inviterName, User user, Board board) {
        this.access_right = access_right;
        this.status = status;
        this.inviterName = inviterName;
        this.user = user;
        this.board = board;

    }

    public Invitation() {

    }
}
