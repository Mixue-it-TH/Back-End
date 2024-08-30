package com.example.kanbanbackend.Entitites.Primary;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "config")
public class Config {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "settingId", nullable = false)
    private Integer id;

    private String  limitmaximumTask;

    private Integer isEnable;
}
