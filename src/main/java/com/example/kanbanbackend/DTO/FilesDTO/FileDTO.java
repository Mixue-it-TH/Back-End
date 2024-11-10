package com.example.kanbanbackend.DTO.FilesDTO;

import lombok.Data;

@Data
public class FileDTO {
    private String id;
    private String url;
    private String name;
    private Integer size;

    public FileDTO() {
    }

    public FileDTO(String id,String name, String url, Integer size) {
        this.id = id;
        this.name = name;
        this.url = url;
        this.size = size;
    }
}
