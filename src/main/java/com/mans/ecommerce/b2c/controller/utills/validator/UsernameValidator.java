package com.mans.ecommerce.b2c.controller.utills.validator;

import java.util.ArrayList;
import java.util.List;

public class UsernameValidator
{

    private boolean valid;

    private List<String> messages;

    private String username;

    private final int MIN_LENGTH = 4;

    private final int MAX_LENGTH = 32;

    public static String REGEX = "^[a-zA-Z0-9_.-]+$";

    public UsernameValidator(String username)
    {
        this.username = username;
        messages = new ArrayList<>();
    }

    public void validate()
    {
        valid = true;

        if (isNullOrEmpty())
        {
            return;
        }

        min();
        max();
        pattern();
    }

    private void pattern()
    {
        if (!username.matches(REGEX))
        {
            valid = false;
            messages.add("must only contain a-z, 0-9, period,underscore, or hyphen");
        }

    }

    private void max()
    {
        if (username.length() > MAX_LENGTH)
        {
            valid = false;
            messages.add("must be no more than 32 characters in length");
        }
    }

    private void min()
    {
        if (username.length() < MIN_LENGTH)
        {
            valid = false;
            messages.add("must be 4 or more characters in length");
        }
    }

    private boolean isNullOrEmpty()
    {
        if (this.username == null)
        {
            valid = false;
            messages.add("must not be empty");
            return true;
        }
        return false;
    }

    public boolean isValid()
    {
        return valid;
    }

    public List<String> getMessages()
    {
        return messages;
    }
}
