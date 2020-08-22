package com.mans.ecommerce.b2c.controller.utill;

import com.mans.ecommerce.b2c.controller.utill.dto.ProductDto;
import com.mans.ecommerce.b2c.domain.enums.CartAction;
import org.junit.BeforeClass;
import org.junit.Test;

public class ProductDtoUT
{

    private final String SKU_FIELD = "sku";

    private final String VALID_CART_ACTION = "add";

    private final String QUANTITY_FIELD = "quantity";

    private final String CART_ACTION_FIELD = "cartAction";

    private final int SMALLEST_QUANTITY = -1;

    private final int LARGEST_QUANTITY = 20;

    private final String VALID_SKU = "dajfh-rjfj-4245522";

    private static ValidatorTestHelper validatorTestHelper;

    @BeforeClass
    public static void beforeClass()
    {
        validatorTestHelper = new ValidatorTestHelper();
    }

    @Test
    public void validation_pass()
    {
        ProductDto productInfoDto = new ProductDto(VALID_SKU, VALID_CART_ACTION);
        validatorTestHelper.validation_pass(productInfoDto);
    }

    @Test
    public void validation_fail_invalidProductSku_TooShort()
    {
        int minCharacterSize = 8;
        String shortId = validatorTestHelper.getStringOfLength(minCharacterSize - 1);
        ProductDto productInfoDto = new ProductDto(shortId, VALID_CART_ACTION);
        validatorTestHelper.validation_fail_TooShort(productInfoDto, SKU_FIELD, minCharacterSize);
    }

    @Test
    public void validation_fail_invalidSku_Null()
    {
        String nullId = validatorTestHelper.getNullString();
        ProductDto productInfoDto = new ProductDto(nullId, VALID_CART_ACTION);
        validatorTestHelper.validation_fail_Null(productInfoDto, SKU_FIELD);
    }

    @Test
    public void validation_fail_invalidQuantity_TooSmall()
    {
        int quantity = SMALLEST_QUANTITY - 1;

        ProductDto productInfoDto = new ProductDto(VALID_SKU, VALID_CART_ACTION, quantity);
        validatorTestHelper.validation_fail_TooSmall(productInfoDto, QUANTITY_FIELD, SMALLEST_QUANTITY);
    }

    @Test
    public void validation_fail_invalidQuantity_TooLarge()
    {
        int quantity = LARGEST_QUANTITY + 1;
        ProductDto productInfoDto = new ProductDto(VALID_SKU, VALID_CART_ACTION, quantity);
        validatorTestHelper.validation_fail_TooLarge(productInfoDto, QUANTITY_FIELD, LARGEST_QUANTITY);
    }

    @Test
    public void validation_fail_NotEnumValue()
    {
        int quantity = LARGEST_QUANTITY + 1;
        ProductDto productInfoDto = new ProductDto(VALID_SKU, "nothing", quantity);
        validatorTestHelper.validation_NotEnumValue(productInfoDto, CART_ACTION_FIELD);
    }
}
