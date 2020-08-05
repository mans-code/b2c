package com.mans.ecommerce.b2c.utill;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString(exclude = {})
public class ProductLockErrorInfo
{
    private String sku;

    private String variationId;

    private String title;

    private int requestedQuantity;

    private int lockedQuantity;
}
