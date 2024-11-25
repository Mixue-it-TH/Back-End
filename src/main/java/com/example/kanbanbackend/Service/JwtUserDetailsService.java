package com.example.kanbanbackend.Service;

import com.example.kanbanbackend.Entitites.AuthUser;
import com.example.kanbanbackend.Entitites.Share.User;
import com.example.kanbanbackend.Repository.Share.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
public class JwtUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        User customer = userRepository.findUsersByUsername(userName);

        if (customer == null) {
            List<GrantedAuthority> roles = new ArrayList<>();
            roles.add(new SimpleGrantedAuthority("ROLE_MSIP_USER"));
            return  new AuthUser(userName, "", roles);
        }
        List<GrantedAuthority> roles = new ArrayList<>();

        UserDetails userDetails = new AuthUser(userName, customer.getPassword(), roles);
        return userDetails;
    }
}
