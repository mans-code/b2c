package com.mans.ecommerce.b2c.domain.entity.product.subEntity;

import java.util.List;

import com.mans.ecommerce.b2c.domain.entity.product.QAndA;
import com.mans.ecommerce.b2c.domain.entity.product.Review;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@Getter
@Setter
@ToString(exclude = {})
public class Feedbacks
{
    private int numReviews;

    private double avgRatingScore;

    private int numCustomerRatings;

    private int numOfQuestions;

    private List<Review> topProductReviews;

    private List<QAndA> topProductQAndA;
}
