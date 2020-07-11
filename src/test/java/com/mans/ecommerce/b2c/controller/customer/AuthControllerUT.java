package com.mans.ecommerce.b2c.controller.customer;

import java.util.Optional;

import com.mans.ecommerce.b2c.controller.utills.dto.SignupDto;
import com.mans.ecommerce.b2c.domain.entity.customer.Customer;
import com.mans.ecommerce.b2c.domain.exception.UserAlreadyExistException;
import com.mans.ecommerce.b2c.service.CustomerService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AuthControllerUT
{

    @InjectMocks
    private AuthController authController;

    @Mock
    private CustomerService customerService;

    private SignupDto signupDto;

    @Test
    public void signup_pass()
    {

        Customer given = new Customer();
        SignupDto signupDto = createSighupDotValidInstance();

        when(customerService.signup(any(SignupDto.class))).thenReturn(Optional.of(given));

        Customer expected = authController.signup(signupDto);

        Assert.assertSame(given, expected);
    }

    @Test(expected = UserAlreadyExistException.class)
    public void signup_fail_userAlreadyExist()
    {
        SignupDto signupDto = createSighupDotValidInstance();

        when(customerService.signup(any(SignupDto.class)))
                .thenReturn(Optional.empty());

        authController.signup(signupDto);
    }


    public SignupDto createSighupDotValidInstance()
    {
        String username = "mans4321";
        String password = "Man#$34564";
        String firstName = "mansour";
        String email = "mans@gmail.com";
        String lastName = "alzahrai";

        return new SignupDto(username,
                             password,
                             email,
                             firstName,
                             lastName);
    }

}
