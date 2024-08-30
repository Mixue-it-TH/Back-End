package com.example.kanbanbackend.Entitites.Primary;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Entity
@Table(name = "customstatus")
public class Status {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "statusId", nullable = false)
    private Integer id;

    @NotBlank
    @Column(name = "statusName", nullable = false,unique = true)
    private String statusName;

    @Column(name = "statusDescription")
    private String statusDescription;

    @Column(name = "statusColor")
    private String statusColor;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "boardId")
    private Board boardId;

    @Column(name = "createdOn", nullable = false, insertable = false, updatable = false)
    private String createdOn;

    @Column(name = "updatedOn", nullable = false, insertable = false, updatable = false)
    private String updatedOn;

}
