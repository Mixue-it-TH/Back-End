package com.example.kanbanbackend.Controller;


import com.example.kanbanbackend.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = {"http://ip23sy2.sit.kmutt.ac.th", "http://intproj23.sit.kmutt.ac.th"})
@RequestMapping("/v2/user")
public class UserContoller {

    @Autowired
    private UserService service;

    @GetMapping("")
    public ResponseEntity<Object> getAllUser(){
        return ResponseEntity.ok(service.getAllUser());
    }

//    @GetMapping("/helloworld")
//    public ResponseEntity<Object> HelloWorld(){
//        return ResponseEntity.ok(service.getAllStatus());
//    }

//    @GetMapping("/lnwza")
//    public ResponseEntity<Object> Login(@RequestBody User user){
//        return ResponseEntity.ok(service.login(user));
//    }

//    @PostMapping("/create")
//    public ResponseEntity<Object> createUser(@Valid @RequestBody User user){
//       return ResponseEntity.ok(service.createUser(user));
//
//    }
}
