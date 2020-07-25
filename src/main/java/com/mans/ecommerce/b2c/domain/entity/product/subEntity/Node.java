package com.mans.ecommerce.b2c.domain.entity.product.subEntity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@Setter
@Getter
@ToString
public class Node
{
    private String id;

    private String title;

    private int priority;//FrontEnd ordering

    private boolean selected; //FrontEnd Focus
}
