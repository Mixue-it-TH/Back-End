package com.example.kanbanbackend.Entitites.Primary;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

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


    @Column(name = "createdOn",nullable = false, insertable = true, updatable = true)
    private Timestamp createdOn;

    @Column(name = "updatedOn",nullable = false, insertable = true, updatable = true)
    private Timestamp updatedOn;

    private static final ZoneId SYSTEM_ZONE = ZoneId.systemDefault();
    @PrePersist
    protected void onCreate() {
        ZonedDateTime now = ZonedDateTime.now(SYSTEM_ZONE);
        this.createdOn = Timestamp.from(now.toInstant());
        this.updatedOn = this.createdOn;
    }

    @PreUpdate
    protected void onUpdate() {
        ZonedDateTime now = ZonedDateTime.now(SYSTEM_ZONE);
        this.updatedOn = Timestamp.from(now.toInstant());
    }


}