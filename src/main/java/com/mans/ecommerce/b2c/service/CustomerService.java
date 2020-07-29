package com.mans.ecommerce.b2c.service;

import com.mans.ecommerce.b2c.controller.utills.dto.LoginDto;
import com.mans.ecommerce.b2c.controller.utills.dto.SignupDto;
import com.mans.ecommerce.b2c.domain.entity.customer.Customer;
import com.mans.ecommerce.b2c.domain.exception.LoginException;
import com.mans.ecommerce.b2c.domain.exception.UserAlreadyExistException;
import com.mans.ecommerce.b2c.repository.customer.CustomerRepository;
import com.mans.ecommerce.b2c.security.JwtProvider;
import com.mans.ecommerce.b2c.utill.response.Token;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class CustomerService
{
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerService.class);

    private CustomerRepository customerRepository;

    private AuthenticationManager authenticationManager;

    private PasswordEncoder passwordEncoder;

    private JwtProvider jwtProvider;

    public CustomerService(
            CustomerRepository customerRepository,
            AuthenticationManager authenticationManager,
            PasswordEncoder passwordEncoder,
            JwtProvider jwtProvider)
    {
        this.customerRepository = customerRepository;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.jwtProvider = jwtProvider;
    }

    public Token signin(LoginDto loginDto)
    {
        String username = loginDto.getUsername();
        String password = loginDto.getPassword();

        if (!isPermitted(username, password))
        {
            throw new LoginException();
        }

        return getToken(username);
    }

    public Customer signup(SignupDto signupDto)
    {
        String username = signupDto.getUsername();
        boolean usernameTaken = customerRepository.existsByUsername(username);

        if (usernameTaken)
        {
            throw new UserAlreadyExistException();
        }

        Customer newCustomer = mapSignupDtoToCustomer(signupDto);
        return customerRepository.save(newCustomer);
    }

    public Token getToken(String username)
    {
        String tokenString = jwtProvider.createToken(username);
        return new Token(tokenString);
    }

    private boolean isPermitted(String username, String password)
    {
        try
        {
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password);
            authenticationManager.authenticate(authToken);
        }
        catch (Exception e)
        {
            return false;
        }
        return true;
    }

    private Customer mapSignupDtoToCustomer(SignupDto signupDto)
    {
        String encodedPassword = passwordEncoder.encode(signupDto.getPassword());
        return Customer
                       .builder()
                       .username(signupDto.getUsername())
                       .password(encodedPassword)
                       .email(signupDto.getEmail())
                       .firstName(signupDto.getFirstName())
                       .lastName(signupDto.getLastName())
                       .build();
    }

}

