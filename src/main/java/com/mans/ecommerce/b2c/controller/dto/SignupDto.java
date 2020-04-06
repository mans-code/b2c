package com.mans.ecommerce.b2c.controller.dto;

import javax.validation.constraints.*;
import javax.xml.bind.annotation.XmlInlineBinaryData;

import com.mans.ecommerce.b2c.domain.annotation.ValidPassword;

public class SignupDto
{

    @NotEmpty
    @Min(4)
    @Max(32)
    private String username;

    @ValidPassword
    private String password;

    @Email
    @NotEmpty
    private String email;

    @NotEmpty
    @Min(3)
    @Max(32)
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
