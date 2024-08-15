package com.example.kanbanbackend.Entitites.Share;

import jakarta.persistence.*;
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
    private String name;

    @NotNull
    @Size(max = 50, message = "")
    private String username;

    @NotNull
    @Size(max = 50,message = "")
    private String email;

    @NotNull
    @Size(max = 100,message = "")
    private String password;

    @NotNull
    private String role;
}

