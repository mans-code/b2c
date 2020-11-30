package com.mans.ecommerce.b2c.controller.utill.dto;

import com.mans.ecommerce.b2c.controller.utill.annotation.ValidPassword;
import com.mans.ecommerce.b2c.controller.utill.annotation.ValidUsername;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString(exclude = { "password" })
@Schema(description = "login details")
public class LoginDto
{
    @ValidUsername
    @Schema(description = "username should be between 4 and 32 characters")
    private String username;

    @ValidPassword
    @Schema(description = "password should be between 8 and 32 characters. "
                                      + "Also must contains at least one character of the following"
                                      + "1 Digit, "
                                      + "1 uppercase, "
                                      + "1 lowercase")
    private String password;

}

