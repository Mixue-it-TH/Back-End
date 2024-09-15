package com.example.kanbanbackend.Entitites.Primary;

import io.viascom.nanoid.NanoId;
import jakarta.persistence.*;
import lombok.Data;

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

    public Board(String id,String boardName, Config configId) {
        this.id = id;
        this.boardName = boardName;
        this.configId = configId;
    }

    public Board() {

    }
}
