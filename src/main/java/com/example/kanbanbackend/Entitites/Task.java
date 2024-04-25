package com.example.kanbanbackend.Entitites;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.sql.Timestamp;

@Data
@Entity
@Table(name = "tasks")
public class Task {
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "taskTitle", nullable = false, length = 100)
    private String taskTitle;

    @Column(name = "taskDescription", length = 500)
    private String taskDescription;

    @Column(name = "taskAssignees", length = 30)
    private String taskAssignees;

    @Column(name = "taskStatus")
    private String taskStatus;

    @Column(name = "createdOn", nullable = false)
    private String createdOn;

    @Column(name = "updatedOn", nullable = false)
    private String updatedOn;
}
