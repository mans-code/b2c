package com.mans.ecommerce.b2c.service;

import java.util.Optional;

import com.mans.ecommerce.b2c.domain.entity.customer.Cart;
import com.mans.ecommerce.b2c.domain.exception.ResourceNotFoundException;
import com.mans.ecommerce.b2c.repository.customer.CartRepository;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CartServiceUT
{

    @InjectMocks
    private CartService cartService;

    @Mock
    private CartRepository cartRepository;

    @Test
    public void findById_pass()
    {
        Cart expected = new Cart();
        Optional<Cart> cartOptional = Optional.of(expected);

        when(cartRepository.findById(any()))
                .thenReturn(cartOptional);

        Cart actual = cartService.findById(any());

        assertEquals(expected, actual);
    }

    @Test
    public void findById_fail_cartNotFound()
    {
        Optional<Cart> cartOptional = Optional.empty();
        ObjectId id = new ObjectId("id");

        when(cartRepository.findById(any()))
                .thenReturn(cartOptional);

        Exception ex = assertThrows(ResourceNotFoundException.class,
                                    () -> cartService.findById(id));

        String errorMessageTemplate = cartService.getNOT_FOUND_TEMPLATE();
        String expectedErrorMessage = String.format(errorMessageTemplate, id);

        assertThat(ex.getMessage(), equalTo(expectedErrorMessage));
    }
}
