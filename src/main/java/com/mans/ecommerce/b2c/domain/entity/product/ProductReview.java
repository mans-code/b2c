package com.mans.ecommerce.b2c.domain.entity.product;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@ToString(exclude = { "reportedAbuses" })
@Document(collection = "review")
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

    private class Comment
    {
        private String commenterID;

        private String by;

        private String replay;

        private int reportedAbuses;

        private int helpful;
    }

    public String getProductId()
    {
        return productId;
    }

    public String getReviewerId()
    {
        return reviewerId;
    }

    public String getBy()
    {
        return by;
    }

    public String getTitle()
    {
        return title;
    }

    public String getReview()
    {
        return review;
    }

    public List<Comment> getComments()
    {
        return comments;
    }

    public int getHelpful()
    {
        return helpful;
    }

    public int getReportedAbuses()
    {
        return reportedAbuses;
    }

    public void setProductId(String productId)
    {
        this.productId = productId;
    }

    public void setReviewerId(String reviewerId)
    {
        this.reviewerId = reviewerId;
    }

    public void setBy(String by)
    {
        this.by = by;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public void setReview(String review)
    {
        this.review = review;
    }

    public void setComments(List<Comment> comments)
    {
        this.comments = comments;
    }

    public void setHelpful(int helpful)
    {
        this.helpful = helpful;
    }

    public void setReportedAbuses(int reportedAbuses)
    {
        this.reportedAbuses = reportedAbuses;
    }
}
