package com.mans.ecommerce.b2c.domain.entity.financial.subEntity;

import java.time.LocalDateTime;
import java.util.Date;

import com.mans.ecommerce.b2c.domain.entity.customer.subEntity.Address;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;

@Getter
@Setter
@ToString(exclude = {})
public class ShippingInfo
{
    @CreatedDate
    private LocalDateTime cratedOn;

    private Date updateOn;

    private String status;

    private String method;

    private String trackingNumber;

    private Address address;

    public ShippingInfo(Address address)
    {
        this.address = address;
    }
}