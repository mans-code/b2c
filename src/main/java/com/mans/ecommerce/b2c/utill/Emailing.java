package com.mans.ecommerce.b2c.utill;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

public class Emailing
{

    private String from;

    private JavaMailSender emailSender;

    Emailing(JavaMailSender emailSender, @Value("${spring.mail.username}") String emailId)
    {
        this.emailSender = emailSender;
        this.from = getFrom(emailId);
    }

    public void sendEmail(List<String> to, String subject, String body)
    {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo((String[]) to.toArray());
        msg.setFrom(from);
        msg.setSubject(subject);
        msg.setText(body);

        emailSender.send(msg);
    }

    private String getFrom(String emailId)
    {
        if (emailId == null)
        {
            return "noreply@E2B.com";
        }
        return "noreply@" + emailId.split("@")[0] + ".com";
    }

}
