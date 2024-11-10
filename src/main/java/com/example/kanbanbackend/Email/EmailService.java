package com.example.kanbanbackend.Email;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;


@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendInvitationEmail(String to, String inviterName, String boardName, String accessRight, String url) throws MessagingException, UnsupportedEncodingException {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");

            helper.setFrom("noreply@intproj23.sit.kmutt.ac.th", "ITBKK-SY2");
            helper.setReplyTo("noreply@intproj23.sit.kmutt.ac.th");
            helper.setTo(to);
            helper.setSubject(inviterName + " has invited you to collaborate with " + accessRight + " access right on '" + boardName + "' board");
            helper.setText(inviterName + " has invited you to collaborate with " + accessRight + " access right on '" + boardName + "' board. "
                    + "You have been invited to collaborate on the board. Click the following link to accept the invitation:\n"
                    + url);

            mailSender.send(mimeMessage);
        } catch (MessagingException | UnsupportedEncodingException e) {
            System.err.println("Failed to send email: " + e.getMessage());
        }
    }
}
