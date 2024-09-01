package com.example.kanbanbackend.Repository.Primary;

import com.example.kanbanbackend.Entitites.Primary.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PrimaryUserRepository extends JpaRepository<User,String> {
}
