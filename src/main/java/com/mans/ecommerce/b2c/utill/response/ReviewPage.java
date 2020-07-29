package com.mans.ecommerce.b2c.utill.response;

import java.util.List;

import com.mans.ecommerce.b2c.domain.entity.product.Review;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString()
public class ReviewPage
{
    private List<Review> reviews;

    private String next;
}
