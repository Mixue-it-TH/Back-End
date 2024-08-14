package com.example.kanbanbackend.Service;


import com.example.kanbanbackend.Entitites.Share.User;
import com.example.kanbanbackend.Repository.Share.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository repository;



    public List<User> getAllUser() {
        List<User> users = repository.findAll();
        System.out.println("HELLO WORLD");
        return users;
    }

//    @Transactional
//    public User createUser(User userData) {
//        System.out.println(userData);
//        String enconder = passwordService.hashPassword(userData.getPassword());
//        System.out.println("hashed password: " + enconder);
//        userData.setPassword(enconder);
//        System.out.println("Is equal: " + passwordService.verifyPassword(userData.getPassword(), enconder));
////
//       User user = repository.saveAndFlush(userData);
//       return user;
//
//    }
//
//    public String login(User userData) {
//        User user = loadUserByUserName(userData.getUsername());
//       boolean isPasswordValid = passwordService.verifyPassword(userData.getPassword(),user.getPassword());
//       if(!isPasswordValid) throw new Error("Password not valid");
//        String token = jwtTokenUtil.generateToken(user);
//        getEmployees();
//        return token;
//    }
//
//    public User loadUserByUserName(String username) {
//        User user = repository.findUsersByUsername(username);
//        if (user == null) throw new Error("User does not exist");
//        return user;
//    }
//
//    private static void getEmployees()
//    {
//        final String baseuri = "http://ip32ft.sit.kmutt.ac.th:3306/ibkk_shared";
//
//        RestTemplate restTemplate = new RestTemplate();
//
//        String uri = UriComponentsBuilder.fromHttpUrl(baseuri)
//                .queryParam("username", "itbkk.olarn")
//                .queryParam("password", "itbkk/olarn")
//                .toUriString();
//
//        // ส่ง GET request
//        ResponseEntity<User> response = restTemplate.getForEntity(uri, User.class);
//
//        // เข้าถึง response
//        User result = response.getBody();
//        System.out.println(result);

    }


