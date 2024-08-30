package com.example.kanbanbackend.Entitites.Primary;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;

import java.io.Serializable;

@Embeddable
@Data
public class BoardUserKey implements Serializable {

    @Column(name = "userId")
    Integer userId;

    @Column(name = "boardId")
    Integer boardId;
}
