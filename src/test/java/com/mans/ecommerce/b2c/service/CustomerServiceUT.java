package com.mans.ecommerce.b2c.service;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceUT
{
//    @InjectMocks
//    private CustomerService customerService;
//
//    @Mock
//    private CustomerRepository customerRepository;
//
//    @Mock
//    private AuthenticationManager authManager;
//
//    @Mock
//    private JwtProvider jwtProvider;
//
//    @Mock
//    private PasswordEncoder passwordEncoder;
//
//    private final String PASSWORD = "password";
//
//    @Test
//    public void signin_pass(@Mock LoginDto loginDto)
//    {
//        String tokenString = "token";
//        Token expected = new Token(tokenString);
//
//        when(authManager.authenticate(any()))
//                .thenReturn(null);
//
//        when(jwtProvider.createToken(any()))
//                .thenReturn(tokenString);
//
//        Token actual = customerService.signin(loginDto);
//
//        assertThat(expected.getToken(), equalToIgnoringCase(actual.getToken()));
//    }
//
//    @Test
//    public void signin_fail_loginException(@Mock LoginDto loginDto)
//    {
//        doThrow(RuntimeException.class)
//                .when(authManager)
//                .authenticate(any());
//
//        assertThrows(LoginException.class,
//                     () -> customerService.signin(loginDto));
//    }
//
//    @Test
//    public void signup_pass()
//    {
//        SignupDto signupDto = createSignupDto();
//        Customer customer = mapSignupDtoToCustomer(signupDto);
//        String encodedPassword = "encodedPassword";
//
//        when(customerRepository.existsByUsername(any()))
//                .thenReturn(false);
//
//        when(passwordEncoder.encode(any()))
//                .thenReturn(encodedPassword);
//
//        when(customerRepository.save(any()))
//                .thenAnswer(funParams -> (Customer) funParams.getArguments()[0]);
//
//        Customer actual = customerService.signup(signupDto, req);
//        assertSignupResult(actual, signupDto, encodedPassword);
//    }
//
//    @Test
//    public void getToken_pass()
//    {
//        String expected = "expectedToken";
//
//        when(jwtProvider.createToken(any()))
//                .thenReturn(expected);
//
//        Token actualToken = customerService.getToken(new String());
//
//        assertThat(actualToken.getToken(), equalTo(expected));
//    }
//
//    @Test
//    public void signup_fail_usernameAlreadyExists()
//    {
//        when(customerRepository.existsByUsername(any()))
//                .thenReturn(true);
//
//        assertThrows(UserAlreadyExistException.class,
//                     () -> customerService.signup(new SignupDto(), req));
//    }
//
//    private void assertSignupResult(Customer customer, SignupDto signupDto, String encodedPassword)
//    {
//        assertThat(customer.getUsername(), equalTo(signupDto.getUsername()));
//        assertThat(customer.getPassword(), equalTo(encodedPassword));
//        assertThat(customer.getFirstName(), equalTo(signupDto.getFirstName()));
//        assertThat(customer.getLastName(), equalTo(signupDto.getLastName()));
//        assertThat(customer.getEmail(), equalTo(signupDto.getEmail()));
//    }
//
//    private Customer mapSignupDtoToCustomer(SignupDto signupDto)
//    {
//        return Customer
//                       .builder()
//                       .username(signupDto.getUsername())
//                       .lastName(signupDto.getLastName())
//                       .firstName(signupDto.getFirstName())
//                       .email(signupDto.getEmail())
//                       .password(signupDto.getPassword())
//                       .build();
//    }
//
//    private SignupDto createSignupDto()
//    {
//        return SignupDto
//                       .builder()
//                       .username("username")
//                       .firstName("mans")
//                       .lastName("saad")
//                       .email("mans@mans.com")
//                       .password("password")
//                       .build();
//    }
}
