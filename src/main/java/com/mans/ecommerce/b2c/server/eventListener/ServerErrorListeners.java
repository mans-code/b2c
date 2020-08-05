package com.mans.ecommerce.b2c.server.eventListener;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mans.ecommerce.b2c.server.eventListener.entity.ServerErrorEvent;
import com.mans.ecommerce.b2c.utill.Emailing;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;

public class ServerErrorListeners
{

    private Emailing emailing;

    private ObjectMapper objectMapper;

    private List<String> teamEmail;

    private List<String> teamPhone;

    ServerErrorListeners(
            Emailing emailing,
            ObjectMapper objectMapper,
            @Value("${app.crash.team.phones}") String phoneNum,
            @Value("${app.crash.team.emails}") String teamEmail)
    {
        this.emailing = emailing;
        this.objectMapper = objectMapper;
        this.teamPhone = new ArrayList<>(Arrays.asList(phoneNum.split(",")));
        this.teamEmail = new ArrayList<>(Arrays.asList(teamEmail.split(",")));
    }

    @Async
    @EventListener
    void sendEmail(ServerErrorEvent event)
    {
        Exception exception = event.getException();
        sendEmail(exception);
        sendPhoneMessage();
    }

    private void sendEmail(Exception ex)
    {
        String subject = "APP Crash!!!!!!!!!!!!";
        String body_template = "Time=%s \n"
                                       + "exception=%s";
        String exception = getJson(ex);
        String time = LocalDate.now().toString();
        String body = String.format(body_template, time, exception);
        emailing.sendEmail(teamEmail, subject, body);
    }

    private void sendPhoneMessage()
    {
    }

    private String getJson(Exception ex)
    {
        try
        {
            return objectMapper.writeValueAsString(ex);
        }
        catch (JsonProcessingException e)
        {
            return ex.getMessage();
        }
    }
}
