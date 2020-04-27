package com.mans.ecommerce.b2c.controller.utills.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.mans.ecommerce.b2c.controller.utills.dto.annotation.ValidPassword;
import com.sun.istack.internal.NotNull;

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
    private String name;

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

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }
}
