package com.mans.ecommerce.b2c.domain.entity.product;

import java.util.Date;
import java.util.List;

import com.mans.ecommerce.b2c.domain.entity.product.subEntity.Answer;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(exclude = {})
public class ProductQAndA
{

    private String productId;

    private String question;

    private List<Answer> answers;

    private Answer ownerAnswer;

    private int votes;

    private Date createdOn;

    private String customerId;

    private String by;
}
