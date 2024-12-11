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

    public void sendInvitationEmail(String to, String inviterName, String boardName, String accessRight, String url) {
        try {
            String modifiedUrl = checkUrl(url);

            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");

            helper.setFrom("noreply@intproj23.sit.kmutt.ac.th", "ITBKK-SY2");
            helper.setReplyTo("noreply@intproj23.sit.kmutt.ac.th");
            helper.setTo(to);
            helper.setSubject(inviterName + " has invited you to collaborate with " + accessRight + " access right on '" + boardName + "' board");

            String emailContent = "<p>" + inviterName + " has invited you to collaborate with <strong>" + accessRight + "</strong> access right on the <strong>'" + boardName + "'</strong> board.</p>"
                    + "<p>You have been invited to collaborate on the board. Click the following link to accept the invitation:</p>"
                    + "<p>If the link doesn't work, please copy and paste the following URL into your browser:</p>"
                    + "<p><a href=\"" + modifiedUrl + "\">Accept Invitation</a></p>";


            helper.setText(emailContent, true);

            mailSender.send(mimeMessage);
        } catch (MessagingException | UnsupportedEncodingException e) {
            System.err.println("Failed to send email: " + e.getMessage());
        }
    }


    private String checkUrl(String url) {
        String baseUrl = "https://intproj23.sit.kmutt.ac.th";
        if (url.startsWith(baseUrl)) {
            return url.replace(baseUrl, baseUrl + "/sy2");
        }
        return url;
    }
}
