package com.example.kanbanbackend.Repository.Primary;

import com.example.kanbanbackend.Entitites.Primary.Config;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ConfigRepository  extends JpaRepository<Config,Integer> {
    @Query("SELECT MAX(s.id) FROM Status s")
    Integer findLatestId();
    Config findTopByOrderByIdDesc();
}
