package com.mans.ecommerce.b2c.controller.customer;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class AuthControllerUT
{

//    @InjectMocks
//    private AuthController authController;
//
//    @Mock
//    private CustomerService customerService;
//
//    private final String VALID_USERNAME = "valid";
//
//    private final String VALID_PASSWORD = "Password1@";
//
//    @Test
//    public void sign_pass()
//    {
//        Token expected = new Token("pass");
//        LoginDto loginDto = new LoginDto(VALID_USERNAME, VALID_PASSWORD);
//
//        when(customerService.signin(any()))
//                .thenReturn(expected);
//
//        Token actual = authController.login(loginDto);
//
//        assertSame(expected, actual);
//    }
//
//    @Test()
//    public void sign_fail_invalidUsernameOrPassword(@Mock LoginDto loginDto)
//    {
//        assertThrows(LoginException.class,
//                     () -> authController.login(loginDto));
//    }
//
//    @Test()
//    public void signin_fail_validationError(@Mock LoginDto loginDto)
//    {
//        assertThrows(LoginException.class,
//                     () -> authController.login(loginDto));
//
////        verify(customerService, never()).signup(any(), req);
//    }
//
//    @Test
//    public void signup_pass(@Mock SignupDto signupDto)
//    {
//
//        Customer customer = Customer.builder().id(new ObjectId("id")).build();
//        String expectedToken = "pass";
//        Token token = new Token(expectedToken);
//
//        when(customerService.signup(any(), req))
//                .thenReturn(customer);
//
//        when(customerService.getToken(any()))
//                .thenReturn(token);
//
//        NewCustomerResponse expected = authController.signup(signupDto);
//
//        //assertThat(expected.getCustomerId(), equalToIgnoringCase(customer.getId()));
//        assertThat(expected.getToken(), equalToIgnoringCase(expectedToken));
//    }
//
//    @Test()
//    public void signup_fail_userAlreadyExist()
//    {
//        when(customerService.signup(any(), req))
//                .thenThrow(UserAlreadyExistException.class);
//
//        assertThrows(UserAlreadyExistException.class,
//                     () -> authController.signup(new SignupDto()));
//
//    }

}
