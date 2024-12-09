package com.example.kanbanbackend.Entitites.Primary;

import io.viascom.nanoid.NanoId;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "boards")
public class Board {

    @Id
    @Column(name = "boardId", nullable = false)
    private String id;

    private String boardName;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "configId")
    private Config configId;

    @Enumerated(EnumType.STRING)
    private Visibility visibility;

    private String theme;

    public Board(String id,String boardName, Config configId, Visibility visibility, String theme) {
        this.id = id;
        this.boardName = boardName;
        this.configId = configId;
        this.visibility = visibility;
        this.theme = theme;
    }

    public Board() {

    }
}
