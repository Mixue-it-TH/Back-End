package com.example.kanbanbackend.Entitites.Primary;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Entity
@Table(name = "users")
public class User {

    @Id
    @Column(name = "oid", nullable = false)
    @Size(max = 36)
    private String oid;

    @NotNull
    @Size(max = 100, message = "")
    private String userName;

    @NotNull
    @Size(max = 50,message = "")
    private String email;
}
