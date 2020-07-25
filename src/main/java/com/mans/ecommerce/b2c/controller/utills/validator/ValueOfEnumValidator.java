package com.mans.ecommerce.b2c.controller.utills.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.mans.ecommerce.b2c.controller.utills.annotation.ValueOfEnum;

public class ValueOfEnumValidator extends Validator implements ConstraintValidator<ValueOfEnum, CharSequence>
{
    private final String NOT_EMPTY = "must not be empty";

    private final String NOT_FOUND_TEMPLATE = "the value must be in %s";

    private String NOT_FOUND;

    private List<String> acceptedValues;

    @Override
    public void initialize(ValueOfEnum annotation)
    {
        acceptedValues = Stream.of(annotation.enumClass().getEnumConstants())
                               .map(Enum::name)
                               .collect(Collectors.toList());

        NOT_FOUND = String.format(NOT_FOUND_TEMPLATE, acceptedValues);
    }

    @Override
    public boolean isValid(CharSequence value, ConstraintValidatorContext context)
    {
        if (value == null)
        {
            buildConstraintValidatorContext(context, NOT_EMPTY);
            return false;
        }

        boolean found = acceptedValues.contains(value.toString());
        if (!found)
        {
            buildConstraintValidatorContext(context, NOT_FOUND);
        }

        return true;
    }
}