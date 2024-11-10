package com.example.kanbanbackend.Service;


import com.example.kanbanbackend.Auth.JwtTokenUtil;
import com.example.kanbanbackend.Config.Permission;
import com.example.kanbanbackend.Config.VisibilityConfig;
import com.example.kanbanbackend.DTO.FilesDTO.FileDTO;
import com.example.kanbanbackend.DTO.StatusDTO.StatusSelectedDTO;
import com.example.kanbanbackend.DTO.TaskDTO.TaskAddEditDTO;
import com.example.kanbanbackend.DTO.TaskDTO.TaskDTO;
import com.example.kanbanbackend.DTO.TaskDTO.TaskSelectedDTO;
import com.example.kanbanbackend.DTO.TaskDTO.TaskV3.TaskBoardDTO;
import com.example.kanbanbackend.DTO.TaskDTO.TaskV3.TaskBoardtestDTO;
import com.example.kanbanbackend.Entitites.Primary.*;
import com.example.kanbanbackend.Exception.*;
import com.example.kanbanbackend.Repository.Primary.*;
import io.jsonwebtoken.Claims;
import io.viascom.nanoid.NanoId;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TaskService {
    @Autowired
    ModelMapper mapper;
    @Autowired
    ListMapper listMapper;
    @Autowired
    private TaskRepository repository;


    @Autowired
    private StatusRepository statusRepository;

    @Autowired
    private BoardRepository boardRepository;
    @Autowired
    private CloudinaryService cloudinaryService;

    @Autowired
    private StatusService statusService;

    @Autowired
    private LimitService limitService;
    @Autowired
    private FileRepository fileRepository;



    @Autowired
    private Permission permission;
    @Autowired
    private VisibilityConfig visibilityConfig;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    private Integer MAX_FILELIMIT = 10;


    public List<TaskDTO> getAllTodo(List<String> filterStatuses, String sortBy, String sortDirection) {
        List<Task> taskList = new ArrayList<>();
        Sort.Direction direction = sortDirection.equalsIgnoreCase("ASC") ? Sort.Direction.ASC : Sort.Direction.DESC;

        if ("statusName".equals(sortBy)) {
            sortBy = "taskStatus.statusName";
        }
        Sort sort = Sort.by(direction, sortBy);
        if (filterStatuses != null) {
            taskList.addAll(repository.findAllByStatusNamesSorted(filterStatuses, sort));
            return listMapper.mapList(taskList, TaskDTO.class);
        }
        List<Task> tasks = repository.findAll(sort);
        return listMapper.mapList(tasks, TaskDTO.class);
    }

//    public TaskSelectedDTO getTaskById(String boardId, int id, HttpServletRequest request) throws ItemNotFoundException {
////        Task task =  repository.findById(id).orElseThrow(() -> new ItemNotFoundException("Task "+ id +" does not exist !!!" ));
//        Task task = repository.findTaskByBoard_IdAndId(boardId, id);
//        System.out.println(task);
//        if (task == null) throw new ItemNotFoundException("Task id: " + id + " or boardId: "+ boardId +" doesn't exist !!!");
//            // Assuming `getCreatedOn` and `getUpdatedOn` return `Timestamp`
//            Timestamp createdOnTimestamp = task.getCreatedOn();
//            Timestamp updatedOnTimestamp = task.getUpdatedOn();
//
//
//            // Convert Timestamp to LocalDateTime
//            LocalDateTime createdDateTime = createdOnTimestamp.toLocalDateTime();
//            LocalDateTime updatedDateTime = updatedOnTimestamp.toLocalDateTime();
//
//            // Convert LocalDateTime to ZonedDateTime in UTC
//            ZonedDateTime createdUTC = createdDateTime.atZone(ZoneOffset.UTC);
//            ZonedDateTime updatedUTC = updatedDateTime.atZone(ZoneOffset.UTC);
//
//            // Convert ZonedDateTime to ISO 8601 format string
//            String createdOnISO = createdUTC.format(DateTimeFormatter.ISO_INSTANT);
//            String updatedOnISO = updatedUTC.format(DateTimeFormatter.ISO_INSTANT);
//
//            TaskSelectedDTO dto = mapper.map(task, TaskSelectedDTO.class);
//            dto.setCreatedOn(createdOnISO);
//            dto.setUpdatedOn(updatedOnISO);
//
//
//            return dto;
//
//    }

    @Transactional
    public TaskDTO createTask(String boardId,TaskAddEditDTO newTaskDTO, HttpServletRequest request) {
        Board board = boardRepository.findBoardById(boardId);
        if(board == null) {
            throw new ItemNotFoundException("Board id: " + boardId + " doesn't exist !!!");
        }
            Status statusfind = statusRepository.findStatusByBoard_IdAndId(boardId,newTaskDTO.getTaskStatusId());
            if(statusfind == null) throw new BadRequestWithFieldException("status", "Status id "+newTaskDTO.getTaskStatusId()+" or Board id"+ boardId +" does not exist");

            limitService.CheckLimitTask(boardId,statusfind.getId());

            newTaskDTO.setBoardId(boardId);
            Task newTask = mapper.map(newTaskDTO, Task.class);
            newTask.setTaskTitle(newTaskDTO.getTaskTitle() == null ? null : newTaskDTO.getTaskTitle().trim());
            newTask.setTaskDescription(newTaskDTO.getTaskDescription() == null || newTaskDTO.getTaskDescription().isEmpty() ? null : newTaskDTO.getTaskDescription().trim());
            newTask.setTaskAssignees(newTaskDTO.getTaskAssignees() == null || newTaskDTO.getTaskAssignees().isEmpty() ? null : newTaskDTO.getTaskAssignees().trim());
            repository.saveAndFlush(newTask);
            Status status = mapper.map(statusfind, Status.class);
            newTask.setTaskStatus(status);
            return mapper.map(newTask, TaskDTO.class);
        
    }

    // old version
//    @Transactional
//    public TaskDTO updateTask(String boardId,Integer taskId, TaskAddEditDTO editedTask, HttpServletRequest request) {
//        Task oldTask = repository.findTaskByBoard_IdAndId(boardId, taskId);
//        if (oldTask == null) throw new ItemNotFoundException("Task id: " + taskId + " or boardId: "+ boardId +" doesn't exist !!!");
//
//        if (editedTask.getTaskTitle() == null) throw new BadRequestWithFieldException("titie", "must not be null");
//
//        limitService.CheckLimitTask(boardId,editedTask.getTaskStatusId());
//
//
//        Status isExited = statusRepository.findStatusByBoard_IdAndId(boardId,editedTask.getTaskStatusId());
//        if(isExited == null) throw new BadRequestWithFieldException("status", "Status id "+editedTask.getTaskStatusId()+"or Board id"+ boardId +" does not exist");
//
//
//        Claims claims = jwtTokenUtil.decodedToken(request);
//            Optional.ofNullable(editedTask.getTaskTitle())
//                    .map(String::trim)
//                    .ifPresent(editedTask::setTaskTitle);
//            Optional.ofNullable(editedTask.getTaskDescription())
//                    .map(String::trim)
//                    .ifPresent(editedTask::setTaskDescription);
//            Optional.ofNullable(editedTask.getTaskAssignees())
//                    .map(String::trim)
//                    .ifPresent(editedTask::setTaskAssignees);
//            oldTask.setId(editedTask.getId() != null ? editedTask.getId() : oldTask.getId());
//            oldTask.setTaskTitle(editedTask.getTaskTitle() != null ? editedTask.getTaskTitle() : oldTask.getTaskTitle());
//            oldTask.setTaskAssignees(editedTask.getTaskAssignees() != null ? editedTask.getTaskAssignees() : oldTask.getTaskAssignees());
//            oldTask.setTaskStatus(editedTask.getTaskStatusId() != null ? isExited : oldTask.getTaskStatus());
//            oldTask.setTaskDescription(editedTask.getTaskDescription() != null ? editedTask.getTaskDescription() : oldTask.getTaskDescription());
//            Date now = new Date();
//            Timestamp timestamp = new Timestamp(now.getTime());
//            oldTask.setUpdatedOn(timestamp);
//
//            repository.saveAndFlush(oldTask);
//            StatusSelectedDTO newStatus = statusService.getStatusByIdAndBoardId(boardId,oldTask.getTaskStatus().getId(),request);
//            TaskBoardtestDTO dto = mapper.map(oldTask,TaskBoardtestDTO.class);
//            dto.setTaskStatus(newStatus);
//
//            return mapper.map(dto, TaskDTO.class);
//
//
//    }


    // update task with file
    @Transactional
    public TaskDTO updateTask(String boardId, Integer taskId, TaskAddEditDTO editedTask, List<MultipartFile> files, HttpServletRequest request) throws IOException {
        Task oldTask = repository.findTaskByBoard_IdAndId(boardId, taskId);
        if (oldTask == null) throw new ItemNotFoundException("Task id: " + taskId + " or boardId: " + boardId + " doesn't exist !!!");

        if (editedTask.getTaskTitle() == null)
            throw new BadRequestWithFieldException("title", "must not be null");

        limitService.CheckLimitTask(boardId, editedTask.getTaskStatusId());

        Status newStatus = statusRepository.findStatusByBoard_IdAndId(boardId, editedTask.getTaskStatusId());
        if (newStatus == null)
            throw new BadRequestWithFieldException("status", "Status id " + editedTask.getTaskStatusId() + " or Board id " + boardId + " does not exist");

        // ลบไฟล์ที่ไม่ต้องการ
        List<String> arrayDelete = editedTask.getArrayDelete();
        List<FileDTO> deletedFiles = new ArrayList<>();
        if (arrayDelete != null && !arrayDelete.isEmpty()) {
            deletedFiles = deleteFiles(taskId, arrayDelete, request);  // เก็บไฟล์ที่ถูกลบ
        }


        // เพิ่มไฟล์ใหม่หรือตรวจสอบและอัปเดตไฟล์ที่มีอยู่
        List<FileDTO> addedFiles = new ArrayList<>();
        List<FileDTO> excessFiles = new ArrayList<>();
        if (files != null && !files.isEmpty()) {
            // หากไม่พบไฟล์ชื่อเดียวกัน ให้ insert file เข้าไป DB
            Map<String, Object> addResult = addFiles(boardId, taskId, files, request);
            addedFiles = (List<FileDTO>) addResult.get("files");
            excessFiles = (List<FileDTO>) addResult.get("excessFiles");

        }

        // อัปเดตรายละเอียดของ Task
        Optional.ofNullable(editedTask.getTaskTitle()).map(String::trim).ifPresent(oldTask::setTaskTitle);
        Optional.ofNullable(editedTask.getTaskDescription()).map(String::trim).ifPresent(oldTask::setTaskDescription);
        Optional.ofNullable(editedTask.getTaskAssignees()).map(String::trim).ifPresent(oldTask::setTaskAssignees);
        oldTask.setTaskStatus(newStatus);
        oldTask.setUpdatedOn(new Timestamp(System.currentTimeMillis()));

        // บันทึกการเปลี่ยนแปลงของ Task
        repository.saveAndFlush(oldTask);

        // สร้าง TaskDTO และตั้งค่าไฟล์
        TaskDTO taskDTO = mapper.map(oldTask, TaskDTO.class);
        taskDTO.setNewFiles(addedFiles);
        taskDTO.setExcessFiles(excessFiles);
        taskDTO.setDeleteFiles(deletedFiles);  // เพิ่มไฟล์ที่ถูกลบเข้าไป

        return taskDTO;
    }





    // old version
//    @Transactional
//    public TaskDTO deleteTask(String boardId,Integer delId, HttpServletRequest request) {
//        Task delTask = repository.findTaskByBoard_IdAndId(boardId, delId);
//        if (delTask == null) throw new ItemNotFoundException("Task id: " + delId + " or boardId: "+ boardId +" doesn't exist !!!");
//
//        Claims claims = jwtTokenUtil.decodedToken(request);
//            repository.delete(delTask);
//            return mapper.map(delTask, TaskDTO.class);
//
//    }

    // delete with file
    @Transactional
    public TaskDTO deleteTask(String boardId, Integer delId, HttpServletRequest request) throws Exception {
        // ค้นหา Task ที่ต้องการลบ โดยใช้ boardId และ delId
        Task delTask = repository.findTaskByBoard_IdAndId(boardId, delId);

        if (delTask == null) throw new ItemNotFoundException("Task id: " + delId + " or boardId: " + boardId + " doesn't exist !!!");

        List<File> files = delTask.getFiles();

        // ลบไฟล์ที่เกี่ยวข้องทั้งหมด
        if (files != null && !files.isEmpty()) {
            List<String> publicIds = new ArrayList<>();

            for (File file : files) {
                publicIds.add(file.getId());
            }

            cloudinaryService.deleteFiles(publicIds);

            fileRepository.deleteAll(files);
        }

        repository.delete(delTask);

        return mapper.map(delTask, TaskDTO.class);
    }


    // Version 3


    // old version
//    public List<TaskBoardDTO>getTaskofBoard(String boardId, HttpServletRequest request) {
//        List<Task> taskBoard = repository.findTaskByBoard_Id(boardId);
//        return listMapper.mapList(taskBoard, TaskBoardDTO.class);
//
//    }


    //PBI 29 ใช้งานได้มั้ง


    public List<TaskBoardDTO> getTaskofBoard(String boardId, HttpServletRequest request) {
        List<Task> taskBoard = repository.findTaskByBoard_Id(boardId);

        // Map Task entities to TaskBoardDTO list
        List<TaskBoardDTO> taskBoardDTOList = listMapper.mapList(taskBoard, TaskBoardDTO.class);
        // ตั้งค่าข้อมูลไฟล์ใน TaskBoardDTO โดยอิงจาก Task
        for (TaskBoardDTO taskDTO : taskBoardDTOList) {
            List<FileDTO> fileDTOs = taskBoard.stream()
                    .filter(task -> task.getId().equals(taskDTO.getId()))
                    .flatMap(task -> task.getFiles().stream().map(file -> new FileDTO(file.getId(),file.getName(), file.getUrl(), file.getSize())))
                    .collect(Collectors.toList());
            taskDTO.setFiles(fileDTOs);
        }


        return taskBoardDTOList;
    }

    public TaskSelectedDTO getTaskById(String boardId, int id, HttpServletRequest request) throws ItemNotFoundException {
        Task task = repository.findTaskByBoard_IdAndId(boardId, id);

        if (task == null) {
            throw new ItemNotFoundException("Task id: " + id + " or boardId: " + boardId + " doesn't exist !!!");
        }

        // Convert Timestamps to ISO strings
        String createdOnISO = task.getCreatedOn().toInstant().toString();
        String updatedOnISO = task.getUpdatedOn().toInstant().toString();

        // Map Task to TaskSelectedDTO and set created/updated time
        TaskSelectedDTO dto = mapper.map(task, TaskSelectedDTO.class);
        dto.setCreatedOn(createdOnISO);
        dto.setUpdatedOn(updatedOnISO);

        // Set files in TaskSelectedDTO from Task's files
        List<FileDTO> fileDTOs = task.getFiles().stream()
                .sorted(Comparator.comparing(File::getAddedOn))
                .map(file -> new FileDTO(file.getId(), file.getName(), file.getUrl(), file.getSize()))
                .collect(Collectors.toList());

        dto.setFiles(fileDTOs);

        return dto;
    }


    @Transactional
    public Map<String, Object> addFiles(String boardId, Integer taskId, List<MultipartFile> files, HttpServletRequest request) throws IOException {

        Task taskBoard = repository.findTaskByBoard_IdAndId(boardId, taskId);
        if (taskBoard == null) throw new ItemNotFoundException("Task id: " + taskId + " or boardId: "+ boardId +" doesn't exist !!!");

        List<File> existingFiles = fileRepository.findByTasks_Id(taskId);

        // Check how many additional files can be added without exceeding the limit
        int remainingSlots = MAX_FILELIMIT - existingFiles.size();

        // 404 If no space, throw an error
        if (remainingSlots <= 0) {
            throw new BadRequestException("There is no space to add the file.");
        }

        List<File> filesToAdd = new ArrayList<>();
        List<FileDTO> excessFiles = new ArrayList<>();

        for (int i = 0; i < files.size(); i++) {
            MultipartFile file = files.get(i);

//            FileDTO dto = fileDTOList.get(i);
            String fileName = file.getOriginalFilename();


            if (i < remainingSlots) {
                // Check if a file with the same name exists for the same task
                boolean fileExists = fileRepository.findFileByTasks_IdAndName(taskId, fileName) != null;

                // 409 If the file already exists, throw an error
                if (fileExists) {
                    throw new ConflictException("This file " + fileName + " already exists for the current task! Please Delete  attachment and add again");
                }

                if(file.getSize() > 20 * 1024 * 1024) {
                    continue;
                }
                // UPLOAD FILE TO CLOUDINARY
                String resourceType = cloudinaryService.getResourceType(file);
                Map result = cloudinaryService.uploadFile(file,resourceType);
                // Create a new file
                File newFile = new File();
                newFile.setId(result.get("public_id").toString());
                newFile.setName(fileName);
                newFile.setUrl(result.get("url").toString());
                newFile.setTasks(taskBoard);
                newFile.setSize((int) file.getSize());
                filesToAdd.add(newFile);
            } else {
//                 Add excess files to excessFiles list
    //                excessFiles.add(dto);
            }
        }

        // Save only the allowed number of files
        fileRepository.saveAll(filesToAdd);

        // Prepare result map with added and excess files
        Map<String, Object> result = new HashMap<>();
        result.put("id", taskBoard.getId());
        result.put("title", taskBoard.getTaskTitle());
        result.put("description", taskBoard.getTaskDescription());
        result.put("assignees", taskBoard.getTaskAssignees());

        // Set added files
        List<FileDTO> addedFileDTOs = filesToAdd.stream()
                .map(f -> new FileDTO(f.getId(), f.getName(), f.getUrl(), f.getSize()))
                .collect(Collectors.toList());
        result.put("files", addedFileDTOs);

        // Include excess files in the result
        result.put("excessFiles", excessFiles);

        return result;
    }


    @Transactional
    public List<FileDTO> deleteFiles(Integer taskId, List<String> fileIds, HttpServletRequest request) throws IOException {
        List<FileDTO> deletedFiles = new ArrayList<>();

        Task task = repository.findById(taskId)
                .orElseThrow(() -> new ItemNotFoundException("Task id: " + taskId + " doesn't exist."));

        // ลบไฟล์ออกจาก task ก่อนเพื่อที่จะได้ลบ file ใน table ได้
        task.getFiles().removeIf(file -> fileIds.contains(file.getId()));


        for (String fileId : fileIds) {
            // เช็คว่าเจอไหม
            Optional<File> fileExist = fileRepository.findById(fileId);
            if (fileExist.isEmpty()) {
                throw new ItemNotFoundException("File id: " + fileId + " doesn't exist in the " + taskId);
            }
            File fileResultDelete = fileExist.get();

            cloudinaryService.deleteFile(fileId);
            fileRepository.deleteById(fileId);
            deletedFiles.add(new FileDTO(fileId, fileResultDelete.getName(), fileResultDelete.getUrl(),fileResultDelete.getSize()));  // คุณสามารถตั้งค่าข้อมูลไฟล์ที่ต้องการส่งกลับได้
        }

        return deletedFiles;
    }





}