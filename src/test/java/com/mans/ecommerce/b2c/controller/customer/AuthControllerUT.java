package com.mans.ecommerce.b2c.controller.customer;

import java.util.Optional;

import com.mans.ecommerce.b2c.controller.utills.dto.LoginDto;
import com.mans.ecommerce.b2c.controller.utills.dto.SignupDto;
import com.mans.ecommerce.b2c.domain.entity.customer.Customer;
import com.mans.ecommerce.b2c.domain.exception.LoginException;
import com.mans.ecommerce.b2c.domain.exception.UserAlreadyExistException;
import com.mans.ecommerce.b2c.service.CustomerService;
import com.mans.ecommerce.b2c.utill.Token;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import static org.junit.Assert.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AuthControllerUT
{

    @InjectMocks
    private AuthController authController;

    @Mock
    private CustomerService customerService;

    @Test
    public void sign_pass()
    {
        Token expected = new Token("pass");
        Optional<Token> optionalToken = Optional.of(expected);
        LoginDto loginDto = new LoginDto();

        when(customerService
                     .signin(any(), any()))
                .thenReturn(optionalToken);

        Token actual = authController.login(loginDto);

        assertSame(expected, actual);
    }

    @Test(expected = LoginException.class)
    public void sign_fail_invalidUsernameOrPassword()
    {
        Optional<Token> optionalToken = Optional.empty();
        LoginDto loginDto = new LoginDto();

        when(customerService
                     .signin(any(), any()))
                .thenReturn(optionalToken);

        authController.login(loginDto);
    }

    @Test
    public void signup_pass()
    {

        Customer given = new Customer();
        SignupDto signupDto = new SignupDto();
        Optional<Customer> optionalCustomer = Optional.of(given);

        when(customerService
                     .signup(any()))
                .thenReturn(optionalCustomer);

        Customer expected = authController.signup(signupDto);

        assertSame(given, expected);
    }

    @Test(expected = UserAlreadyExistException.class)
    public void signup_fail_userAlreadyExist()
    {
        when(customerService.signup(any()))
                .thenReturn(Optional.empty());

        authController.signup(new SignupDto());
    }

}
