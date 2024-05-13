package com.example.kanbanbackend.Service;

import com.example.kanbanbackend.DTO.StatusDTO;
import com.example.kanbanbackend.DTO.StatusSelectedDTO;
import com.example.kanbanbackend.DTO.TaskAddEditDTO;
import com.example.kanbanbackend.DTO.TaskDTO;
import com.example.kanbanbackend.Entitites.Status;
import com.example.kanbanbackend.Entitites.Task;
import com.example.kanbanbackend.Exception.ItemNotFoundDelUpdate;
import com.example.kanbanbackend.Exception.ItemNotFoundException;
import com.example.kanbanbackend.Repository.StatusRepository;
import com.example.kanbanbackend.Repository.TaskRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StatusService {
    @Autowired
    ModelMapper mapper;
    @Autowired
    ListMapper listMapper;
    @Autowired
    private StatusRepository repository;

    @Autowired
    private TaskRepository taskRepository;

    public List<StatusDTO> getAllStatus(){
        List<Status> status = repository.findAll();
        return  listMapper.mapList(status,StatusDTO.class);
    }
    public StatusSelectedDTO getStatusById(int id){
        Status status = repository.findById(id).orElseThrow(() -> new ItemNotFoundException("Status "+ id +" does not exist !!!" ));
        return mapper.map(status,StatusSelectedDTO.class);
    }
    public StatusDTO createStatus(StatusDTO newStatusDTO){
        Status status = mapper.map(newStatusDTO,Status.class);
        if(status.getStatusColor() == null || status.getStatusColor().isBlank()){
            status.setStatusColor("#6b7280");
        }
        Optional.ofNullable(status.getStatusName())
                .map(String::trim)
                .ifPresent(status::setStatusName);
        Optional.ofNullable(status.getStatusDescription())
                .map(String::trim)
                .ifPresent(status::setStatusDescription);
        repository.saveAndFlush(status);
        return mapper.map(status,StatusDTO.class);
    }
    public StatusDTO updateStatus(Integer statusId, StatusDTO editedStatus ){
       Status oldStatus = repository.findById(statusId).orElseThrow(() -> new ItemNotFoundDelUpdate(" NOT FOUND "));
        oldStatus.setStatusName(editedStatus.getStatusName() != null ? editedStatus.getStatusName() : oldStatus.getStatusName());
        oldStatus.setStatusDescription(editedStatus.getStatusDescription() != null ? editedStatus.getStatusDescription() : oldStatus.getStatusDescription());
        oldStatus.setStatusColor(editedStatus.getStatusColor() != null ? editedStatus.getStatusColor() : oldStatus.getStatusColor());   
        repository.save(oldStatus);
        return mapper.map(oldStatus, StatusDTO.class);
    }

    public void deleteStatus(Integer delId){
        Status statusDel = repository.findById(delId).orElseThrow(() -> new ItemNotFoundDelUpdate( "NOT FOUND "));
        repository.delete(statusDel);
    }

    public void deleteStatusAndTransfer(Integer delId,Integer tranferId){
        Status statusDel = repository.findById(delId).orElseThrow(() -> new ItemNotFoundDelUpdate("NOT FOUND"));
        Status statusTransfer = repository.findById(tranferId).orElseThrow(() -> new ItemNotFoundDelUpdate("NOT FOUND"));
        if(statusDel != null && statusTransfer != null) {
            List<Task> taskList = taskRepository.findByTaskStatus(statusDel);
            System.out.println(taskList);
            taskList.forEach((task) -> {
                task.setTaskStatus(statusTransfer);
                taskRepository.save(task);
            });
            repository.delete(statusDel);
        }
    }
}
