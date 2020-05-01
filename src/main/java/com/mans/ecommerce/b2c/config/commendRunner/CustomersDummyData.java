package com.mans.ecommerce.b2c.config.commendRunner;

import java.util.List;

import com.github.javafaker.Faker;
import com.mans.ecommerce.b2c.domain.entity.customer.Customer;
import com.mans.ecommerce.b2c.repository.customer.CustomerCartRepository;
import com.mans.ecommerce.b2c.repository.customer.CustomerFeedRepository;
import com.mans.ecommerce.b2c.repository.customer.CustomerPaymentInfoRepository;
import com.mans.ecommerce.b2c.repository.customer.CustomerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class CustomersDummyData implements CommandLineRunner
{

    private CustomerRepository customerRepository;

    private CustomerCartRepository customerCartRepository;

    private CustomerFeedRepository customerFeedRepository;

    private CustomerPaymentInfoRepository customerPaymentInfoRepository;

    private Faker faker;

    public CustomersDummyData(
            CustomerRepository customerRepository,
            CustomerCartRepository customerCartRepository,
            CustomerFeedRepository customerFeedRepository,
            CustomerPaymentInfoRepository customerPaymentInfoRepository
            )
    {
        this.customerRepository = customerRepository;
        this.customerCartRepository = customerCartRepository;
        this.customerFeedRepository = customerFeedRepository;
        this.customerPaymentInfoRepository = customerPaymentInfoRepository;
        this.faker = new Faker();
    }

    @Override
    public void run(String... args)
            throws Exception
    {

    }

    private List<Customer> createCustomers()
    {
        return null;
    }

}
