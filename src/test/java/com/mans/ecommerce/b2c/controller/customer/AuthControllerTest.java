package com.mans.ecommerce.b2c.controller.customer;

import java.util.Optional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mans.ecommerce.b2c.controller.utills.dto.SignupDto;
import com.mans.ecommerce.b2c.domain.entity.customer.Customer;
import com.mans.ecommerce.b2c.domain.exception.UserAlreadyExistException;
import com.mans.ecommerce.b2c.service.CustomerService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(AuthController.class)
public class AuthControllerTest
{
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CustomerService customerService;

    @Test
    public void signup_pass()
            throws Exception
    {

        when(customerService.signup(any(SignupDto.class)))
                .thenReturn(Optional.of(new Customer()));

        SignupDto signupDto = createSighupDotValidInstance();

        mockMvc.perform(post("/auth/signup")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(signupDto)))
               .andExpect(status().isOk());
    }

    @Test()
    public void sighup_fail_userAlreadyExist()
            throws Exception
    {

        when(customerService.signup(any(SignupDto.class)))
                .thenReturn(Optional.empty());

        SignupDto signupDto = createSighupDotValidInstance();

        mockMvc.perform(post("/auth/signup")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(signupDto)))
               .andExpect(result -> assertTrue(result.getResolvedException() instanceof UserAlreadyExistException))
               .andExpect(result -> assertEquals("User Already Exists", result.getResolvedException().getMessage()));
    }

    private SignupDto createSighupDotValidInstance()
    {
        String username = "mans4321";
        String password = "Man#$34564";
        String firstName = "mansour";
        String email = "mans@gmail.com";
        String lastName = "alzahrai";
        return new SignupDto(username, password, email, firstName, lastName);
    }

}
