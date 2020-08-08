package com.mans.ecommerce.b2c.server.eventListener;

import com.mans.ecommerce.b2c.domain.entity.customer.Customer;
import com.mans.ecommerce.b2c.server.eventListener.entity.CustomerCreationEvent;
import com.mans.ecommerce.b2c.utill.Emailing;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class CustomerListeners
{
    private Emailing emailing;

    CustomerListeners(Emailing emailing)
    {
        this.emailing = emailing;
    }

    @Async
    @EventListener
    void sendWelcoming(CustomerCreationEvent customerEvent)
    {
        Customer customer = customerEvent.getCustomer();
        String subject = "Welcome " + customer.getFirstName();
        String to = customer.getEmail();
        String body = "nice To have on border";
        emailing.sendEmail(to, subject, body);
    }
}
