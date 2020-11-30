package com.mans.ecommerce.b2c.controller.customer;

import javax.validation.*;
import java.util.Set;

import com.mans.ecommerce.b2c.controller.utill.dto.LoginDto;
import com.mans.ecommerce.b2c.controller.utill.dto.SignupDto;
import com.mans.ecommerce.b2c.domain.entity.customer.Customer;
import com.mans.ecommerce.b2c.security.jwt.JWTReactiveAuthenticationManager;
import com.mans.ecommerce.b2c.security.jwt.JWTToken;
import com.mans.ecommerce.b2c.security.jwt.TokenProvider;
import com.mans.ecommerce.b2c.service.CustomerService;
import com.mans.ecommerce.b2c.utill.Global;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "authentication api", description = "auth rest api to authenticate or register customers")
public class AuthController
{

    private TokenProvider tokenProvider;

    private JWTReactiveAuthenticationManager authenticationManager;

    private Validator validation;

    private CustomerService customerService;

    public AuthController(
            CustomerService customerService,
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
    @Operation(description = "sigin operation")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200" , description = "successful operation",  content = @Content(schema = @Schema(implementation = JWTToken.class))),
            @ApiResponse(responseCode = "401" , description = "Bad credentials")
    })
    public Mono<JWTToken> login(@RequestBody LoginDto loginDto)
    {
        if (!isValid(loginDto))
        {
            return Mono.error(new BadCredentialsException("Bad credentials"));
        }

        Authentication authenticationToken =
                new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword());

        Mono<Authentication> authentication = this.authenticationManager.authenticate(authenticationToken);

        ReactiveSecurityContextHolder.withAuthentication(authenticationToken);

        return authentication.map(auth -> {
            String jwt = tokenProvider.createToken(auth);
            return new JWTToken(jwt);
        });
    }

    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(description = "sigup operation")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200" , description = "successful operation", content = @Content(schema = @Schema(implementation = Customer.class))),
            @ApiResponse(responseCode = "200" , description = "Bad request infos", content = @Content(schema = @Schema(implementation = Customer.class))),
    })
    public Mono<Customer> signup(
            @RequestBody @Valid SignupDto signupDto,
            ServerHttpRequest req,
            ServerHttpResponse res)
    {

        return customerService.signup(signupDto, req)
                              .doOnSuccess(cust -> Global.setIdHeader(res, cust.getId()));
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
