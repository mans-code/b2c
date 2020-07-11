package com.mans.ecommerce.b2c.controller.utills.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

import com.mans.ecommerce.b2c.controller.utills.annotation.ValidPassword;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class SignupDto
{
    @Pattern(regexp = "^[a-z0-9_-]{4,15}$",
            message = "Length at least 3 characters and maximum of 15, and only contain a-z, 0-9, underscore, or hyphen")
    private String username;

    @ValidPassword
    private String password;

    @NotEmpty
    @Email(message = "The given email must be a valid email")
    private String email;

    @Pattern(regexp = "^[a-zA-Z]{2,32}$", message = "First Name: must be between 2 and 32 characters long")
    // TODO what about other languages
    private String firstName;

    @Pattern(regexp = "^[a-zA-Z]{2,32}$", message = "Last Name: must be between 2 and 32 characters long")
    private String lastName;

    public SignupDto(
            String username,
            String password,
            String email,
            String firstName,
            String lastName)
    {
        this.username = username;
        this.password = password;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
    }
}
