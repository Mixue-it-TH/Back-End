package com.example.kanbanbackend.Service;


import com.example.kanbanbackend.Entitites.Share.User;
import com.example.kanbanbackend.Repository.Share.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository; // ใช้ repository ของคุณเพื่อเข้าถึงข้อมูลผู้ใช้

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findUsersByUsername(username); // ดึงข้อมูลผู้ใช้จากฐานข้อมูล
        if (user == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), new ArrayList<>()); // สร้าง UserDetails object
    }
}
