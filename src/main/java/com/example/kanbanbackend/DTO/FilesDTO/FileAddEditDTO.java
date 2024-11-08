package com.example.kanbanbackend.DTO.FilesDTO;

import lombok.Data;

@Data
public class FileAddEditDTO {
    private String id;
    private String url;
    private String name;


    public FileAddEditDTO() {
    }
}
