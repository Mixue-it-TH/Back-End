package com.example.kanbanbackend.DTO.CollabsDTO;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class CollabDTO {
    private String oid;
    @JsonProperty("name")
    private String userName;
    private String email;
    @JsonProperty("accessRight")
    private String access_right;

    @JsonIgnore
    @Column(name = "added_On", nullable = false, insertable = false, updatable = false)
    private Timestamp addedOn;
}
