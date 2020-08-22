package com.mans.ecommerce.b2c.e2e;

import com.mans.ecommerce.b2c.controller.utill.dto.LoginDto;
import com.mans.ecommerce.b2c.controller.utill.dto.SignupDto;
import com.mans.ecommerce.b2c.domain.entity.customer.Customer;
import com.mans.ecommerce.b2c.domain.exception.UserAlreadyExistException;
import com.mans.ecommerce.b2c.security.jwt.JWTToken;
import com.mans.ecommerce.b2c.utills.Global;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
public class AuthControllerIT
{

    private final String VALID_DB_USERNAME = "valid";

    private final String VALID_PASSWORD = "Password1@";

    private final String INVALID_USERNAME = "invalid";

    private final String INVALID_PASSWORD = "Passworf4&";

    private final String BASE_URL = "/auths";

    private final String SIGNIN_PATH = "/signin";

    private final String SIGNUP_PATH = "/signup";

    @Autowired
    private WebTestClient webTestClient;

    @Test
    public void signin_pass()
    {
        LoginDto login = new LoginDto(VALID_DB_USERNAME, VALID_PASSWORD);
        webTestClient.post()
                     .uri(BASE_URL + SIGNIN_PATH)
                     .body(Mono.just(login), LoginDto.class)
                     .accept(Global.JSON)
                     .exchange()
                     .expectStatus().is2xxSuccessful()
                     .expectBody(JWTToken.class)
                     .consumeWith(res -> {
                         JWTToken token = res.getResponseBody();
                         assertThat(token.getToken(), is(not(emptyString())));
                     });
    }

    @Test
    public void signin_fail_UsernameNotFound()
    {
        LoginDto login = new LoginDto(INVALID_USERNAME, VALID_PASSWORD);
        badCredentialsSignin(login);
    }

    @Test
    public void signin_fail_PasswordNotFound()
    {
        LoginDto login = new LoginDto(VALID_DB_USERNAME, INVALID_PASSWORD);
        badCredentialsSignin(login);
    }

    @Test
    public void signup_pass()
            throws Exception
    {
        SignupDto signupDto = validSignupDtoInstence();

        webTestClient.post()
                     .uri(BASE_URL + SIGNUP_PATH)
                     .body(Mono.just(signupDto), SignupDto.class)
                     .accept(Global.JSON)
                     .exchange()
                     .expectStatus().isCreated()
                     .expectBody(Customer.class)
                     .consumeWith(res -> {
                         Customer actual = res.getResponseBody();
                         assertThat(actual.getId(), not(emptyOrNullString()));
                         assertTrue(sameValues(actual, signupDto));
                     });
    }

    @Test
    public void signup_failed_userAlreadyExists()
    {
        SignupDto signupDto = validSignupDtoInstence();
        signupDto.setUsername(VALID_DB_USERNAME);

        webTestClient.post()
                     .uri(BASE_URL + SIGNUP_PATH)
                     .body(Mono.just(signupDto), SignupDto.class)
                     .accept(Global.JSON)
                     .exchange()
                     .expectStatus().isEqualTo(HttpStatus.CONFLICT.value())
                     .expectBody(UserAlreadyExistException.class)
                     .consumeWith(res -> {
                         UserAlreadyExistException ex = res.getResponseBody();
                         assertThat(ex.getMessage(), equalToIgnoringCase("User Already Exists"));
                     });
    }

    private void badCredentialsSignin(LoginDto login)
    {
        webTestClient.post()
                     .uri(BASE_URL + SIGNIN_PATH)
                     .body(Mono.just(login), LoginDto.class)
                     .accept(Global.JSON)
                     .exchange()
                     .expectStatus().isUnauthorized()
                     .expectBody(BadCredentialsException.class)
                     .consumeWith(res -> {
                         BadCredentialsException ex = res.getResponseBody();
                         assertThat(ex.getMessage(), equalToIgnoringCase("Invalid Credentials"));
                     });
    }

    private SignupDto validSignupDtoInstence()
    {
        return SignupDto
                       .builder()
                       .username("mans_code")
                       .password(VALID_PASSWORD)
                       .email("mans@mans.com")
                       .firstName("mans")
                       .lastName("alzahrani")
                       .build();
    }

    private boolean sameValues(Customer actual, SignupDto signupDto)
    {
        return actual.getUsername().equals(signupDto.getUsername())
                       &&
                       actual.getEmail().equals(signupDto.getEmail())
                       &&
                       actual.getFirstName().equals(signupDto.getFirstName())
                       &&
                       actual.getLastName().equals(signupDto.getLastName());
    }
}
