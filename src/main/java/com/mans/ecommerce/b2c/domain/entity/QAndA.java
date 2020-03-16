package com.mans.ecommerce.b2c.domain.entity;

import java.util.Date;
import java.util.List;


public class QAndA
{
    private String question;

    private List<Answer> answers;

    private Answer ownerAnswer = new Answer();

    private int votes;

    protected QAndA()
    {

    }
    
    private class Answer
    {
        private String answer;

        private String by;

        private Date createdOn;
    }

}
