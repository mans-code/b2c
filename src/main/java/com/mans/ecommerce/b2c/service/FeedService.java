package com.mans.ecommerce.b2c.service;

import com.mans.ecommerce.b2c.domain.entity.customer.Feed;
import com.mans.ecommerce.b2c.domain.entity.sharedSubEntity.ProductInfo;
import com.mans.ecommerce.b2c.repository.customer.FeedRepository;
import org.bson.types.ObjectId;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Service;

@Service
public class FeedService
{
    private FeedRepository feedRepository;

    FeedService(FeedRepository feedRepository)
    {
        this.feedRepository = feedRepository;
    }

    public void save(ObjectId id)
    {
        Feed feed = new Feed(id);
        feedRepository
                .save(feed)
                .subscribe();
    }

    public void addToCart(ObjectId id, ProductInfo productInfo)
    {
    }

    public void addToClicked(String sku, ServerHttpRequest req)
    {
    }
}
