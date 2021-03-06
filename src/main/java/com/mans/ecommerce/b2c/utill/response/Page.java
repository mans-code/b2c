package com.mans.ecommerce.b2c.utill.response;

import java.util.List;

import com.mans.ecommerce.b2c.domain.entity.product.QAndA;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString()
public class Page<T>
{
    private List<T> list;

    private String next;

}
