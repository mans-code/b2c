package com.mans.ecommerce.b2c.controller.customer;

import javax.validation.Valid;

import com.mans.ecommerce.b2c.controller.utills.dto.LoginDto;
import com.mans.ecommerce.b2c.controller.utills.dto.SignupDto;
import com.mans.ecommerce.b2c.domain.entity.customer.Customer;
import com.mans.ecommerce.b2c.domain.exception.UserAlreadyExistException;
import com.mans.ecommerce.b2c.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public Authentication login(@RequestBody @Valid LoginDto loginDto)
    {
        return customerService.signin(loginDto.getUsername(), loginDto.getPassword());
    }

    @PostMapping("/signup")
    public Customer signup(@RequestBody @Valid SignupDto signupDto)
    {
        return customerService.signup(signupDto)
                              .orElseThrow(() -> new UserAlreadyExistException());
    }

    //TODO Reset Password

}
