package com.mans.ecommerce.b2c.controller.utill.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.mans.ecommerce.b2c.controller.utill.annotation.ValidPassword;
import com.mans.ecommerce.b2c.controller.utill.annotation.ValidUsername;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString()
@Schema(description = "signUp details")
public class SignupDto
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

    @NotBlank(message = "must not be empty")
    @Email(message = "must be a valid email")
    @Schema(description = "must be a valid email address")
    private String email;

    @NotBlank(message = "must not be empty")
    @Size(min = 2, message = "must be 2 or more characters in length")
    @Size(max = 32, message = "must be no more than 32 characters in length")
    @Pattern(regexp = "^[a-zA-Z]+$", message = "must only be characters")
    @Schema(description = "first name should be between 2 and 32 characters", minimum = "2", maximum = "32")
    private String firstName;

    @NotBlank(message = "must not be empty")
    @Size(min = 2, message = "must be 2 or more characters in length")
    @Size(max = 32, message = "must be no more than 32 characters in length")
    @Pattern(regexp = "^[a-zA-Z]+$", message = "must only be characters")
    @Schema(description = "first name should be between 2 and 32 characters", minimum = "2", maximum = "32")
    private String lastName;

}
