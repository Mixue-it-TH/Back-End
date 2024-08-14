package com.example.kanbanbackend.Entitites.Share;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;


    @Column(length = 50,nullable = false)
    private String username;

    @Size(max = 14,message = "TOO LONG KUB")
    @Column(length = 14,nullable = false)
    private String password;
}
