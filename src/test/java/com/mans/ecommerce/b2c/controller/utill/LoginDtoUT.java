package com.mans.ecommerce.b2c.controller.utill;

import com.mans.ecommerce.b2c.controller.utill.dto.LoginDto;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class LoginDtoUT
{
    // TODO USEeNAME test
    private final String USERNAME_FIELD = "username";

    private final String PASSWORD_FIELD = "password";

    private static String validUsername;

    private static String validPassword;

    private static ValidatorTestHelper validatorTestHelper;

    @BeforeAll
    public static void beforeClass()
    {
        validatorTestHelper = new ValidatorTestHelper();
        validUsername = validatorTestHelper.getValidUsername();
        validPassword = validatorTestHelper.getValidPassword();
    }

    @Test
    public void validation_pass()
    {
        LoginDto loginDto = new LoginDto(validUsername, validPassword);
        validatorTestHelper.validation_pass(loginDto);
    }

    @Test
    public void validation_fail_invalidUsername_TooShort()
    {
        int minCharacterSize = 4;
        String shortUsername = validatorTestHelper.getStringOfLength(minCharacterSize - 1);
        LoginDto loginDto = new LoginDto(shortUsername, validPassword);

        validatorTestHelper.validation_fail_TooShort(loginDto, USERNAME_FIELD, minCharacterSize);
    }

    @Test
    public void validation_fail_invalidUsername_TooLong()
    {
        int maxCharacterSize = 32;
        String longUsername = validatorTestHelper.getStringOfLength(maxCharacterSize + 1);
        LoginDto loginDto = new LoginDto(longUsername, validPassword);

        validatorTestHelper.validation_fail_TooLong(loginDto, USERNAME_FIELD, maxCharacterSize);
    }

    @Test
    public void validation_fail_invalidUsername_Null()
    {
        String nullUsername = validatorTestHelper.getNullString();
        LoginDto loginDto = new LoginDto(nullUsername, validPassword);
        validatorTestHelper.validation_fail_Null(loginDto, USERNAME_FIELD);
    }

    @Test
    public void validation_fail_invalidUsername_startWithDigits()
    {
        //TODOfail();
        //String startWithDigitsUsername = validatorTestHelper.getStartWithDigits();
        //        LoginDto loginDto = new LoginDto(startWithDigitsUsername, validPassword);
        //        validatorTestHelper.validation_fail_Null(loginDto, USERNAME_FIELD);
    }

    @Test
    public void validation_fail_invalidPassword_TooShort()
    {
        int minCharacterSize = 8;
        String shortPassword = validatorTestHelper.getStringOfLength(minCharacterSize - 1);
        LoginDto loginDto = new LoginDto(validUsername, shortPassword);
        validatorTestHelper.validation_fail_TooShort(loginDto, PASSWORD_FIELD, minCharacterSize);
    }

    @Test
    public void validation_fail_invalidPassword_TooLong()
    {
        int maxCharacterSize = 32;
        String longPassword = validatorTestHelper.getStringOfLength(maxCharacterSize + 1);
        LoginDto loginDto = new LoginDto(validUsername, longPassword);

        validatorTestHelper.validation_fail_TooLong(loginDto, PASSWORD_FIELD, maxCharacterSize);
    }

    @Test
    public void validation_fail_invalidPassword_Null()
    {
        String nullPassword = validatorTestHelper.getNullString();
        LoginDto loginDto = new LoginDto(validUsername, nullPassword);
        validatorTestHelper.validation_fail_Null(loginDto, PASSWORD_FIELD);
    }

    @Test
    public void validation_fail_invalidPassword_noCapitalLetters()
    {
        String noCapitalLettersPassword = validatorTestHelper.getNoCapitalLettersString();
        LoginDto loginDto = new LoginDto(validUsername, noCapitalLettersPassword);
        validatorTestHelper.validation_fail_noCapitalLetters(loginDto, PASSWORD_FIELD);
    }

    @Test
    public void validation_fail_invalidPassword_noDigits()
    {
        String noDigitsPassword = validatorTestHelper.getNoDigitsString();
        LoginDto loginDto = new LoginDto(validUsername, noDigitsPassword);
        validatorTestHelper.validation_fail_noDigits(loginDto, PASSWORD_FIELD);
    }

    @Test
    public void validation_fail_invalidPassword_noSymbols()
    {
        String password_noSpecialLettersString = validatorTestHelper.getNoSpecialLettersString();
        LoginDto loginDto = new LoginDto(validUsername, password_noSpecialLettersString);
        validatorTestHelper.validation_fail_noSpecials(loginDto, PASSWORD_FIELD);
    }

    @Test
    public void validation_fail_invalidPassword_whiteSpaces()
    {
        String password_whiteSpaces = validatorTestHelper.getWithWhiteSpacesString();
        LoginDto loginDto = new LoginDto(validUsername, password_whiteSpaces);
        validatorTestHelper.validation_fail_whiteSpaces(loginDto, PASSWORD_FIELD);
    }

}
