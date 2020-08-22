package com.mans.ecommerce.b2c.controller.utill.annotation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

import com.mans.ecommerce.b2c.controller.utill.validator.UsernameConstraintValidator;

@Documented
@Constraint(validatedBy = UsernameConstraintValidator.class)
@Target({ ElementType.FIELD, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidUsername
{
    String message() default "Invalid Username";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
