package com.example.kanbanbackend.Service;

import com.example.kanbanbackend.Entitites.AuthUser;
import com.example.kanbanbackend.Entitites.Share.User;
import com.example.kanbanbackend.Repository.Share.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
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
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, userName + " does not exist !!");
        }
        List<GrantedAuthority> roles = new ArrayList<>();
        GrantedAuthority grantedAuthority = new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return customer.getRole();
            }
        };
        roles.add(grantedAuthority);
        UserDetails userDetails = new AuthUser(userName, customer.getPassword(), roles);
        return userDetails;
    }
}
