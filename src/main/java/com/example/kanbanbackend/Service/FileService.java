package com.example.kanbanbackend.Service;

import com.example.kanbanbackend.DTO.FilesDTO.FileAddEditDTO;
import com.example.kanbanbackend.DTO.FilesDTO.FileDTO;
import com.example.kanbanbackend.Entitites.Primary.File;
import com.example.kanbanbackend.Entitites.Primary.Task;
import com.example.kanbanbackend.Exception.BadRequestException;
import com.example.kanbanbackend.Exception.ItemNotFoundException;
import com.example.kanbanbackend.Repository.Primary.FileRepository;
import com.example.kanbanbackend.Repository.Primary.TaskRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class FileService {
    @Autowired
    private FileRepository fileRepository;
    @Autowired
    ListMapper listMapper;
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    ModelMapper mapper;


//    public List<FileDTO> getAllFiles(Integer taskId) {
//        List<File> fileList = fileRepository.findFileByTasks_Id(taskId);
//        return listMapper.mapList(fileList, FileDTO.class);
//    }

//    public Map<String, Object> addFiles(String boardId, Integer taskId, List<FileAddEditDTO> fileDTOList, HttpServletRequest request) {
//        Task taskBoard = taskRepository.findTaskByBoard_IdAndId(boardId, taskId);
//        if (taskBoard == null) throw new ItemNotFoundException("Task id: " + taskId + " or boardId: "+ boardId +" doesn't exist !!!");
//        List<File> existingFiles = fileRepository.findByTasks_Id(taskId);
//
//        // Check how many additional files can be added without exceeding 10
//        int remainingSlots = 3 - existingFiles.size();
//
//        // if exceed can't add
//        if(remainingSlots == 0 ){
//            throw new BadRequestException("There are no space to add the file");
//        }
//
//        List<File> filesToAdd = new ArrayList<>();
//        List<FileAddEditDTO> excessFiles = new ArrayList<>();
//
//        for (int i = 0; i < fileDTOList.size(); i++) {
//            if (i < remainingSlots) {
//                FileAddEditDTO dto = fileDTOList.get(i);
//
//                // Check the file is duplicate if true can't add all
//                boolean isDuplicate = existingFiles.stream()
//                        .anyMatch(existingFile -> existingFile.getName().equals(dto.getName()));
//                if(isDuplicate) {
//                    throw new BadRequestException("Duplicate file name: " + dto.getName());
//                }
//                    File newFile = new File();
//                    newFile.setId(dto.getId());
//                    newFile.setName(dto.getName());
//                    newFile.setUrl(dto.getUrl());
//                    newFile.setTasks(taskBoard);
//                    filesToAdd.add(newFile);
//            } else {
//                // Collect any excess files
//                excessFiles.add(fileDTOList.get(i));
//            }
//        }
//
//        // Save only the allowed number of files
//        fileRepository.saveAll(filesToAdd);
//
//        Map<String, Object> result = new HashMap<>();
//        result.put("addedFiles", filesToAdd.stream()
//                .map(f -> {
//                    FileDTO dto = new FileDTO();
//                    dto.setId(f.getId());
//                    dto.setUrl(f.getUrl());
//                    dto.setName(f.getName());
//                    return dto;
//                })
//                .collect(Collectors.toList()));
//        result.put("excessFiles", excessFiles);
//
//        return result;
//    }
//
//
//    public FileDTO deleteFile(Integer taskId, FileDTO fileDetail) {
//        File file = fileRepository.findFileByIdAndTasks_Id(fileDetail.getId(), taskId);
//        if(file == null) throw new ItemNotFoundException("File id: " + fileDetail.getId() + " doesn't exist !!!");
//        fileRepository.deleteById(file.getId());
//        return mapper.map(file, FileDTO.class);
//    }



}
