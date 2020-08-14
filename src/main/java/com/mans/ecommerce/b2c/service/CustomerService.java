package com.mans.ecommerce.b2c.service;

import java.util.List;
import java.util.Optional;

import com.mans.ecommerce.b2c.controller.utills.dto.SignupDto;
import com.mans.ecommerce.b2c.domain.entity.customer.Cart;
import com.mans.ecommerce.b2c.domain.entity.customer.Customer;
import com.mans.ecommerce.b2c.domain.entity.customer.subEntity.Address;
import com.mans.ecommerce.b2c.domain.exception.ResourceNotFoundException;
import com.mans.ecommerce.b2c.domain.exception.UserAlreadyExistException;
import com.mans.ecommerce.b2c.repository.customer.CustomerRepository;
import com.mans.ecommerce.b2c.utill.Global;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authentication.AuthenticationManager;
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

    private ApplicationEventPublisher publisher;

    private CartService cartService;

    public CustomerService(
            CustomerRepository customerRepository,
            CartService cartService,
            PasswordEncoder passwordEncoder,
            ApplicationEventPublisher publisher)
    {
        this.customerRepository = customerRepository;
        this.cartService = cartService;
        this.passwordEncoder = passwordEncoder;
        this.publisher = publisher;
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

    public Mono<Customer> signup(SignupDto signupDto, ServerHttpRequest req)
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

    public Mono<Customer> findToLogin(String username)
    {
        return customerRepository.findToLogin(username);
    }

    private Mono<ObjectId> getCustomerId(ServerHttpRequest req)
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

    private Customer mapSignupDtoToCustomer(ObjectId id, SignupDto signupDto)
    {
        String encodedPassword = passwordEncoder.encode(signupDto.getPassword());
        return Customer
                       .builder()
                       .id(id)
                       .username(signupDto.getUsername())
                       .password(encodedPassword)
                       .email(signupDto.getEmail())
                       .firstName(signupDto.getFirstName())
                       .lastName(signupDto.getLastName())
                       .build();
    }
}

