package com.mans.ecommerce.b2c.server.commendRunner;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.mans.ecommerce.b2c.domain.entity.customer.Cart;
import com.mans.ecommerce.b2c.domain.entity.customer.Customer;
import com.mans.ecommerce.b2c.domain.entity.customer.PaymentInfo;
import com.mans.ecommerce.b2c.domain.entity.product.Product;
import com.mans.ecommerce.b2c.domain.entity.product.QAndA;
import com.mans.ecommerce.b2c.domain.entity.product.Review;
import com.mans.ecommerce.b2c.repository.customer.CartRepository;
import com.mans.ecommerce.b2c.repository.customer.CustomerRepository;
import com.mans.ecommerce.b2c.repository.customer.PaymentInfoRepository;
import com.mans.ecommerce.b2c.repository.product.ProductRepository;
import com.mans.ecommerce.b2c.repository.product.QAndARepository;
import com.mans.ecommerce.b2c.repository.product.ReviewRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@Profile("!prod")
public class EmbeddedDBDummyData implements CommandLineRunner
{

    private CustomerRepository customerRepository;

    private CartRepository cartRepository;

    private PaymentInfoRepository paymentInfoRepository;

    private ProductRepository productRepository;

    private QAndARepository qAndARepository;

    private ReviewRepository reviewRepository;

    private ObjectMapper objectMapper;

    private ResourceLoader resourceLoader;

    private PasswordEncoder passwordEncoder;

    public EmbeddedDBDummyData(
            CustomerRepository customerRepository,
            CartRepository cartRepository,
            PaymentInfoRepository paymentInfoRepository,
            ProductRepository productRepository,
            QAndARepository qAndARepository,
            ReviewRepository reviewRepository,
            ObjectMapper objectMapper,
            ResourceLoader resourceLoader,
            PasswordEncoder passwordEncoder)
    {
        this.customerRepository = customerRepository;
        this.cartRepository = cartRepository;
        this.paymentInfoRepository = paymentInfoRepository;
        this.productRepository = productRepository;
        this.qAndARepository = qAndARepository;
        this.reviewRepository = reviewRepository;
        this.resourceLoader = resourceLoader;
        this.objectMapper = objectMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override public void run(String... args)
            throws Exception
    {

        cartRepository.deleteAll().block();
        cartRepository.saveAll(loadFile("carts", Cart.class)).subscribe();

        paymentInfoRepository.deleteAll().block();
        paymentInfoRepository.saveAll(loadFile("paymentsInfo", PaymentInfo.class)).subscribe();

        productRepository.deleteAll().subscribe();
        productRepository.saveAll(loadFile("products", Product.class)).subscribe();

        qAndARepository.deleteAll().block();
        qAndARepository.saveAll(loadFile("qAndAs", QAndA.class)).subscribe();

        reviewRepository.deleteAll().block();
        reviewRepository.saveAll(loadFile("reviews", Review.class)).subscribe();

        List<Customer> customers = loadFile("customers", Customer.class);
        encodeAllPassword(customers);

        customerRepository.deleteAll().block();
        customerRepository.saveAll(customers).subscribe();

        cartRepository.findAll().subscribe(System.out::println);
    }

    private void encodeAllPassword(List<Customer> customers)
    {
        customers.forEach(customer -> {
            String password = customer.getPassword();
            String encodedPassword = passwordEncoder.encode(password);
            customer.setPassword(encodedPassword);
        });
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
