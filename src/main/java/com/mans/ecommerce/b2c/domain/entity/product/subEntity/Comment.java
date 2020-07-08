package com.mans.ecommerce.b2c.domain.entity.product.subEntity;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(exclude = {})
public class Comment
{
    private String customerId;

    private String by;

    private String replay;

    private int reportedAbuses;

    private int helpful;

    private Date createdOn;
}
