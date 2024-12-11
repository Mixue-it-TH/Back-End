package com.example.kanbanbackend.Service;


import com.example.kanbanbackend.Auth.JwtRequestUser;
import com.example.kanbanbackend.Auth.JwtTokenUtil;
import com.example.kanbanbackend.DTO.Token;
import com.example.kanbanbackend.Entitites.AuthUser;
import com.example.kanbanbackend.Entitites.Share.User;
import com.example.kanbanbackend.Exception.UnauthorizedException;
import com.example.kanbanbackend.Repository.Primary.PrimaryUserRepository;
import com.example.kanbanbackend.Repository.Share.UserRepository;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.context.SecurityContextHolder;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class UserService {

    private final OAuth2AuthorizedClientService authorizedClientService;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    private UserRepository repository;
    @Autowired
    private PasswordService passwordService;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private PrimaryUserRepository primaryUserRepository;

    public UserService(OAuth2AuthorizedClientService authorizedClientService) {
        this.authorizedClientService = authorizedClientService;
    }


    public List<User> getAllUser() {
        List<User> users = repository.findAll();
        return users;
    }
    public String getMicrosoftAccessToken(Authentication authentication) {
        String clientRegistrationId = "azure-dev";

        OAuth2AuthorizedClient authorizedClient = authorizedClientService.loadAuthorizedClient(
                clientRegistrationId,
                authentication.getName()
        );

        if (authorizedClient != null) {
            OAuth2AccessToken accessToken = authorizedClient.getAccessToken();
            return accessToken != null ? accessToken.getTokenValue() : null;
        }
        return null;
    }
    public Token getTokenByMicrosoftAuthen(Authentication authentication) {

        // 401 Unauthorized
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new UnauthorizedException("The token is expried");
        }
        OAuth2AuthorizedClient authorizedClient = authorizedClientService.loadAuthorizedClient(
                "azure-dev",
                authentication.getName()
        );

        OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();
        Map<String, Object> oauth2UserAttributes = oauth2User.getAttributes();


        if (authorizedClient != null) {
            OAuth2AccessToken accessToken = authorizedClient.getAccessToken();
            if (accessToken != null) {

                com.example.kanbanbackend.Entitites.Primary.User user = new com.example.kanbanbackend.Entitites.Primary.User(
                        oauth2UserAttributes.get("oid").toString(),
                        (String) oauth2UserAttributes.get("name"),
                        (String) oauth2UserAttributes.get("email")
                );

                com.example.kanbanbackend.Entitites.Primary.User primaryUser = primaryUserRepository.findUsersByOid(user.getOid());
                if (primaryUser == null) {
                    primaryUser = new com.example.kanbanbackend.Entitites.Primary.User(user.getOid(), user.getUserName(), user.getEmail());
                    primaryUserRepository.save(primaryUser);
                }

                String tokenGenerate = jwtTokenUtil.generateToken(user);
                String refreshToken = jwtTokenUtil.generateRefreshToken(user);

                return new Token(tokenGenerate, refreshToken);
            }
        }
        return null;
    }


    public Token login(JwtRequestUser userData) {
        // check is user is exist on the DB

        User user = repository.findUsersByUsername(userData.getUserName());
        if (user == null) throw new UnauthorizedException("Username or Password is incorrect");

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(userData.getUserName(), userData.getPassword());
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        if (!authentication.isAuthenticated()) {
            throw new UsernameNotFoundException("Invalid user or password");

        }

        String tokenGenarate = jwtTokenUtil.generateToken(user);
        String refreshToken = jwtTokenUtil.generateRefreshToken(user);

        com.example.kanbanbackend.Entitites.Primary.User primaryUser = primaryUserRepository.findUsersByOid(user.getOid());
        if (primaryUser == null) {
            primaryUser = new com.example.kanbanbackend.Entitites.Primary.User(user.getOid(), user.getName(), user.getEmail());
            primaryUserRepository.save(primaryUser);
        }

        return new Token(tokenGenarate, refreshToken);
    }


    public Token
    refreshLogin(HttpServletRequest request) {
        // check is user is exist on the DB
        String token = request.getHeader("Authorization").substring(7).trim();
        Claims claims = jwtTokenUtil.getAllClaimsFromToken(token);
        User user = repository.findUserByOid(claims.get("oid").toString());
        if (user == null) throw new UnauthorizedException("Username or Password is incorrect");

        String tokenGenarate = jwtTokenUtil.generateToken(user);

        return new Token(tokenGenarate);
    }


    public User loadUserByUserName(String username) {
        User user = repository.findUsersByUsername(username);
        UserDetails userDetails = new AuthUser(username, user.getPassword());
        System.out.println(userDetails.getPassword());
        if (user == null) throw new UnauthorizedException("Username or Password is incorrect");
        boolean isPasswordValid = passwordService.verifyPassword(user.getPassword(), user.getPassword());
        if (!isPasswordValid) throw new UnauthorizedException("Username or Password is incorrect");
        return user;
    }


}


