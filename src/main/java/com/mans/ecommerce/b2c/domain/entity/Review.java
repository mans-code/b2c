package com.mans.ecommerce.b2c.domain.entity.subEntity;

import java.util.List;

public class Review
{
    private String reviwerID;
    private String username;
    private String name;
    private String title;
    private String review;
    private List<Comment> comments;
    private int helpful;
    private int reportedAbuses;

    private class Comment
    {
        private String commenterID;
        private String username;
        private String name;
        private String replay;
        private int reportedAbuses;
    }
}
