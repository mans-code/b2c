package com.mans.ecommerce.b2c.domain.entity.product;

import java.util.Date;
import java.util.List;

import com.mans.ecommerce.b2c.domain.entity.product.subEntity.Answer;
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
public class QAndA
{

    @Id
    private String Id;

    @Indexed
    private String productId;

    private String question;

    private List<Answer> answers;

    private Answer ownerAnswer;

    //Maybe @Indexed
    private int votes;

    //Maybe @Indexed
    @CreatedDate
    private Date createdOn;

    private String customerId;

    private String by;
}
