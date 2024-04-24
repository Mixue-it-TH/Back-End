package com.example.kanbanbackend.Entitites;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "tasks")
public class Task {
    @Id
    private Integer id;
    private String taskTitle;
    private String taskDescription;
    private String taskAssignees;
    private String taskStatus;
    private String createdOn;
    private String updatedOn;
}
