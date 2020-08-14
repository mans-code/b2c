package com.mans.ecommerce.b2c.server.eventListener;

import java.util.List;

import com.mans.ecommerce.b2c.domain.entity.customer.Feed;
import com.mans.ecommerce.b2c.domain.entity.sharedSubEntity.ProductInfo;
import com.mans.ecommerce.b2c.repository.customer.FeedRepository;
import com.mans.ecommerce.b2c.server.eventListener.entity.AddProductToCartEvent;
import com.mans.ecommerce.b2c.server.eventListener.entity.BoughtProductsEvent;
import com.mans.ecommerce.b2c.server.eventListener.entity.ClickedOnProductEvent;
import com.mans.ecommerce.b2c.server.eventListener.entity.CreateFeedEvent;
import org.bson.types.ObjectId;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;

public class FeedListeners
{

    private FeedRepository feedRepository;

    FeedListeners(FeedRepository feedRepository)
    {
        this.feedRepository = feedRepository;
    }

    @Async
    @EventListener
    void createFeed(CreateFeedEvent event)
    {
        ObjectId id = event.getId();
        Feed feed = new Feed(id);
        //feedRepository.save(feed);
    }

    @Async
    @EventListener
    void added(AddProductToCartEvent event)
    {
        ObjectId id = event.getFeedId();
        ProductInfo productInfo = event.getProductInfo();
        //addToArray
    }

    @Async
    @EventListener
    void clicked(ClickedOnProductEvent event)
    {
        ObjectId id = event.getFeedId();
        String sku = event.getSku();
        //addToArray
    }

    @Async
    @EventListener
    void bought(BoughtProductsEvent event)
    {
        ObjectId id = event.getFeedId();
        List<ProductInfo> sku = event.getProductInfos();
        //AddToArray
    }
}
