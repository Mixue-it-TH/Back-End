package com.example.kanbanbackend.Service;


import com.example.kanbanbackend.DTO.Token;
import com.example.kanbanbackend.Auth.JwtRequestUser;
import com.example.kanbanbackend.Entitites.AuthUser;
import com.example.kanbanbackend.Entitites.Share.User;
import com.example.kanbanbackend.Exception.UnauthorizedException;
import com.example.kanbanbackend.Repository.Primary.PrimaryUserRepository;
import com.example.kanbanbackend.Repository.Share.UserRepository;
import com.example.kanbanbackend.Auth.JwtTokenUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
    @Autowired
    private PrimaryUserRepository primaryUserRepository;

    @Autowired
    AuthenticationManager authenticationManager;


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
        // check is user is exist on the DB
        User user = repository.findUsersByUsername(userData.getUserName());
        if(user == null) throw new UnauthorizedException("Username or Password is incorrect");

            UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(userData.getUserName(), userData.getPassword());
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
            if (!authentication.isAuthenticated()) {
                throw new UsernameNotFoundException("Invalid user or password");
            }

        String tokenGenarate = jwtTokenUtil.generateToken(user);
        String refreshToken = jwtTokenUtil.generateRefreshToken(user);

        com.example.kanbanbackend.Entitites.Primary.User primaryUser = primaryUserRepository.findUsersByOid(user.getOid());
        if(primaryUser == null) {
            primaryUser = new com.example.kanbanbackend.Entitites.Primary.User(user.getOid(), user.getUsername(), user.getEmail());
            primaryUserRepository.save(primaryUser);
        }
        return  new Token(tokenGenarate, refreshToken);
    }

    public Token refreshLogin( HttpServletRequest request) {
        // check is user is exist on the DB
        String token = request.getHeader("Authorization").substring(7).trim();
        Claims claims = jwtTokenUtil.getAllClaimsFromToken(token);
        User user = repository.findUserByOid(claims.get("oid").toString());
        if(user == null) throw new UnauthorizedException("Username or Password is incorrect");

        String tokenGenarate = jwtTokenUtil.generateToken(user);

        return  new Token(tokenGenarate);
    }


    public User loadUserByUserName(String username) {

        User user = repository.findUsersByUsername(username);
        UserDetails userDetails = new AuthUser(username,user.getPassword());
        System.out.println(userDetails.getPassword());
        if (user == null) throw new UnauthorizedException("Username or Password is incorrect");
        boolean isPasswordValid = passwordService.verifyPassword(user.getPassword(), user.getPassword());
        if (!isPasswordValid) throw new UnauthorizedException("Username or Password is incorrect");
        return user;
    }


}

