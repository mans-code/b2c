package com.mans.ecommerce.b2c.controller.dto;

import javax.validation.constraints.NotNull;

public class LoginDto
{
    @NotNull
    private String username;

    @NotNull
    private String password;

    protected LoginDto()
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
}

