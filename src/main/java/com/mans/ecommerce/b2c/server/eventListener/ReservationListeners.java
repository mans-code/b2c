package com.mans.ecommerce.b2c.server.eventListener;

import com.mans.ecommerce.b2c.domain.entity.product.subEntity.Reservation;
import com.mans.ecommerce.b2c.domain.exception.SystemConstraintViolation;
import com.mans.ecommerce.b2c.server.eventListener.entity.ReservationCreationEvent;
import com.mans.ecommerce.b2c.server.eventListener.entity.ReservationUpdateEvent;
import com.mans.ecommerce.b2c.service.ProductService;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class ReservationListeners
{

    private ProductService productService;

//    @Async
//    @EventListener
//    void saveReservation(ReservationCreationEvent event)
//    {
//        Reservation reservation = event.getReservation();
//        boolean added = productService.addReservation(reservation);
//        if(!added)
//        {
//            new SystemConstraintViolation(String.format("couldn't save reservation=", reservation.toString()));
//        }
//    }
//
//    @Async
//    @EventListener
//    void updateReservation(ReservationUpdateEvent event)
//    {
//        Reservation reservation = event.getReservation();
//        int  locked = event.getLockedQuantity();
//        boolean updated = productService.updateReservation(reservation, locked);
//        if(!updated)
//        {
//            new SystemConstraintViolation(String.format("couldn't update reservation=", reservation.toString()));
//        }
//    }

}
