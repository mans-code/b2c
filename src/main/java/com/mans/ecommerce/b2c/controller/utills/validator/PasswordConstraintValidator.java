package com.mans.ecommerce.b2c.controller.utills.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.mans.ecommerce.b2c.controller.utills.annotation.ValidPassword;
import org.passay.*;

public class PasswordConstraintValidator implements ConstraintValidator<ValidPassword, String>
{
    @Override
    public void initialize(ValidPassword constraintAnnotation)
    {

    }

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context)
    {

        PasswordValidator validator = new PasswordValidator(
                Arrays.asList(
                        new LengthRule(8, 30),
                        new CharacterRule(EnglishCharacterData.UpperCase, 1),
                        new CharacterRule(EnglishCharacterData.LowerCase, 1),
                        new CharacterRule(EnglishCharacterData.Digit, 1),
                        new CharacterRule(EnglishCharacterData.Special, 1),
                        new WhitespaceRule()));

        RuleResult result = validator.validate(new PasswordData(password));
        if (result.isValid())
        {
            return true;
        }

        //we aggregated all the failed condition's error messages in a String separated
        // by "," and then put it into the context and returned false.

        List<String> messages = validator.getMessages(result);
        String messageTemplate = messages.stream()
                .collect(Collectors.joining(","));
        context.buildConstraintViolationWithTemplate(messageTemplate)
               .addConstraintViolation()
               .disableDefaultConstraintViolation();

        return false;
    }
}
