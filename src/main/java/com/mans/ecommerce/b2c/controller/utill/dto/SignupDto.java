package com.mans.ecommerce.b2c.controller.utill.dto;

import javax.validation.constraints.*;

import com.mans.ecommerce.b2c.controller.utill.annotation.ValidPassword;
import com.mans.ecommerce.b2c.controller.utill.annotation.ValidUsername;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString()
public class SignupDto
{

    @ValidUsername
    private String username;

    @ValidPassword
    private String password;

    @NotBlank(message = "must not be empty")
    @Email(message = "must be a valid email")
    private String email;

    @NotBlank(message = "must not be empty")
    @Size(min = 2, message = "must be 2 or more characters in length")
    @Size(max = 32, message = "must be no more than 32 characters in length")
    @Pattern(regexp = "^[a-zA-Z]+$", message = "must only be characters")
    private String firstName;

    @NotBlank(message = "must not be empty")
    @Size(min = 2, message = "must be 2 or more characters in length")
    @Size(max = 32, message = "must be no more than 32 characters in length")
    @Pattern(regexp = "^[a-zA-Z]+$", message = "must only be characters")
    private String lastName;

}
