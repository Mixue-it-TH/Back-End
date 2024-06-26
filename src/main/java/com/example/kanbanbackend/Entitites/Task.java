package com.example.kanbanbackend.Entitites;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "tasks")
public class Task {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "taskTitle", nullable = false, length = 100)
    private String taskTitle;

    @Column(name = "taskDescription", length = 500)
    private String taskDescription;

    @Column(name = "taskAssignees", length = 30)
    private String taskAssignees;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "taskStatus")
    private Status taskStatus;


    @Column(name = "createdOn", nullable = false, insertable = false, updatable = false)
    private String createdOn;

    @Column(name = "updatedOn", nullable = false, insertable = false, updatable = false)
    private String updatedOn;


}