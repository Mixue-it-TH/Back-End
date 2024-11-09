package com.example.kanbanbackend.Entitites.Primary;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.sql.Timestamp;

@Data
@Entity
@Table(name = "files")
public class File {
    @Id
    @Column(name = "idfiles", nullable = false)
    private String id;

    private String url;

    private String name;

    @Column(name = "addedOn", nullable = false, insertable = false, updatable = false)
    private Timestamp addedOn;

    private Integer size;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="tasks_id")
    @JsonIgnore
    private Task tasks;
}
