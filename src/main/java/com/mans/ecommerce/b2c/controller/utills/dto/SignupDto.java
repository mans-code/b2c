package com.mans.ecommerce.b2c.controller.utills.dto;

import javax.validation.constraints.*;

import com.mans.ecommerce.b2c.controller.utills.annotation.ValidPassword;

public class SignupDto
{
    /*
        ASCII letters and digits, with hyphens,
        underscores and spaces as internal separators.
    */
    @Pattern(regexp = "/^[A-Za-z0-9]+(?:[ _-][A-Za-z0-9]+)*$/")
    @Size(min = 4, max = 32)
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

    protected SignupDto()
    {
    }

    public String getUsername()
    {
        return username;
    }

    public String getPassword()
    {
        return password;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getEmail()
    {
        return email;
    }

    public String getFirstName()
    {
        return firstName;
    }

    public String getLastName()
    {
        return lastName;
    }
}
