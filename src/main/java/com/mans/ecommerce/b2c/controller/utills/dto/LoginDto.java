package com.mans.ecommerce.b2c.controller.utills.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class LoginDto
{
    @NotNull
    @Size(min = 4, max = 32)
    private String username;

    @NotNull
    @Size(min = 6)
    private String password;

}

