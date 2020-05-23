package com.mans.ecommerce.b2c.config.commendRunner;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.mans.ecommerce.b2c.domain.entity.customer.Cart;
import com.mans.ecommerce.b2c.domain.entity.customer.Customer;
import com.mans.ecommerce.b2c.domain.entity.customer.PaymentInfo;
import com.mans.ecommerce.b2c.domain.entity.product.Details;
import com.mans.ecommerce.b2c.domain.entity.product.Glimpse;
import com.mans.ecommerce.b2c.domain.entity.product.QAndA;
import com.mans.ecommerce.b2c.domain.entity.product.Review;
import com.mans.ecommerce.b2c.repository.customer.CartRepository;
import com.mans.ecommerce.b2c.repository.customer.CustomerRepository;
import com.mans.ecommerce.b2c.repository.customer.PaymentInfoRepository;
import com.mans.ecommerce.b2c.repository.product.DetailsRepository;
import com.mans.ecommerce.b2c.repository.product.GlimpseRepository;
import com.mans.ecommerce.b2c.repository.product.QAndARepository;
import com.mans.ecommerce.b2c.repository.product.ReviewRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

@Component

public class EmbeddedDBDummyData implements CommandLineRunner
{

    private CustomerRepository customerRepository;

    private CartRepository cartRepository;

    private PaymentInfoRepository paymentInfoRepository;

    private GlimpseRepository glimpseRepository;

    private DetailsRepository detailsRepository;

    private QAndARepository qAndARepository;

    private ReviewRepository reviewRepository;

    private ObjectMapper objectMapper;

    private ResourceLoader resourceLoader;

    public EmbeddedDBDummyData(
            CustomerRepository customerRepository,
            CartRepository cartRepository,
            PaymentInfoRepository paymentInfoRepository,
            GlimpseRepository glimpseRepository,
            DetailsRepository detailsRepository,
            QAndARepository qAndARepository,
            ReviewRepository reviewRepository,
            ObjectMapper objectMapper,
            ResourceLoader resourceLoader)
    {
        this.customerRepository = customerRepository;
        this.cartRepository = cartRepository;
        this.paymentInfoRepository = paymentInfoRepository;
        this.glimpseRepository = glimpseRepository;
        this.detailsRepository = detailsRepository;
        this.qAndARepository = qAndARepository;
        this.reviewRepository = reviewRepository;
        this.resourceLoader = resourceLoader;
        this.objectMapper = objectMapper;
    }

    @Override public void run(String... args)
            throws Exception
    {

        customerRepository.saveAll(loadFile("customers", Customer.class));
        cartRepository.saveAll(loadFile("carts", Cart.class));
        paymentInfoRepository.saveAll(loadFile("paymentsInfo", PaymentInfo.class));

        glimpseRepository.saveAll(loadFile("productsGlimpses", Glimpse.class));
        detailsRepository.saveAll(loadFile("productsDetails", Details.class));
        qAndARepository.saveAll(loadFile("qAndAs", QAndA.class));
        reviewRepository.saveAll(loadFile("reviews", Review.class));
    }

    private <T> List<T> loadFile(String fileName, Class<T> tClass)
            throws IOException
    {
        String path = String.format("classpath:db/%s.json", fileName);
        Resource resource = resourceLoader.getResource(path);
        InputStream inputStream = resource.getInputStream();

        CollectionType listType = objectMapper.getTypeFactory().constructCollectionType(ArrayList.class, tClass);
        List<T> list = objectMapper.readValue(inputStream, listType);

        return list;
    }

}
