package com.example.kanbanbackend.Repository.Share;

import com.example.kanbanbackend.Entitites.Share.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Integer> {

    User findUsersByUsername(String username);
}