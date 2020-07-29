package com.mans.ecommerce.b2c.security;

import java.util.Optional;

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

    private JwtProvider jwtProvider;

    private final String ROLE = "user";

    protected CustomerDetailsService(
            CustomerRepository customerRepository,
            JwtProvider jwtProvider)
    {
        this.customerRepository = customerRepository;
        this.jwtProvider = jwtProvider;
    }

    @Override public UserDetails loadUserByUsername(String username)
    {
        Customer customer = customerRepository.findByUsername(username)
                                              .orElseThrow(() -> new UsernameNotFoundException(
                                                      String.format("User with name %s does not exist", username)
                                              ));

        //org.springframework.security.core.userdetails.User.withUsername() builder
        return withUsername(customer.getUsername())
                       .password(customer.getPassword())
                       .authorities(ROLE)
                       .accountExpired(false)
                       .accountLocked(false)
                       .credentialsExpired(false)
                       .disabled(false)
                       .build();
    }

    /**
     * Extract username and roles from a validated jwt string.
     *
     * @param jwtToken jwt string
     * @return UserDetails if valid, Empty otherwise
     */
    public Optional<UserDetails> loadUserByJwtToken(String jwtToken)
    {
        if (jwtProvider.isValidToken(jwtToken))
        {
            return Optional.of(
                    withUsername(jwtProvider.getUsername(jwtToken))
                            .authorities(jwtProvider.getRoles(jwtToken))
                            .password("") //token does not have password but field may not be empty
                            .accountExpired(false)
                            .accountLocked(false)
                            .credentialsExpired(false)
                            .disabled(false)
                            .build());
        }
        return Optional.empty();
    }

    /**
     * Extract the username from the JWT then lookup the user in the database.
     *
     * @param jwtToken
     * @return
     */
    public Optional<UserDetails> loadUserByJwtTokenAndDatabase(String jwtToken)
    {
        if (jwtProvider.isValidToken(jwtToken))
        {
            return Optional.of(loadUserByUsername(jwtProvider.getUsername(jwtToken)));
        }
        else
        {
            return Optional.empty();
        }
    }
}
