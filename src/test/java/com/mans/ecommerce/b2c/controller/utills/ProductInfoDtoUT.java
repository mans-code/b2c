package com.mans.ecommerce.b2c.controller.utills;

import com.mans.ecommerce.b2c.controller.utills.dto.LoginDto;
import com.mans.ecommerce.b2c.controller.utills.dto.ProductInfoDto;
import org.junit.BeforeClass;
import org.junit.Test;

public class ProductInfoDtoUT
{

    private final String PRODUCT_FIELD = "productId";

    private final String QUANTITY_FIELD = "quantity";

    private final int SMALLEST_QUANTITY = 1;

    private final int LARGEST_QUANTITY = 20;

    private final int VALID_QUANTITY = 1;


    private static ValidatorTestHelper validatorTestHelper;

    private static String VALID_ID;

    @BeforeClass
    public static void beforeClass()
    {
        validatorTestHelper = new ValidatorTestHelper();
        VALID_ID =  validatorTestHelper.getValidId();
    }


    @Test
    public void validation_pass()
    {
        ProductInfoDto productInfoDto = new ProductInfoDto(VALID_ID, VALID_QUANTITY , null);
        validatorTestHelper.validation_pass(productInfoDto);
    }

    @Test
    public void validation_fail_invalidProductId_TooShort()
    {
        int minCharacterSize = 8;
        String shortId= validatorTestHelper.getStringOfLength(minCharacterSize - 1);
        ProductInfoDto productInfoDto = new ProductInfoDto(shortId, VALID_QUANTITY , null);
        validatorTestHelper.validation_fail_TooShort(productInfoDto, PRODUCT_FIELD, minCharacterSize);
    }

    @Test
    public void validation_fail_invalidUsername_Null()
    {
        String nullId = validatorTestHelper.getNullString();
        ProductInfoDto productInfoDto = new ProductInfoDto(nullId, VALID_QUANTITY , null);
        validatorTestHelper.validation_fail_Null(productInfoDto, PRODUCT_FIELD);
    }

    @Test
    public void validation_fail_invalidQuantity_TooSmall()
    {
        int quantity = SMALLEST_QUANTITY - 1;

        ProductInfoDto productInfoDto = new ProductInfoDto(VALID_ID, quantity , null);
        validatorTestHelper.validation_fail_TooSmall(productInfoDto, QUANTITY_FIELD, SMALLEST_QUANTITY);
    }

    @Test
    public void validation_fail_invalidQuantity_TooLarge()
    {
        int quantity = LARGEST_QUANTITY + 1;
        ProductInfoDto productInfoDto = new ProductInfoDto(VALID_ID, quantity , null);
        validatorTestHelper.validation_fail_TooLarge(productInfoDto, QUANTITY_FIELD, LARGEST_QUANTITY);
    }
}
