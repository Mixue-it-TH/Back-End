package com.example.kanbanbackend.Entitites;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;
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
    @JoinColumn(name = "taskStatus")
    private Status taskStatus;

//    private String taskStatus;

    @Column(name = "createdOn", nullable = false, insertable = false, updatable = false)
    private String createdOn;

    @Column(name = "updatedOn", nullable = false, insertable = false, updatable = false)
    private String updatedOn;
}
