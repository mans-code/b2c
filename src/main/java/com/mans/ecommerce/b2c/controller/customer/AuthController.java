package com.mans.ecommerce.b2c.controller.customer;

import javax.validation.*;
import java.util.Set;

import com.mans.ecommerce.b2c.controller.utills.dto.LoginDto;
import com.mans.ecommerce.b2c.controller.utills.dto.SignupDto;
import com.mans.ecommerce.b2c.domain.entity.customer.Customer;
import com.mans.ecommerce.b2c.domain.exception.LoginException;
import com.mans.ecommerce.b2c.domain.exception.UserAlreadyExistException;
import com.mans.ecommerce.b2c.service.CustomerService;
import com.mans.ecommerce.b2c.utill.NewCustomerResponse;
import com.mans.ecommerce.b2c.utill.Token;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController
{

    private CustomerService customerService;

    @Autowired
    public AuthController(CustomerService customerService)
    {
        this.customerService = customerService;
    }

    @PostMapping("/signin")
    public Token login(@RequestBody LoginDto loginDto)
    {
        if (!isValid(loginDto))
        {
            throw new LoginException();
        }

        return customerService.signin(loginDto);
    }

    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    public NewCustomerResponse signup(@RequestBody @Valid SignupDto signupDto)
    {
        Customer newCustomer = customerService.signup(signupDto);
        Token token = customerService.getToken(newCustomer.getUsername());
        NewCustomerResponse response = new NewCustomerResponse(newCustomer.getId(), token.getToken());

        return response;
    }

    private boolean isValid(LoginDto loginDto)
    {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<Object>> violations = validator.validate(loginDto);

        return violations.size() == 0;
    }

    //TODO Reset Password

}
