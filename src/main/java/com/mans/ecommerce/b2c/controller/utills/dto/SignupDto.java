package com.mans.ecommerce.b2c.controller.utills.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.mans.ecommerce.b2c.controller.utills.annotation.ValidPassword;
import com.mans.ecommerce.b2c.controller.utills.annotation.ValidUsername;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@ToString(exclude = {"password"})
public class SignupDto
{

    @ValidUsername
    private String username;

    @ValidPassword
    private String password;

    @NotEmpty
    @Email(message = "must be a valid email")
    private String email;

    @NotEmpty
    @Size(min = 2, message = "must be 2 or more characters in length")
    @Size(max = 32, message = "must be no more than 32 characters in length")
    @Pattern(regexp = "^[a-zA-Z]+$", message = "must only be characters")
    private String firstName;

    @NotEmpty
    @Size(min = 2, message = "must be 2 or more characters in length")
    @Size(max = 32, message = "must be no more than 32 characters in length")
    @Pattern(regexp = "^[a-zA-Z]+$", message = "must only be characters")
    private String lastName;

}
