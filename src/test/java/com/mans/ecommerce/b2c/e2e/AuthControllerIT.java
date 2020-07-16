package com.mans.ecommerce.b2c.e2e;

import java.util.Map;
import java.util.Optional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.mans.ecommerce.b2c.controller.utills.dto.LoginDto;
import com.mans.ecommerce.b2c.controller.utills.dto.SignupDto;
import com.mans.ecommerce.b2c.domain.entity.customer.Customer;
import com.mans.ecommerce.b2c.repository.customer.CustomerRepository;
import com.mans.ecommerce.b2c.utill.NewCustomerResponse;
import com.mans.ecommerce.b2c.utill.Token;
import com.mans.ecommerce.b2c.utills.Message;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import static org.hamcrest.Matchers.equalToIgnoringCase;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

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
    private CustomerRepository customerRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private final String VALID_DB_USERNAME = "valid";

    private final String VALID_DB_PASSWORD = "Password1@";

    private final MediaType MEDIA_TYPE = MediaType.APPLICATION_JSON;

    @Test
    public void signin_pass()
            throws Exception
    {
        LoginDto loginDto = new LoginDto(VALID_DB_USERNAME, VALID_DB_PASSWORD);
        String url = "/auth/signin";
        MvcResult result = postRequest(url, loginDto);

        assertSameStatus(result, HttpStatus.OK);

        Token response = getResponse(result, Token.class);
        String token = response.getToken();
        assertFalse(token.isEmpty());
    }

    @Test
    public void signin_fail_usernameNotFound()
            throws Exception
    {
        String usernameNotInDB = "not_found";
        LoginDto loginDto = new LoginDto(usernameNotInDB, VALID_DB_PASSWORD);

        unsuccessfullySignin(loginDto);
    }

    @Test
    public void sign_fail_passwordNotMatchingUsername()
            throws Exception
    {
        String wrongPassword = "wrongPassword1#";
        LoginDto loginDto = new LoginDto(VALID_DB_USERNAME, wrongPassword);

        unsuccessfullySignin(loginDto);
    }

    @Test
    public void sign_fail_validation()
            throws Exception
    {
        String invalidPassword = "invalidPassword";
        String invalidUsername = "#invalidUsername#";
        LoginDto loginDto = new LoginDto(invalidUsername, invalidPassword);

        unsuccessfullySignin(loginDto);
    }

    @Test
    public void signup_pass()
            throws Exception
    {

        SignupDto signupDto = validSignupDtoInstence();
        String url = "/auth/signup";
        MvcResult result = postRequest(url, signupDto);

        assertSameStatus(result, HttpStatus.CREATED);

        NewCustomerResponse response = getResponse(result, NewCustomerResponse.class);
        assertFalse(response.getCustomerId().isEmpty());
        assertFalse(response.getToken().isEmpty());

        Optional<Customer> dbCustomerOption = customerRepository.findById(response.getCustomerId());
        assertTrue(dbCustomerOption.isPresent());

        Customer dbCustomer = dbCustomerOption.get();
        assertSameInfoExceptPassword(dbCustomer, signupDto);
    }

    @Test
    public void signup_failed_userAlreadyExists()
            throws Exception
    {
        SignupDto signupDto = validSignupDtoInstence();
        signupDto.setUsername(VALID_DB_USERNAME);

        String url = "/auth/signup";
        MvcResult result = postRequest(url, signupDto);

        assertSameStatus(result, HttpStatus.CONFLICT);

        Message response = getResponse(result, Message.class);
        String expectedMessage = "User Already Exists";
        assertThat(response.getMessage(), equalToIgnoringCase(expectedMessage));
    }

    @Test
    public void signup_failed_validationError()
            throws Exception
    {
        SignupDto signupDto = invalidSignupDtoInstence();
        String url = "/auth/signup";
        MvcResult result = postRequest(url, signupDto);

        assertSameStatus(result, HttpStatus.BAD_REQUEST);

        Map<String, String> response = getResponse(result, Map.class);
        int numOfExpectedError = 5;

        assertEquals(numOfExpectedError, response.size());
        response.values().forEach(v -> assertFalse(v.isEmpty()));
    }

    private void unsuccessfullySignin(LoginDto loginDto)
            throws Exception
    {
        String url = "/auth/signin";
        MvcResult result = postRequest(url, loginDto);

        assertSameStatus(result, HttpStatus.UNAUTHORIZED);

        Message response = getResponse(result, Message.class);
        String expectedMessage = "Username or the password is invalid";
        assertThat(response.getMessage(), equalToIgnoringCase(expectedMessage));
    }

    private MvcResult postRequest(String url, Object content)
            throws Exception
    {
        return mvc.perform(post(url)
                                   .content(toJson(content))
                                   .contentType(MEDIA_TYPE))
                  .andReturn();

    }

    private SignupDto validSignupDtoInstence()
    {
        return SignupDto
                       .builder()
                       .username("mans_code")
                       .password(VALID_DB_PASSWORD)
                       .email("mans@mans.com")
                       .firstName("mans")
                       .lastName("alzahrani")
                       .build();
    }

    private SignupDto invalidSignupDtoInstence()
    {
        return SignupDto
                       .builder()
                       .username("man")
                       .password("Admin1")
                       .email("mansmans.com")
                       .firstName("m")
                       .lastName("a")
                       .build();
    }

    private void assertSameStatus(MvcResult result, HttpStatus status)
    {
        int actualStatus = result.getResponse().getStatus();
        int expectedStatus = status.value();
        assertEquals(expectedStatus, actualStatus);
    }

    private void assertSameInfoExceptPassword(Customer dbCustomer, SignupDto signupDto)
    {
        assertThat(dbCustomer.getUsername(), equalToIgnoringCase(signupDto.getUsername()));
        assertThat(dbCustomer.getEmail(), equalToIgnoringCase(signupDto.getEmail()));
        assertThat(dbCustomer.getFirstName(), equalToIgnoringCase(signupDto.getFirstName()));
        assertThat(dbCustomer.getLastName(), equalToIgnoringCase(signupDto.getLastName()));

        assertNotEquals(signupDto.getPassword(), dbCustomer.getPassword());
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

    private <T> T getResponse(MvcResult result, Class<T> cls)
            throws Exception
    {
        String content = result.getResponse().getContentAsString();
        return objectMapper.readValue(content, cls);
    }
}
