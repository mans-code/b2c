package com.mans.ecommerce.b2c.service;

import java.util.Optional;

import com.mans.ecommerce.b2c.controller.utills.dto.SignupDto;
import com.mans.ecommerce.b2c.domain.entity.customer.Customer;
import com.mans.ecommerce.b2c.repository.customer.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class CustomerService
{
    private CustomerRepository customerRepository;

    private AuthenticationManager authenticationManager;

    private PasswordEncoder passwordEncoder;

    @Autowired
    public CustomerService(
            CustomerRepository customerRepository,
            AuthenticationManager authenticationManager,
            PasswordEncoder passwordEncoder)
    {
        this.customerRepository = customerRepository;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
    }

    public Authentication signin(String username, String password)
    {
        return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
    }

    public Optional<Customer> signup(SignupDto signupDto)
    {
        if (customerRepository.findByUsername(
                signupDto.getUsername())
                               .isPresent()
        )
        {
            return Optional.empty();
        }

        Customer newCustomer = mapSignupDtoToCustomer(signupDto);
        return Optional.of(
                customerRepository.save(newCustomer)
        );

    }

    private Customer mapSignupDtoToCustomer(SignupDto signupDto)
    {
        return
                new Customer(
                        signupDto.getUsername(),
                        passwordEncoder.encode(signupDto.getPassword()),
                        signupDto.getEmail(),
                        signupDto.getFirstName(),
                        signupDto.getLastName()
                );
    }
}

