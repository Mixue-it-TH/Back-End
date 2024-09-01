package com.example.kanbanbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.sql.Timestamp;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

@SpringBootApplication
public class kanbanbackendApplication {

    public static void main(String[] args) {


        SpringApplication.run(kanbanbackendApplication.class, args);
        ZonedDateTime now = ZonedDateTime.now(ZoneOffset.UTC);
        System.out.println("now time: " + now); // Should be in UTC

        Timestamp timestamp = Timestamp.from(now.toInstant());
        System.out.println("Timestamp: " + timestamp);

        // Convert timestamp back to ZonedDateTime for verification
        ZonedDateTime convertedBack = Timestamp.from(now.toInstant()).toInstant().atZone(ZoneOffset.UTC);
        System.out.println("Converted back: " + convertedBack);
    }

}
