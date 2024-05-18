package com.example.kanbanbackend.DTO.LimitFunc;

import com.example.kanbanbackend.Utils.LimitConfig;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class LimitConfigDTO {
    public Integer number;

    public boolean limit;
}
