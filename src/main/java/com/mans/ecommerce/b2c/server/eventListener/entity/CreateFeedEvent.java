package com.mans.ecommerce.b2c.server.eventListener.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bson.types.ObjectId;

@AllArgsConstructor
@Getter
public class CreateFeedEvent
{
    private ObjectId id;
}
