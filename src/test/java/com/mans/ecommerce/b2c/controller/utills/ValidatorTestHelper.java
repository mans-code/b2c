package com.mans.ecommerce.b2c.controller.utills;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;
import java.util.stream.IntStream;

import lombok.Getter;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@Getter
public class ValidatorTestHelper
{
    private final Validator validator;

    private final String validUsername;

    private final String validPassword;

    private final String validId;

    private final String validEmail;

    private final String inValidEmail;

    private final String nullString;

    private final String noCapitalLettersString;

    private final String noSpecialLettersString;

    private final String withWhiteSpacesString;

    private final String noDigitsString;

    private final String withDigitsString;

    private final String withSpecialsLettersString;

    public ValidatorTestHelper()
    {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();

        validId = "5eaa32339e58d82df4319992";
        validUsername = "valid";
        validPassword = "Valid@1234";
        validEmail = "mans@mans.com";
        inValidEmail = "ohoh.com.co";
        nullString = null;
        withDigitsString = "digits12";
        withSpecialsLettersString = "specialsLetters$%";
        noCapitalLettersString = "nocapitalletters";
        noSpecialLettersString = "noSymbols1";
        withWhiteSpacesString = "with White Spaces";
        noDigitsString = "noDigits";
    }

    public String getStringOfLength(int num)
    {
        char[] chars = new char[num];
        IntStream.range(0, num).forEach(i -> chars[i] = 'a');
        return new String(chars);
    }

    public void validation_pass(Object object)
    {
        int expectedViolations = 0;
        Set<ConstraintViolation<Object>> constraintViolations = validator.validate(object);
        assertEquals(expectedViolations, constraintViolations.size());
    }

    public void validation_fail_Null(Object object, String fieldToValidate)
    {
        String expectedErrorMessage = "must not be empty";
        assertErrorMessageContainGiven(expectedErrorMessage, fieldToValidate, object);
    }

    public void validation_fail_TooSmall(Object object, String fieldToValidate, int min)
    {
        String messageTemplate = "must be %d or more";
        String expectedErrorMessage = String.format(messageTemplate, min);
        assertErrorMessageContainGiven(expectedErrorMessage, fieldToValidate, object);
    }

    public void validation_fail_TooLarge(Object object, String fieldToValidate, int min)
    {
        String messageTemplate = "must be no more than %d";
        String expectedErrorMessage = String.format(messageTemplate, min);
        assertErrorMessageContainGiven(expectedErrorMessage, fieldToValidate, object);
    }

    public void validation_fail_TooShort(Object object, String fieldToValidate, int min)
    {
        String messageTemplate = "must be %d or more characters in length";
        String expectedErrorMessage = String.format(messageTemplate, min);
        assertErrorMessageContainGiven(expectedErrorMessage, fieldToValidate, object);
    }

    public void validation_fail_TooLong(Object object, String fieldToValidate, int max)
    {
        String messageTemplate = "must be no more than %d characters in length";
        String expectedErrorMessage = String.format(messageTemplate, max);
        assertErrorMessageContainGiven(expectedErrorMessage, fieldToValidate, object);
    }

    public void validation_fail_ContainsNoneAlphabet(Object object, String fieldToValidate)
    {
        String expectedErrorMessage = "must only be characters";
        assertErrorMessageContainGiven(expectedErrorMessage, fieldToValidate, object);
    }

    public void validation_fail_noCapitalLetters(Object object, String fieldToValidate)
    {
        String expectedErrorMessage = "must contain 1 or more uppercase characters";
        assertErrorMessageContainGiven(expectedErrorMessage, fieldToValidate, object);
    }

    public void validation_fail_noDigits(Object object, String fieldToValidate)
    {
        String expectedErrorMessage = "must contain 1 or more digit characters";
        assertErrorMessageContainGiven(expectedErrorMessage, fieldToValidate, object);
    }

    public void validation_fail_noSpecials(Object object, String fieldToValidate)
    {
        String expectedErrorMessage = "must contain 1 or more special characters";
        assertErrorMessageContainGiven(expectedErrorMessage, fieldToValidate, object);
    }

    public void validation_fail_whiteSpaces(Object object, String fieldToValidate)
    {
        String expectedErrorMessage = "contains a whitespace character";
        assertErrorMessageContainGiven(expectedErrorMessage, fieldToValidate, object);
    }

    public void validation_fail_invalidEmail(Object object, String fieldToValidate)
    {
        String expectedErrorMessage = "must be a valid email";
        assertErrorMessageContainGiven(expectedErrorMessage, fieldToValidate, object);
    }

    private void assertErrorMessageContainGiven(String expectedErrorMessage, String fieldToValidate, Object object)
    {
        Set<ConstraintViolation<Object>> constraintViolations = validator.validateProperty(object, fieldToValidate);
        String acutalErrorMessage = constraintViolations.iterator().next().getMessage();
        String failMessageTemplate = "validating filed=%s should return errorMessage=%s, however errorMessage=%s";
        String failMessage = String.format(failMessageTemplate,
                                           fieldToValidate,
                                           expectedErrorMessage,
                                           acutalErrorMessage);

        assertTrue(failMessage, acutalErrorMessage.contains(expectedErrorMessage));
    }
}
