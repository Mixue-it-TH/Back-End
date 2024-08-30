package com.example.kanbanbackend.Entitites.Primary;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "boards")
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "boardId", nullable = false)
    private Integer id;

    private String boardName;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "configId")
    private Config configId;
}
