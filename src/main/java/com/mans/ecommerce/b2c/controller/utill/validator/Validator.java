package com.mans.ecommerce.b2c.controller.utill.validator;

import javax.validation.ConstraintValidatorContext;
import java.util.List;
import java.util.stream.Collectors;

public class Validator
{
    protected void buildConstraintValidatorContext(ConstraintValidatorContext context, String messagesTemplate)
    {
        context.buildConstraintViolationWithTemplate(messagesTemplate)
               .addConstraintViolation()
               .disableDefaultConstraintViolation();
    }

    protected String joinErrorMessages(List<String> errorMessage)
    {
        return errorMessage
                       .stream()
                       .collect(Collectors
                                        .joining(","));
    }

    protected void validate()
    {
    }
}