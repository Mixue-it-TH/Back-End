package com.example.kanbanbackend.Entitites.Primary;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;

import java.io.Serializable;

@Embeddable
@Data
public class CollaboratorKey implements Serializable {

    @Column(name = "userId")
    private String userId;

    @Column(name = "boardId")
    private String boardId;
}
