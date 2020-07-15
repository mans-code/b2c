package com.mans.ecommerce.b2c.e2e;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.mans.ecommerce.b2c.controller.utills.dto.LoginDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import static org.hamcrest.Matchers.equalToIgnoringCase;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class AuthControllerIT
{
    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private final String VALID_USERNAME = "valid";

    private final String VALID_PASSWORD = "Password1@";

    private final MediaType MEDIA_TYPE = MediaType.APPLICATION_JSON;

    @Test
    public void signin_pass()
            throws Exception
    {
        LoginDto loginDto = new LoginDto(VALID_USERNAME, VALID_PASSWORD);

        MvcResult result = mvc.perform(post("/auth/signin")
                                               .content(toJson(loginDto))
                                               .contentType(MEDIA_TYPE))
                              .andExpect(status().isOk())
                              .andReturn();

        DocumentContext jsonContext = parseResponse(result);
        String tokenPath = "$['token']";
        String token = jsonContext.read(tokenPath);

        assertThat(token.length(), greaterThan(1));
    }

    @Test
    public void signin_fail_usernameNotFound()
            throws Exception
    {
        String wrongPassword = "wrongPassword1#";
        LoginDto loginDto = new LoginDto(VALID_USERNAME, wrongPassword);

        unsuccessfullySignin(loginDto);
    }

    @Test
    public void sign_fail_passwordNotMatchingUsername()
            throws Exception
    {
        String usernameNotInDB = "not_found";
        LoginDto loginDto = new LoginDto(usernameNotInDB, VALID_PASSWORD);

        unsuccessfullySignin(loginDto);
    }

    @Test
    public void signup_pass()
            throws Exception
    {

    }

    private void unsuccessfullySignin(LoginDto loginDto)
            throws Exception
    {

        MvcResult result = mvc.perform(post("/auth/signin")
                                               .content(toJson(loginDto))
                                               .contentType(MEDIA_TYPE))
                              .andExpect(status().isUnauthorized())
                              .andReturn();

        DocumentContext jsonContext = parseResponse(result);
        String messagePath = "$['message']";

        String actualMessage = jsonContext.read(messagePath);
        String expectedMessage = "Username or the password is invalid";
        assertThat(actualMessage, equalToIgnoringCase(expectedMessage));
    }

    private DocumentContext parseResponse(MvcResult result)
            throws Exception
    {
        String body = result.getResponse().getContentAsString();
        return JsonPath.parse(body);
    }

    private String toJson(Object object)
            throws JsonProcessingException
    {
        return objectMapper.writeValueAsString(object);
    }

}
