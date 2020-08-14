package com.mans.ecommerce.b2c.utill;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
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
        sendAsync(msg);
    }

    public void sendEmail(String to, String subject, String body)
    {
        List<String> tos = new ArrayList<String>(Arrays.asList(to));
        sendEmail(tos, subject, body);
    }

    private void sendAsync(SimpleMailMessage msg)
    {
        Mono.fromCallable(() -> {
            emailSender.send(msg);
            return true;
        }).subscribe();
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
