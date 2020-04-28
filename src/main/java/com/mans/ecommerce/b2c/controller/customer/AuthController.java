package com.mans.ecommerce.b2c.controller.customer;

import javax.validation.Valid;

import com.mans.ecommerce.b2c.controller.utills.dto.LoginDto;
import com.mans.ecommerce.b2c.controller.utills.dto.SignupDto;
import com.mans.ecommerce.b2c.domain.entity.customer.Customer;
import com.mans.ecommerce.b2c.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/customers/auth")
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
        return customerService.signup(signupDto).orElseThrow(() -> new RuntimeException("User already exists"));
    }

    /**
     * Exception handler if NoSuchElementException is thrown in this Controller
     *
     * @param ex exception
     * @return Error message String.
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(RuntimeException.class)
    public String return400(RuntimeException ex)
    {
        return ex.getMessage();
    }
}
