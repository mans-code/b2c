package com.mans.ecommerce.b2c.server.eventListener.entity;

import java.util.Optional;

import com.mans.ecommerce.b2c.utill.Global;
import lombok.Getter;
import org.bson.types.ObjectId;
import org.springframework.http.server.reactive.ServerHttpRequest;

@Getter
public class ClickedOnProductEvent
{

    private ObjectId feedId;

    private String sku;

        public ClickedOnProductEvent(String sku, ServerHttpRequest req)
        {
            this.sku = sku;
            this.feedId = getId(req);
        }

        private ObjectId getId(ServerHttpRequest req)
        {
            Optional<String> idOptional = Global.getId(req);
            if (idOptional.isPresent())
            {
                return new ObjectId(idOptional.get());
            }
            return null;
        }
}
