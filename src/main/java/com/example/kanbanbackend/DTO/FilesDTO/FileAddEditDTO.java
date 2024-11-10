package com.example.kanbanbackend.DTO.FilesDTO;

import lombok.Data;

@Data
public class FileAddEditDTO {
    private String id;
    private String url;
    private String name;
    private long size;
    private String type;
    private byte[] data;

    public FileAddEditDTO() {
    }

    public FileAddEditDTO(String id, String name, String url, long size, String type, byte[] data) {
        this.id = id;
        this.name = name;
        this.url = url;
        this.size = size;
        this.type = type;
        this.data = data;
    }
}
