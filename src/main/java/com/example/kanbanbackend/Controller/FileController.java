package com.example.kanbanbackend.Controller;


import com.example.kanbanbackend.Service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = {"http://ip23sy2.sit.kmutt.ac.th", "http://intproj23.sit.kmutt.ac.th", "https://ip23sy2.sit.kmutt.ac.th", "https://intproj23.sit.kmutt.ac.th", "http://localhost:5173"})
@RequestMapping("/v3/files")
public class FileController {
    @Autowired
    FileService fileService;

//    @GetMapping("/{id}")
//    public ResponseEntity<Object> getFileByTaskId(@PathVariable Integer taskId) {
//        return ResponseEntity.ok(fileService.getAllFiles(taskId));
//    }
}
