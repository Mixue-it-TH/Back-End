package com.example.kanbanbackend.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserDTO {

    @Id
    @Column(name = "oid", nullable = false)
    @Size(max = 36)
    private String oid;

    @NotNull
    @Size(max = 100, message = "")
    private String name;

    @NotNull
    @Size(max = 50, message = "")
    @JsonProperty("userName")
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
