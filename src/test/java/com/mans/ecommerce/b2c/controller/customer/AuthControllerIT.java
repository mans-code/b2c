package com.mans.ecommerce.b2c.controller.customer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mans.ecommerce.b2c.controller.utills.dto.SignupDto;
import com.mans.ecommerce.b2c.service.CustomerService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@RunWith(SpringRunner.class)
@WebMvcTest(AuthController.class)
public class AuthControllerIT
{

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CustomerService customerService;

    private SignupDto signupDto;

    private MediaType JSON = MediaType.APPLICATION_JSON;

    @Before
    public void createSighupDotValidInstance()
    {
        String username = "mans4321";
        String password = "Man#$34564";
        String firstName = "mansour";
        String email = "mans@gmail.com";
        String lastName = "alzahrai";
        signupDto = new SignupDto(username,
                                  password,
                                  email,
                                  firstName,
                                  lastName);
    }

    @Test()
    public void signup_fail_invalidEmail()
            throws Exception
    {
        signupDto.setEmail("manskdkd");
        invalidSignupForm();
    }

    @Test()
    public void signup_fail_invalidUsername()
            throws Exception
    {
        signupDto.setUsername("m456+*");
        invalidSignupForm();
    }

    @Test()
    public void signup_fail_invalidPassword()
            throws Exception
    {
        signupDto.setPassword("m456+*");
        invalidSignupForm();
    }

    @Test()
    public void signup_fail_invalidFirstName()
            throws Exception
    {
        signupDto.setFirstName("m");
        invalidSignupForm();
    }

    @Test()
    public void signup_fail_invalidLastName()
            throws Exception
    {
        signupDto.setLastName("m");
        invalidSignupForm();
    }

    private MvcResult signup()
            throws Exception
    {
        String signupDtoJson = objectMapper.writeValueAsString(signupDto);

        return mockMvc.perform(post("/auth/signup")
                                       .contentType(JSON)
                                       .content(signupDtoJson))
                      .andReturn();
    }

    private void invalidSignupForm()
            throws Exception
    {
        MvcResult mvcResult = signup();

        int status = mvcResult.getResponse().getStatus();
        Exception exception = getException(mvcResult);

        assertTrue(exception instanceof MethodArgumentNotValidException);
        assertEquals(status, HttpStatus.BAD_REQUEST.value());
    }

    private Exception getException(MvcResult result)
    {
        return result.getResolvedException();
    }
}
