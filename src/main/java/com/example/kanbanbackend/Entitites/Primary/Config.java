package com.example.kanbanbackend.Entitites.Primary;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

@Data
@Entity
@Table(name = "config")
public class Config {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "settingId", nullable = false)
    private Integer id;


    private Integer  noOfTasks;

    private Integer limitMaximumTask;

    public Config() {
        this.noOfTasks = 5;
        this.limitMaximumTask = 0;
    }
}
