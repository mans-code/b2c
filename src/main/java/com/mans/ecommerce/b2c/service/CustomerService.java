package com.mans.ecommerce.b2c.service;

import java.util.Optional;

import com.mans.ecommerce.b2c.controller.utills.dto.LoginDto;
import com.mans.ecommerce.b2c.controller.utills.dto.SignupDto;
import com.mans.ecommerce.b2c.domain.entity.customer.Customer;
import com.mans.ecommerce.b2c.repository.customer.CustomerRepository;
import com.mans.ecommerce.b2c.security.JwtProvider;
import com.mans.ecommerce.b2c.utill.Token;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
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

    public Optional<Token> signin(LoginDto loginDto)
    {
        LOGGER.info("New user attempting to sign in");

        Optional<Token> token = Optional.empty();
        String username = loginDto.getUsername();
        String password = loginDto.getPassword();

        try
        {
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password);
            authenticationManager.authenticate(authToken);
            String tokenString = jwtProvider.createToken(username);
            token = Optional.of(new Token(tokenString));
        }
        catch (AuthenticationException e)
        {
            System.out.println(e.getMessage());
            LOGGER.info("Log in failed for user {}", username);
        }

        return token;
    }

    public Optional<Customer> signup(SignupDto signupDto)
    {
        String username = signupDto.getUsername();
        Optional<Customer> customer = customerRepository.findByUsername(username);

        if (customer.isPresent())
        {
            return Optional.empty();
        }

        Customer newCustomer = mapSignupDtoToCustomer(signupDto);
        Customer savedCustomer = customerRepository.save(newCustomer);
        return Optional.of(savedCustomer);
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

