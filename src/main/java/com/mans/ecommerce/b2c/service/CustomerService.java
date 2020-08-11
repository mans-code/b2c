package com.mans.ecommerce.b2c.service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

import com.mans.ecommerce.b2c.controller.utills.dto.LoginDto;
import com.mans.ecommerce.b2c.controller.utills.dto.SignupDto;
import com.mans.ecommerce.b2c.domain.entity.customer.Cart;
import com.mans.ecommerce.b2c.domain.entity.customer.Customer;
import com.mans.ecommerce.b2c.domain.entity.customer.subEntity.Address;
import com.mans.ecommerce.b2c.domain.exception.LoginException;
import com.mans.ecommerce.b2c.domain.exception.ResourceNotFoundException;
import com.mans.ecommerce.b2c.domain.exception.UserAlreadyExistException;
import com.mans.ecommerce.b2c.repository.customer.CustomerRepository;
import com.mans.ecommerce.b2c.security.JwtProvider;
import com.mans.ecommerce.b2c.utill.Global;
import com.mans.ecommerce.b2c.utill.response.Token;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class CustomerService
{
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerService.class);

    private CustomerRepository customerRepository;

    private AuthenticationManager authenticationManager;

    private PasswordEncoder passwordEncoder;

    private JwtProvider jwtProvider;

    private ApplicationEventPublisher publisher;

    private CartService cartService;

    public CustomerService(
            CustomerRepository customerRepository,
            AuthenticationManager authenticationManager,
            PasswordEncoder passwordEncoder,
            ApplicationEventPublisher publisher,
            JwtProvider jwtProvider)
    {
        this.customerRepository = customerRepository;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.jwtProvider = jwtProvider;
        this.publisher = publisher;
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

    public Mono<Customer> signup(SignupDto signupDto, HttpServletRequest req)
    {
        Mono<ObjectId> idMono = getCustomerId(req);
        Mono<Customer> customerMono = idMono.flatMap(id -> {
            Customer newCustomer = mapSignupDtoToCustomer(id, signupDto);
            return customerRepository.save(newCustomer);
        });

        return customerMono.doOnSuccess(customer -> { //TODO error or Success test
            if (customer == null)
            {
                throw new UserAlreadyExistException();
            }
        });
    }

    public Token getToken(String username)
    {
        String tokenString = jwtProvider.createToken(username);
        return new Token(tokenString);
    }

    public Mono<List<Address>> getShippingAddresses(ObjectId customerId)
    {
        Mono<Customer> customerMono = customerRepository.getShippingAddresses(customerId);

        customerMono.doOnSuccess(customer -> {
            if (customer == null)
            {
                throw new ResourceNotFoundException("customer not found");
            }
        });

        return customerMono.flatMap(customer -> Mono.just(customer.getShippingAddresses()));
    }

    public Address getDefaultShippingAddress(ObjectId customerId)
    {
        //TODO
        // customerRepository.getDefaultShippingAddress(customerId)
        return null;
    }

    private Mono<ObjectId> getCustomerId(HttpServletRequest req)
    {
        Optional<String> idOptional = Global.getId(req);
        if (idOptional.isPresent())
        {
            ObjectId id = new ObjectId(idOptional.get());
            return Mono.just(id);
        }
        Mono<Cart> cartMono = cartService.create(false);
        return cartMono.flatMap(cart -> Mono.just(cart.getIdObj()));
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

    private Customer mapSignupDtoToCustomer(ObjectId id, SignupDto signupDto)
    {
        String encodedPassword = passwordEncoder.encode(signupDto.getPassword());
        String token = getToken(signupDto.getUsername()).getToken();
        return Customer
                       .builder()
                       .id(id)
                       .username(signupDto.getUsername())
                       .password(encodedPassword)
                       .email(signupDto.getEmail())
                       .firstName(signupDto.getFirstName())
                       .lastName(signupDto.getLastName())
                       .token(token)
                       .build();
    }
}

