package com.example.kanbanbackend.Entitites.Primary;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.sql.Timestamp;
import java.time.ZoneId;
import java.time.ZonedDateTime;

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
    private Board board;


    @Column(name = "createdOn")
    private Timestamp createdOn;

    @Column(name = "updatedOn")
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
