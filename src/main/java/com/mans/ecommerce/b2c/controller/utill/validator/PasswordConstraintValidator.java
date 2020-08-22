package com.mans.ecommerce.b2c.controller.utill.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.List;

import com.mans.ecommerce.b2c.controller.utill.annotation.ValidPassword;
import org.passay.*;

public class PasswordConstraintValidator extends Validator implements ConstraintValidator<ValidPassword, String>
{

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context)
    {

        if (password == null || password.isEmpty())
        {
            String errorMessage = "must not be empty";
            buildConstraintValidatorContext(context, errorMessage);
            return false;
        }

        PasswordValidator validator = new PasswordValidator(
                Arrays.asList(
                        new LengthRule(8, 32),
                        new CharacterRule(EnglishCharacterData.UpperCase, 1),
                        new CharacterRule(EnglishCharacterData.LowerCase, 1),
                        new CharacterRule(EnglishCharacterData.Digit, 1),
                        new CharacterRule(EnglishCharacterData.Special, 1),
                        new WhitespaceRule()
                ));

        RuleResult result = validator.validate(new PasswordData(password));
        if (result.isValid())
        {
            return true;
        }

        List<String> errors = validator.getMessages(result);
        String allErrorMessages = joinErrorMessages(errors);
        buildConstraintValidatorContext(context, allErrorMessages);

        return false;
    }

}
