package com.mans.ecommerce.b2c.command.runner;

import java.util.List;

import com.mans.ecommerce.b2c.domain.entity.product.ProductDetails;
import com.mans.ecommerce.b2c.domain.entity.product.ProductGlimpse;
import com.mans.ecommerce.b2c.domain.entity.product.ProductQAndA;
import com.mans.ecommerce.b2c.domain.entity.product.ProductReview;
import com.mans.ecommerce.b2c.domain.entity.sharedSubEntity.ProductInfo;
import com.mans.ecommerce.b2c.repository.product.ProductDetailsRepository;
import com.mans.ecommerce.b2c.repository.product.ProductGlimpseRepository;
import com.mans.ecommerce.b2c.repository.product.ProductQAndARepository;
import com.mans.ecommerce.b2c.repository.product.ProductReviewRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;

@Order(2)
public class ProductDummyData implements CommandLineRunner
{

    private ProductGlimpseRepository productGlimpseRepository;

    private ProductDetailsRepository productDetailsRepository;

    private ProductQAndARepository productQAndARepository;

    private ProductReviewRepository productReviewRepository;

    public ProductDummyData(
            ProductGlimpseRepository productGlimpseRepository,
            ProductDetailsRepository productDetailsRepository,
            ProductQAndARepository productQAndARepository,
            ProductReviewRepository productReviewRepository)
    {
        this.productGlimpseRepository = productGlimpseRepository;
        this.productDetailsRepository = productDetailsRepository;
        this.productQAndARepository = productQAndARepository;
        this.productReviewRepository = productReviewRepository;
    }

    @Override
    public void run(String... args)
            throws Exception
    {
        List<ProductGlimpse> productsGlimpses = createProductsGlimpses();
        productGlimpseRepository.saveAll(productsGlimpses);

        List<ProductDetails> productsDetails = createProductsDetails(productsGlimpses);
        productDetailsRepository.saveAll(productsDetails);

        List<ProductQAndA> productsQAndAS = createProductsQAndAs(productsGlimpses);
        productQAndARepository.saveAll(productsQAndAS);

        List<ProductReview> productsProductReviews = createProductsReviews(productsGlimpses);
        productReviewRepository.saveAll(productsProductReviews);
    }

    private List<ProductGlimpse> createProductsGlimpses()
    {
        return null;
    }

    private List<ProductDetails> createProductsDetails(List<ProductGlimpse> productsGlimpses)
    {
        return null;
    }

    private List<ProductReview> createProductsReviews(List<ProductGlimpse> productsGlimpses)
    {
        return null;
    }

    private List<ProductQAndA> createProductsQAndAs(List<ProductGlimpse> productsGlimpses)
    {
        return null;
    }

    public List<ProductInfo> getRandomListOfProductInfo()
    {
        return null;
    }

    public ProductInfo getRandomProductInfo()
    {
        return null;
    }
}
