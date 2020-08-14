package com.mans.ecommerce.b2c.security;

import java.util.Arrays;
import java.util.Objects;

import com.mans.ecommerce.b2c.domain.entity.customer.Customer;
import com.mans.ecommerce.b2c.repository.customer.CustomerRepository;
import com.mans.ecommerce.b2c.service.CustomerService;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 * @author duc-d
 */
@Component
public class ReactiveUserDetailsServiceImpl implements ReactiveUserDetailsService
{

    private final CustomerRepository customerRepository;

    public ReactiveUserDetailsServiceImpl(CustomerRepository customerRepository)
    {
        this.customerRepository = customerRepository;
    }

    @Override
    public Mono<UserDetails> findByUsername(String username)
    {
        return customerRepository.findToLogin(username)
                              .filter(Objects::nonNull)
                              .switchIfEmpty(Mono.error(new BadCredentialsException(
                                      String.format("User %s not found in database", username))))
                              .map(this::createSpringSecurityUser);
    }

    private User createSpringSecurityUser(Customer customer)
    {
        return new User(customer.getUsername(),
                        customer.getPassword(),
                        Arrays.asList(new Authority()));
    }

    private class Authority implements GrantedAuthority
    {

        @Override public String getAuthority()
        {
            return AuthoritiesConstants.USER;
        }
    }
}
