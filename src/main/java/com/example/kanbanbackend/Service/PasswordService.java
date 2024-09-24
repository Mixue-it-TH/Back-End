package com.example.kanbanbackend.Service;

import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@EnableWebSecurity
public class PasswordService {

    private final Argon2PasswordEncoder passwordEncoder;
    public PasswordService() {
        this.passwordEncoder = new Argon2PasswordEncoder(
                8,   // salt length
                32,       // hash length
                1,       // parallelism
                65536,      // memory
                3        // iterations
        );
    }

    public String hashPassword(String plainPassword) {
        return passwordEncoder.encode(plainPassword);
    }

    public boolean verifyPassword(String plainPassword, String hashedPassword) {
        return passwordEncoder.matches(plainPassword, hashedPassword);
    }
}
