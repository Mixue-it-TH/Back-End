package com.example.kanbanbackend.DTO.InvitationDTO;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class InvitationDTO {
    private String oid;

    @JsonProperty("name")
    private String userName;
    private String email;
    @JsonProperty("accessRight")
    private String access_right;

    private String status;


    @JsonIgnore
    @Column(name = "added_On", nullable = false, insertable = false, updatable = false)
    private Timestamp addedOn;
}
