package com.mans.ecommerce.b2c.controller.customer;

import javax.validation.*;
import java.util.Objects;
import java.util.Set;

import com.mans.ecommerce.b2c.controller.utills.dto.LoginDto;
import com.mans.ecommerce.b2c.controller.utills.dto.SignupDto;
import com.mans.ecommerce.b2c.domain.entity.customer.Customer;
import com.mans.ecommerce.b2c.domain.exception.LoginException;
import com.mans.ecommerce.b2c.security.jwt.JWTReactiveAuthenticationManager;
import com.mans.ecommerce.b2c.security.jwt.JWTToken;
import com.mans.ecommerce.b2c.security.jwt.TokenProvider;
import com.mans.ecommerce.b2c.service.CustomerService;
import com.mans.ecommerce.b2c.utill.Global;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/auths")
public class AuthController
{

    private TokenProvider tokenProvider;

    private JWTReactiveAuthenticationManager authenticationManager;

    private Validator validation;

    private CustomerService customerService;

    public AuthController(CustomerService customerService,
                          TokenProvider tokenProvider,
                          JWTReactiveAuthenticationManager authenticationManager,
                          Validator validation)
    {
        this.customerService = customerService;
        this.tokenProvider = tokenProvider;
        this.authenticationManager = authenticationManager;
        this.validation = validation;
    }

    @PostMapping("/signin")
    public Mono<JWTToken> login(@RequestBody LoginDto loginDto)
    {
        if (!isValid(loginDto))
        {
            return Mono.error(new LoginException());
        }

        Authentication authenticationToken =
                new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword());

        Mono<Authentication> authentication = this.authenticationManager.authenticate(authenticationToken);
        authentication.doOnError(throwable -> {
            throw new BadCredentialsException("Bad crendentials");
        });

        ReactiveSecurityContextHolder.withAuthentication(authenticationToken);

        return authentication.map(auth -> {
            String jwt = tokenProvider.createToken(auth);
            return new JWTToken(jwt);
        });
    }

    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Customer> signup(
            @RequestBody @Valid SignupDto signupDto,
            ServerHttpRequest req,
            ServerHttpResponse res)
    {
        Mono<Customer> customerMono = customerService.signup(signupDto, req);
        customerMono.doOnSuccess(customer -> {
            if (!Objects.isNull(customer))
            {
                String id = customer.getId();
                Global.setId(res, id);
            }
        });

        return customerMono;
    }

    private boolean isValid(LoginDto loginDto)
    {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<Object>> violations = validator.validate(loginDto);

        return violations.size() == 0;
    }

    //TODO Reset Password

}
