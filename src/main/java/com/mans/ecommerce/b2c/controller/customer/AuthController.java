package com.mans.ecommerce.b2c.controller.customer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.*;
import java.util.Objects;
import java.util.Set;

import com.mans.ecommerce.b2c.controller.utills.dto.LoginDto;
import com.mans.ecommerce.b2c.controller.utills.dto.SignupDto;
import com.mans.ecommerce.b2c.domain.entity.customer.Customer;
import com.mans.ecommerce.b2c.domain.exception.LoginException;
import com.mans.ecommerce.b2c.service.CustomerService;
import com.mans.ecommerce.b2c.utill.Global;
import com.mans.ecommerce.b2c.utill.response.Token;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/auths")
public class AuthController
{

    private CustomerService customerService;

    public AuthController(CustomerService customerService)
    {
        this.customerService = customerService;
    }

    @PostMapping("/signin")
    public Token login(@RequestBody LoginDto loginDto)
    {
        if (!isValid(loginDto))
        {
            throw new LoginException();
        }
        return customerService.signin(loginDto);
    }

    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Customer> signup(
            @RequestBody @Valid SignupDto signupDto,
            HttpServletRequest req,
            HttpServletResponse httpRes)
    {
        Mono<Customer> customerMono = customerService.signup(signupDto, req);
        customerMono.doOnSuccess(customer -> {
            if (!Objects.isNull(customer))
            {
                String id = customer.getId();
                Global.setId(httpRes, id);
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
