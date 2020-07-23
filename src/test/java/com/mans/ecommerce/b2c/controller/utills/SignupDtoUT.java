package com.mans.ecommerce.b2c.controller.utills;

import com.mans.ecommerce.b2c.controller.utills.dto.SignupDto;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class SignupDtoUT
{
    private final String USERNAME_FIELD = "username";

    private final String PASSWORD_FIELD = "password";

    private final String EMAIL_FIELD = "email";

    private final String FIRST_NAME_FIELD = "firstName";

    private final String LAST_NAME_FIELD = "lastName";

    private static ValidatorTestHelper validatorTestHelper;

    @BeforeAll
    public static void beforeClass()
    {
        validatorTestHelper = new ValidatorTestHelper();
    }

    @Test
    public void validation_pass()
    {
        SignupDto signupDto = getVaildSignupDto();
        validatorTestHelper.validation_pass(signupDto);
    }

    @Test
    public void validation_fail_invalidEmail()
    {
        String invalidEmail = validatorTestHelper.getInValidEmail();
        SignupDto signupDto = SignupDto.builder().email(invalidEmail).build();
        validatorTestHelper.validation_fail_invalidEmail(signupDto, EMAIL_FIELD);
    }

    @Test
    public void validation_fail_invalidEmail_Null()
    {
        String nullEmail = validatorTestHelper.getNullString();
        SignupDto signupDto = SignupDto.builder().email(nullEmail).build();

        validatorTestHelper.validation_fail_Null(signupDto, EMAIL_FIELD);
    }

    @Test
    public void validation_fail_invalidLastName_Null()
    {
        String nullLastName = validatorTestHelper.getNullString();
        SignupDto signupDto = SignupDto.builder().lastName(nullLastName).build();
        validatorTestHelper.validation_fail_Null(signupDto, LAST_NAME_FIELD);
    }

    @Test
    public void validation_fail_invalidFirstName_Null()
    {
        String nullFirstName = validatorTestHelper.getNullString();
        SignupDto signupDto = SignupDto.builder().firstName(nullFirstName).build();

        validatorTestHelper.validation_fail_Null(signupDto, FIRST_NAME_FIELD);
    }

    @Test
    public void validation_fail_invalidFirstName_TooShort()
    {
        int minCharacterSize = 2;
        String shortFirstName = validatorTestHelper.getStringOfLength(minCharacterSize - 1);
        SignupDto signupDto = SignupDto.builder().firstName(shortFirstName).build();

        validatorTestHelper.validation_fail_TooShort(signupDto, FIRST_NAME_FIELD, minCharacterSize);
    }

    @Test
    public void validation_fail_invalidLastName_TooShort()
    {
        int minCharacterSize = 2;
        String shortLastName = validatorTestHelper.getStringOfLength(minCharacterSize - 1);
        SignupDto signupDto = SignupDto.builder().lastName(shortLastName).build();

        validatorTestHelper.validation_fail_TooShort(signupDto, LAST_NAME_FIELD, minCharacterSize);
    }

    @Test
    public void validation_fail_invalidFirstName_TooLong()
    {
        int maxCharacterSize = 32;
        String longFirstName = validatorTestHelper.getStringOfLength(maxCharacterSize + 1);
        SignupDto signupDto = SignupDto.builder().firstName(longFirstName).build();

        validatorTestHelper.validation_fail_TooLong(signupDto, FIRST_NAME_FIELD, maxCharacterSize);
    }

    @Test
    public void validation_fail_invalidLastName_TooLong()
    {
        int maxCharacterSize = 32;
        String longLastName = validatorTestHelper.getStringOfLength(maxCharacterSize + 1);
        SignupDto signupDto = SignupDto.builder().lastName(longLastName).build();

        validatorTestHelper.validation_fail_TooLong(signupDto, LAST_NAME_FIELD, maxCharacterSize);
    }

    @Test
    public void validation_fail_invalidFirstName_ContainsNoneAlphabet()
    {
        String firstNameWithDigits = validatorTestHelper.getWithDigitsString();
        String firstNameWithSpecial = validatorTestHelper.getWithSpecialsLettersString();

        SignupDto signupDtoDigits = SignupDto.builder().firstName(firstNameWithDigits).build();
        SignupDto signupDtoSpecials = SignupDto.builder().firstName(firstNameWithSpecial).build();

        validatorTestHelper.validation_fail_ContainsNoneAlphabet(signupDtoDigits, FIRST_NAME_FIELD);
        validatorTestHelper.validation_fail_ContainsNoneAlphabet(signupDtoSpecials, FIRST_NAME_FIELD);
    }

    @Test
    public void validation_fail_invalidLastName_ContainsNoneAlphabet()
    {
        String lastNameWithDigits = validatorTestHelper.getWithDigitsString();
        String lastNameWithSpecial = validatorTestHelper.getWithSpecialsLettersString();

        SignupDto signupDtoDigits = SignupDto.builder().lastName(lastNameWithDigits).build();
        SignupDto signupDtoSpecials = SignupDto.builder().lastName(lastNameWithSpecial).build();

        validatorTestHelper.validation_fail_ContainsNoneAlphabet(signupDtoDigits, LAST_NAME_FIELD);
        validatorTestHelper.validation_fail_ContainsNoneAlphabet(signupDtoSpecials, LAST_NAME_FIELD);
    }

    @Test
    public void validation_fail_invalidUsername_TooShort()
    {
        int minCharacterSize = 4;
        String shortUsername = validatorTestHelper.getStringOfLength(minCharacterSize - 1);
        SignupDto signupDto = SignupDto.builder().username(shortUsername).build();

        validatorTestHelper.validation_fail_TooShort(signupDto, USERNAME_FIELD, minCharacterSize);
    }

    @Test
    public void validation_fail_invalidUsername_TooLong()
    {
        int maxCharacterSize = 32;
        String longUsername = validatorTestHelper.getStringOfLength(maxCharacterSize + 2);
        SignupDto signupDto = SignupDto.builder().username(longUsername).build();

        validatorTestHelper.validation_fail_TooLong(signupDto, USERNAME_FIELD, maxCharacterSize);
    }

    @Test
    public void validation_fail_invalidUsername_Null()
    {
        String nullUsername = validatorTestHelper.getNullString();
        SignupDto signupDto = SignupDto.builder().username(nullUsername).build();

        validatorTestHelper.validation_fail_Null(signupDto, USERNAME_FIELD);
    }

    @Test
    public void validation_fail_invalidPassword_TooShort()
    {
        int minCharacterSize = 8;
        String shortPassword = validatorTestHelper.getStringOfLength(minCharacterSize - 1);
        SignupDto signupDto = SignupDto.builder().password(shortPassword).build();

        validatorTestHelper.validation_fail_noSpecials(signupDto, PASSWORD_FIELD);
    }

    @Test
    public void validation_fail_invalidPassword_TooLong()
    {
        int maxCharacterSize = 32;
        String longPassword = validatorTestHelper.getStringOfLength(maxCharacterSize + 1);
        SignupDto signupDto = SignupDto.builder().password(longPassword).build();

        validatorTestHelper.validation_fail_TooLong(signupDto, PASSWORD_FIELD, maxCharacterSize);
    }

    @Test
    public void validation_fail_invalidPassword_Null()
    {
        String nullPassword = validatorTestHelper.getNullString();
        SignupDto signupDto = SignupDto.builder().password(nullPassword).build();

        validatorTestHelper.validation_fail_Null(signupDto, PASSWORD_FIELD);
    }

    @Test
    public void validation_fail_invalidPassword_noCapitalLetters()
    {
        String noCapitalLettersPassword = validatorTestHelper.getNoCapitalLettersString();
        SignupDto signupDto = SignupDto.builder().password(noCapitalLettersPassword).build();

        validatorTestHelper.validation_fail_noSpecials(signupDto, PASSWORD_FIELD);
        ;
    }

    @Test
    public void validation_fail_invalidPassword_noDigits()
    {
        String noDigitsPassword = validatorTestHelper.getNoDigitsString();
        SignupDto signupDto = SignupDto.builder().password(noDigitsPassword).build();

        validatorTestHelper.validation_fail_noSpecials(signupDto, PASSWORD_FIELD);
    }

    @Test
    public void validation_fail_invalidPassword_noSymbols()
    {
        String password_noSpecialLettersString = validatorTestHelper.getNoSpecialLettersString();
        SignupDto signupDto = SignupDto.builder().password(password_noSpecialLettersString).build();

        validatorTestHelper.validation_fail_noSpecials(signupDto, PASSWORD_FIELD);
    }

    @Test
    public void validation_fail_invalidPassword_whiteSpaces()
    {
        String password_whiteSpaces = validatorTestHelper.getWithWhiteSpacesString();
        SignupDto signupDto = SignupDto.builder().password(password_whiteSpaces).build();

        validatorTestHelper.validation_fail_whiteSpaces(signupDto, PASSWORD_FIELD);
    }

    private SignupDto getVaildSignupDto()
    {
        return SignupDto
                       .builder()
                       .username(validatorTestHelper.getValidUsername())
                       .password(validatorTestHelper.getValidPassword())
                       .email(validatorTestHelper.getValidEmail())
                       .firstName(validatorTestHelper.getStringOfLength(3))
                       .lastName(validatorTestHelper.getStringOfLength(3))
                       .build();
    }
}
