package com.mans.ecommerce.b2c.service;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString(exclude = {})
public class ProductLockInfo
{
    private String sku;

    private String variationId;

    private String title;

    private int requestedQuentity;

    private int lockedQuentity;
}
