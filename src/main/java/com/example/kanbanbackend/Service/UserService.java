package com.example.kanbanbackend.Service;


import com.example.kanbanbackend.DTO.Token;
import com.example.kanbanbackend.Auth.JwtRequestUser;
import com.example.kanbanbackend.Entitites.Share.User;
import com.example.kanbanbackend.Exception.UnauthorizedException;
import com.example.kanbanbackend.Repository.Share.UserRepository;
import com.example.kanbanbackend.Auth.JwtTokenUtil;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository repository;

    @Autowired
    private PasswordService passwordService;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;


    public List<User> getAllUser() {
        List<User> users = repository.findAll();
        return users;
    }

    @Transactional
    public User createUser(User userData) {
        String enconder = passwordService.hashPassword(userData.getPassword());
        userData.setPassword(enconder);
        User user = repository.saveAndFlush(userData);
        return user;

    }

    public Token login(JwtRequestUser userData) {
        User user = loadUserByUserName(userData.getUsername());
        boolean isPasswordValid = passwordService.verifyPassword(userData.getPassword(), user.getPassword());
        if (!isPasswordValid) throw new UnauthorizedException("Username or Password is incorrect");
        String tokenGenarate = jwtTokenUtil.generateToken(user);
        Token token = new Token();
        token.setAccess_token(tokenGenarate);
        return token;
    }

    public User loadUserByUserName(String username) {
        User user = repository.findUsersByUsername(username);
        if (user == null) throw new UnauthorizedException("Username or Password is incorrect");
        return user;
    }


}

