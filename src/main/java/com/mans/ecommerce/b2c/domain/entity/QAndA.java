package com.mans.ecommerce.b2c.domain.entity;

import java.util.Date;
import java.util.List;

public class QAndA
{

    private String productId;

    private String question;

    private List<Answer> answers;

    private Answer ownerAnswer = new Answer();

    private int votes;

    private Date createdOn;

    private String customerId;

    private String by;

    protected QAndA()
    {

    }

    private class Answer
    {
        private String answer;

        private String customerId;

        private String by;

        private Date createdOn;
    }

    public String getProductId()
    {
        return productId;
    }

    public String getQuestion()
    {
        return question;
    }

    public List<Answer> getAnswers()
    {
        return answers;
    }

    public Answer getOwnerAnswer()
    {
        return ownerAnswer;
    }

    public int getVotes()
    {
        return votes;
    }

    public Date getCreatedOn()
    {
        return createdOn;
    }

    public String getCustomerId()
    {
        return customerId;
    }

    public String getBy()
    {
        return by;
    }

    public void setProductId(String productId)
    {
        this.productId = productId;
    }

    public void setQuestion(String question)
    {
        this.question = question;
    }

    public void setAnswers(List<Answer> answers)
    {
        this.answers = answers;
    }

    public void setOwnerAnswer(Answer ownerAnswer)
    {
        this.ownerAnswer = ownerAnswer;
    }

    public void setVotes(int votes)
    {
        this.votes = votes;
    }

    public void setCreatedOn(Date createdOn)
    {
        this.createdOn = createdOn;
    }

    public void setCustomerId(String customerId)
    {
        this.customerId = customerId;
    }

    public void setBy(String by)
    {
        this.by = by;
    }
}
