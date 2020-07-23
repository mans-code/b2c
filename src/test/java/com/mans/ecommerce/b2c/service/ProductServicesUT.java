package com.mans.ecommerce.b2c.service;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.mans.ecommerce.b2c.controller.utills.dto.ProductDto;
import com.mans.ecommerce.b2c.domain.entity.customer.Cart;
import com.mans.ecommerce.b2c.domain.entity.product.Product;
import com.mans.ecommerce.b2c.domain.entity.product.subEntity.Availability;
import com.mans.ecommerce.b2c.domain.entity.product.subEntity.BasicInfo;
import com.mans.ecommerce.b2c.domain.entity.sharedSubEntity.ProductInfo;
import com.mans.ecommerce.b2c.domain.exception.ResourceNotFoundException;
import com.mans.ecommerce.b2c.repository.product.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServicesUT
{

    @Spy
    @InjectMocks
    private ProductService productService;

    @Mock
    private ProductRepository productRepository;

    @Test
    public void findById_pass()
    {
        Product expected = new Product();
        Optional<Product> productOptional = Optional.of(expected);

        when(productRepository.findById(anyString()))
                .thenReturn(productOptional);

        Product actual = productService.findById(anyString());

        assertEquals(expected, actual);
    }

    @Test
    public void findById_fail_productNotFound()
    {
        Optional<Product> productOptional = Optional.empty();
        String id = "id";

        when(productRepository.findById(id))
                .thenReturn(productOptional);

        Exception ex = assertThrows(ResourceNotFoundException.class,
                                    () -> productService.findById(id));

        String errorMessageTemplate = productService.getNOT_FOUNT_TEMPLATE();
        String expectedErrorMessage = String.format(errorMessageTemplate, id);

        assertThat(ex.getMessage(), equalTo(expectedErrorMessage));
    }

    @Test
    public void lockAndGetProductInfo_pass_lockedAllRequestedQuantity()
    {
        int requestedQuantity = 2;
        int availableQuantity = requestedQuantity + 1;
        Product product = createProduct(availableQuantity);
        ProductInfo productInfo = lockAndGetProductInfoPass(requestedQuantity, product);

        assertTrue(productInfo.getQuantity() == requestedQuantity);
    }

    @Test
    public void lockAndGetProductInfo_pass_lockedSomeRequestedQuantity()
    {
        int requestedQuantity = 2;
        int availableQuantity = requestedQuantity - 1;
        Product product = createProduct(availableQuantity);
        ProductInfo productInfo = lockAndGetProductInfoPass(requestedQuantity, product);

        assertTrue(productInfo.getQuantity() == availableQuantity);
    }

    @Test
    public void lockAndGetProductInfo_fail_ProductNotFound(@Mock ProductDto productInfoDto)
    {
        String id = null;
        int requestedQuentity = 0;

        when(productRepository.lockAndProjectBasicInfoAndPrevAvailability(id, requestedQuentity))
                .thenReturn(Optional.empty());

        Exception ex = assertThrows(ResourceNotFoundException.class,
                                    () -> productService.lockAndGetProductInfo(productInfoDto));

        String errorMessageTemplate = productService.getNOT_FOUNT_TEMPLATE();
        String expectedErrorMessage = String.format(errorMessageTemplate, id);

        assertThat(ex.getMessage(), equalTo(expectedErrorMessage));
    }

    @Test
    public void unlock_pass()
    {
        unlock(true, true, false);
        verify(productRepository, times(1)).unlock(anyString(), anyInt());
    }

    @Test
    public void unlockProductList_pass()
    {
        int size = 10;
        ProductInfo productInfo = new ProductInfo();
        Cart cart = Cart.builder().expireDate(timeNow()).build();
        List<ProductInfo> list = IntStream.range(0, size).mapToObj(i -> productInfo).collect(Collectors.toList());

        productService.unlock(cart, list);

        verify(productService, times(size)).unlock(cart, productInfo);
    }

    @Test
    public void unlock_pass_cartNotActive()
    {
        unlock(false, true, false);
        verify(productRepository, never()).unlock(anyString(), anyInt());
    }

    @Test
    public void unlock_pass_productNotLocked()
    {
        unlock(true, false, false);
        verify(productRepository, never()).unlock(anyString(), anyInt());
    }


    @Test
    public void unlock_pass_alreadyExpired()
    {
        unlock(true, false, true);
        verify(productRepository, never()).unlock(anyString(), anyInt());
    }



    private void unlock(boolean active, boolean locked, boolean expired)
    {
        String id = "";
        int quantityToUnlock = 0;
        Date expireDate = expired ? timeNow() : getTimeAfter10Mins();

        ProductInfo productInfo = ProductInfo.builder()
                                             .productId(id)
                                             .quantity(quantityToUnlock)
                                             .locked(locked).build();

        Cart cart = Cart.builder().active(active).expireDate(expireDate).build();

        productService.unlock(cart, productInfo);
    }

    private Date timeNow()
    {
        long now = Instant.now().toEpochMilli();
        return new Date(now);
    }

    private Date getTimeAfter10Mins()
    {
        long ONE_MINUTE_IN_MILLIS = 60000;
        long now = timeNow().getTime();
        return new Date(now + (10 * ONE_MINUTE_IN_MILLIS));
    }

    private ProductInfo lockAndGetProductInfoPass(int requestedQuantity, Product product)
    {
        String id = "id";
        ProductDto productInfoDto = ProductDto.builder().productId(id).quantity(requestedQuantity).build();

        when(productRepository.lockAndProjectBasicInfoAndPrevAvailability(id, requestedQuantity))
                .thenReturn(Optional.of(product));

        return productService.lockAndGetProductInfo(productInfoDto);
    }

    private Product createProduct(int availableQuantity)
    {
        Availability availability = Availability.builder().numUnitsAvailable(availableQuantity).build();
        BasicInfo basicInfo = BasicInfo.builder().title("title").build();
        return Product
                       .builder()
                       .basicInfo(basicInfo)
                       .availability(availability)
                       .build();

    }

}
