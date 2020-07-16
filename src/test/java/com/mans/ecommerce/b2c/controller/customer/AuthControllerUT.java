package com.mans.ecommerce.b2c.controller.customer;

import java.util.Optional;

import com.mans.ecommerce.b2c.controller.utills.dto.LoginDto;
import com.mans.ecommerce.b2c.controller.utills.dto.SignupDto;
import com.mans.ecommerce.b2c.domain.entity.customer.Customer;
import com.mans.ecommerce.b2c.domain.exception.LoginException;
import com.mans.ecommerce.b2c.domain.exception.UserAlreadyExistException;
import com.mans.ecommerce.b2c.service.CustomerService;
import com.mans.ecommerce.b2c.utill.NewCustomerResponse;
import com.mans.ecommerce.b2c.utill.Token;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import sun.rmi.runtime.Log;
import static org.hamcrest.Matchers.equalToIgnoringCase;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthControllerUT
{
    @Mock
    private CustomerService customerService;

    @InjectMocks
    private AuthController authController;

    private final String VALID_USERNAME = "valid";

    private final String VALID_PASSWORD = "Password1@";

    @Test
    public void sign_pass()
    {
        Token expected = new Token("pass");
        Optional<Token> optionalToken = Optional.of(expected);
        LoginDto loginDto = new LoginDto(VALID_USERNAME, VALID_PASSWORD);

        when(customerService.signin(any()))
                .thenReturn(optionalToken);

        Token actual = authController.login(loginDto);

        assertSame(expected, actual);
    }

    @Test()
    public void sign_fail_invalidUsernameOrPassword()
    {
        LoginDto loginDto = new LoginDto();

        assertThrows(LoginException.class,
                     () -> authController.login(loginDto));
    }

    @Test()
    public void signin_fail_validationError()
    {
        LoginDto loginDto = new LoginDto();

        assertThrows(LoginException.class,
                     () -> authController.login(loginDto));

        verify(customerService, never()).signup(any());
    }

    @Test
    public void signup_pass()
    {

        Customer customer = Customer.builder().id("id").build();
        Optional<Customer> optionalCustomer = Optional.of(customer);
        String expectedToken = "pass";
        Token token = new Token(expectedToken);
        SignupDto signupDto = new SignupDto();

        when(customerService.signup(any()))
                .thenReturn(optionalCustomer);

        when(customerService.getToken(any()))
                .thenReturn(token);

        NewCustomerResponse expected = authController.signup(signupDto);

        assertThat(expected.getCustomerId(), equalToIgnoringCase(customer.getId()));
        assertThat(expected.getToken(), equalToIgnoringCase(expectedToken));
    }

    @Test()
    public void signup_fail_userAlreadyExist()
    {
        when(customerService.signup(any()))
                .thenReturn(Optional.empty());

        assertThrows(UserAlreadyExistException.class,
                     () -> authController.signup(new SignupDto()));

    }

}
