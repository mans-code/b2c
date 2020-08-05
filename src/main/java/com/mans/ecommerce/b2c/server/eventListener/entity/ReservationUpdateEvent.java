package com.mans.ecommerce.b2c.server.eventListener.entity;

import com.mans.ecommerce.b2c.domain.entity.product.subEntity.Reservation;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class ReservationUpdateEvent
{
    private Reservation reservation;

    private int lockedQuantity;
}
