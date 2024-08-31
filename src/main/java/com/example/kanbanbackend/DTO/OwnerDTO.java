package com.example.kanbanbackend.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class OwnerDTO {
    private String oid;
    @JsonProperty("name")
    private String userName;
}
