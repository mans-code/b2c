package com.mans.ecommerce.b2c.domain.entity.product;

import java.util.List;

import com.mans.ecommerce.b2c.domain.entity.product.subEntity.Comment;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(exclude = {})
public class ProductReview
{
    private String productId;

    private String reviewerId;

    private String by;

    private String title;

    private String review;

    private List<Comment> comments;

    private int helpful;

    private int reportedAbuses;
}
