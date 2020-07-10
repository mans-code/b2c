package com.mans.ecommerce.b2c.controller.utills.dto;

import javax.validation.constraints.*;

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
    /*
        ASCII letters and digits, with hyphens,
        underscores and spaces as internal separators.
    */
    @Pattern(regexp = "^[a-z0-9_-]{4,15}$")
    private String username;

    @ValidPassword
    private String password;

    @Email
    @NotEmpty
    private String email;

    @NotNull
    @Size(min = 4, max = 32)
    private String firstName;

    @NotNull
    @Size(min = 4, max = 32)
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
