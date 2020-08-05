package com.mans.ecommerce.b2c.server.eventListener.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ServerErrorEvent
{
    private Exception exception;
}
