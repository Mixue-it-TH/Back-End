package com.example.kanbanbackend.Email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendInvitationEmail(String to, String inviterName, String boardName, String accessRight, String url) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("noreply@intproj23.sit.kmutt.ac.th");
        message.setTo(to);
        message.setSubject(inviterName + " has invited you to collaborate with " + accessRight + " access right on " + boardName + " board");
        message.setText("You have been invited to collaborate on the board. Click the following link to accept the invitation:\n" +
                url);

        mailSender.send(message);
    }
}
