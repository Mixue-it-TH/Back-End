package com.example.kanbanbackend.Entitites.Primary;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Timestamp;
import java.time.*;
import java.util.List;

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
    @JoinColumn(name = "statusId")
    private Status taskStatus;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="boardId")
    private Board board;

    @OneToMany(mappedBy = "tasks", fetch = FetchType.EAGER)
    private List<File> files;


    @Column(name = "createdOn", nullable = false, insertable = false, updatable = false)
    private Timestamp createdOn;

    @Column(name = "updatedOn", nullable = false, insertable = false, updatable = false)
    private Timestamp updatedOn;

    public Task() {
    }
    public Task(String taskTitle, String taskDescription, String taskAssignees, Status taskStatus, Board board) {
        this.taskTitle = taskTitle;
        this.taskDescription = taskDescription;
        this.taskAssignees = taskAssignees;
        this.taskStatus = taskStatus;
        this.board = board;
    }
}