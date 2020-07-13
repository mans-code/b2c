package com.mans.ecommerce.b2c.controller.utills.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;

import com.mans.ecommerce.b2c.controller.utills.annotation.ValidUsername;

public class UsernameConstraintValidator extends Validator implements ConstraintValidator<ValidUsername, String>
{
    @Override public boolean isValid(String username, ConstraintValidatorContext context)
    {

        UsernameValidator validator = new UsernameValidator(username);
        validator.validate();

        if (validator.isValid())
        {
            return true;
        }

        List<String> errors = validator.getMessages();
        String allErrorMessages = joinErrorMessages(errors);
        buildConstraintValidatorContext(context, allErrorMessages);

        return false;
    }
}
