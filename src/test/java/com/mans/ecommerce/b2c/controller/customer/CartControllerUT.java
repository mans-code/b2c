package com.mans.ecommerce.b2c.controller.customer;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class CartControllerUT
{

//    @InjectMocks
//    private CartController cartController;
//
//    @Mock
//    private CartService cartService;
//
//    @Mock
//    private ProductService productService;
//
//    @Mock
//    private CartLogic cartLogic;
//
//    @Test
//    public void getCart_pass(@Mock Cart expected)
//    {
//        when(cartService.findById(anyString()))
//                .thenReturn(expected);
//
//        Cart actual = cartController.getCart(anyString());
//
//        assertEquals(expected, actual);
//    }
//
//    @Test
//    public void add_pass(@Mock Cart expected)
//    {
//        int requestedQuantity = 2;
//        int availableQuantity = requestedQuantity + 1;
//        ProductDto dto = ProductDto.builder().quantity(requestedQuantity).build();
//
//        setAddTest(expected, availableQuantity, dto);
//
//        Cart actual = cartController.add(anyString(), dto);
//
//        assertEquals(expected, actual);
//        verify(cartService, times(1)).save(expected);
//    }
//
//    @Test
//    public void add_fail_outOfStock()
//    {
//        int requestedQuantity = 2;
//        int availableQuantity = 0;
//        ProductDto dto = ProductDto.builder().quantity(requestedQuantity).build();
//        ProductInfo productInfo = ProductInfo.builder().quantity(availableQuantity).build();
//
//        when(productService.lockAndGetProductInfo(dto))
//                .thenReturn(productInfo);
//
//        Exception exc = assertThrows(OutOfStock.class,
//                                     () -> cartController.add(anyString(), dto));
//
//        String actualMessage = exc.getMessage();
//        String expectedMessage = "product is Out Of Stock";
//
//        assertThat(actualMessage, equalToIgnoringCase(expectedMessage));
//    }
//
//    @Test
//    public void add_fail_partialOutOfStock(@Mock Cart cart)
//    {
//        int requestedQuantity = 2;
//        int availableQuantity = 1;
//        ProductDto dto = ProductDto.builder().quantity(requestedQuantity).build();
//
//        setAddTest(cart, availableQuantity, dto);
//
//        Exception exception = assertThrows(PartialOutOfStock.class,
//                                           () -> cartController.add(anyString(), dto));
//
//        String actualMessage = exception.getMessage();
//        String expectedMessageTemplate = "This product has only %d of these available";
//        String expectedMessage = String.format(expectedMessageTemplate, 1);
//
//        assertThat(actualMessage, equalToIgnoringCase(expectedMessage));
//    }
//
//    @Test
//    public void delete_pass(@Mock ProductDto productInfoDto, @Mock Cart expected)
//    {
//        when(cartService.findById(anyString()))
//                .thenReturn(expected);
//
//        when(cartService.save(expected))
//                .thenReturn(expected);
//
//        Cart actual = cartController.delete(anyString(), productInfoDto);
//
//        assertEquals(expected, actual);
//    }
//
//    @Test
//    public void deleteAll_pass(@Mock ProductDto productInfoDto, @Mock Cart expected)
//    {
//        when(cartService.findById(anyString()))
//                .thenReturn(expected);
//
//        when(cartService.save(expected))
//                .thenReturn(expected);
//
//        Cart actual = cartController.deleteAll(anyString());
//
//        assertEquals(expected, actual);
//    }
//
//    private void setAddTest(
//            Cart cart,
//            int availableQuantity,
//            ProductDto dto)
//    {
//        ProductInfo productInfo = ProductInfo.builder().quantity(availableQuantity).build();
//
//        when(productService.lockAndGetProductInfo(dto))
//                .thenReturn(productInfo);
//
//        when(cartService.findById(anyString()))
//                .thenReturn(cart);
//
//        when(cartService.save(cart))
//                .thenReturn(cart);
//    }
}
