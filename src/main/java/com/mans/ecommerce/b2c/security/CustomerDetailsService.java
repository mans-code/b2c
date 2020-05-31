package com.mans.ecommerce.b2c.security;

import com.mans.ecommerce.b2c.domain.entity.customer.Customer;
import com.mans.ecommerce.b2c.repository.customer.CustomerRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import static org.springframework.security.core.userdetails.User.withUsername;

@Component
public class CustomerDetailsService implements UserDetailsService
{

    private CustomerRepository customerRepository;

    protected CustomerDetailsService(CustomerRepository customerRepository)
    {
        this.customerRepository = customerRepository;
    }

    @Override public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException
    {
        Customer customer = customerRepository.findByUsername(username)
                                              .orElseThrow(() -> new UsernameNotFoundException(
                                                      String.format("User with name %s does not exist", username)
                                              ));

        //org.springframework.security.core.userdetails.User.withUsername() builder
        return withUsername(customer.getUsername())
                .password(customer.getPassword())
                .build();
    }
}
