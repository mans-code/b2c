package com.mans.ecommerce.b2c.domain.entity.product;

import java.util.Date;
import java.util.List;

import com.mans.ecommerce.b2c.domain.entity.product.subEntity.Comment;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@NoArgsConstructor
@Getter
@Setter
@ToString(exclude = {})
public class Review
{
    @Id
    private String id;

    @Indexed
    private String productId;

    private String customerId;

    private String by;

    private String title;

    private String review;

    private List<Comment> comments;

    //Maybe @Indexed
    private int helpful;

    //Maybe @Indexed
    @CreatedDate
    private Date createdOn;

    private int reportedAbuses;
}
