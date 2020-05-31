package com.mans.ecommerce.b2c.controller.utills.dto;

import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class LoginDto
{
    @NotNull
    private String username;

    @NotNull
    private String password;
}

