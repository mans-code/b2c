package com.mans.ecommerce.b2c.command.runner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.github.javafaker.Faker;
import com.mans.ecommerce.b2c.domain.entity.customer.*;
import com.mans.ecommerce.b2c.domain.entity.customer.CustomerPaymentInfo.CreditCard;
import com.mans.ecommerce.b2c.repository.customer.CustomerCartRepository;
import com.mans.ecommerce.b2c.repository.customer.CustomerFeedRepository;
import com.mans.ecommerce.b2c.repository.customer.CustomerPaymentInfoRepository;
import com.mans.ecommerce.b2c.repository.customer.CustomerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;

@Profile("dev")
@DependsOn({ "productDummyData" })
@Order(2)
public class CustomersDummyData implements CommandLineRunner
{

    private CustomerRepository customerRepository;

    private CustomerCartRepository customerCartRepository;

    private CustomerFeedRepository customerFeedRepository;

    private CustomerPaymentInfoRepository customerPaymentInfoRepository;

    private ProductDummyData productDummyData;

    private Faker faker;

    public CustomersDummyData(
            CustomerRepository customerRepository,
            CustomerCartRepository customerCartRepository,
            CustomerFeedRepository customerFeedRepository,
            CustomerPaymentInfoRepository customerPaymentInfoRepository,
            ProductDummyData productDummyData,
            Faker faker)
    {
        this.customerRepository = customerRepository;
        this.customerCartRepository = customerCartRepository;
        this.customerFeedRepository = customerFeedRepository;
        this.customerPaymentInfoRepository = customerPaymentInfoRepository;
        this.productDummyData = productDummyData;
        this.faker = faker;
    }

    @Override
    public void run(String... args)
            throws Exception
    {
        List<Customer> customers = createCustomers();
        customerRepository.saveAll(customers);

        List<CustomerCart> customersCarts = createCustomersCarts(customers);
        customerCartRepository.saveAll(customersCarts);

        List<CustomerFeed> customersFeeds = createCustomersFeeds(customers);
        customerFeedRepository.saveAll(customersFeeds);

        List<CustomerPaymentInfo> customersPaymentsInfo = createCustomersPaymentsInfo(customers);
        customerPaymentInfoRepository.saveAll(customersPaymentsInfo);

    }

    private List<Customer> createCustomers()
    {
        List<Customer> customers = new ArrayList<>();

        for (int i = 0; i < 50; i++)
        {
            customers.add(
                    new Customer(
                            faker.name().username(),
                            faker.crypto().md5(),
                            faker.phoneNumber().phoneNumber(),
                            faker.funnyName() + "@gmail.com",
                            faker.name().lastName()
                    ));
        }

        customers.forEach(System.out::println);

        return customers;
    }

    private List<CustomerCart> createCustomersCarts(List<Customer> customers)
    {
        List<CustomerCart> customersCarts = new ArrayList<>();

        for (Customer customer : customers)
        {
            boolean active = faker.random().nextBoolean();
            customersCarts.add(
                    new CustomerCart(
                            customer.getId(),
                            active,
                            active ?
                                    faker.date().future(3, TimeUnit.HOURS) :
                                    faker.date().past(3, TimeUnit.HOURS)
                            ,
                            faker.random().nextInt(1, 1000),
                            faker.random().nextInt(1, 10)
                    )
            );
        }

        return customersCarts;
    }

    private List<CustomerFeed> createCustomersFeeds(List<Customer> customers)
    {
        List<CustomerFeed> customersFeeds = new ArrayList<>();

        for (Customer customer : customers)
        {
            new CustomerFeed(
                    customer.getId(),
                    productDummyData.getRandomListOfProductInfo(),
                    productDummyData.getRandomListOfProductInfo(),
                    productDummyData.getRandomProductInfo(),
                    productDummyData.getRandomProductInfo(),
                    productDummyData.getRandomListOfProductInfo()
            );
        }

        return customersFeeds;
    }

    private List<CustomerPaymentInfo> createCustomersPaymentsInfo(List<Customer> customers)
    {
        List<CustomerPaymentInfo> customersPaymentsInfo = new ArrayList<>();

        for (Customer customer : customers)
        {
            customersPaymentsInfo.add(
                    new CustomerPaymentInfo(
                            customer.getId(),
                            createCustomersCreditCards()
                    )
            );
        }

        return customersPaymentsInfo;
    }

    private List<CreditCard> createCustomersCreditCards()
    {
        List<CreditCard> creditCards = new ArrayList<>();
        int numOfCreditCards = faker.random().nextInt(1, 5);

        for (int i = 0; i < numOfCreditCards; i++)
        {
            Date date = faker.date().future(1000, TimeUnit.DAYS);
            creditCards.add(
                    new CreditCard(
                            faker.name().fullName(),
                            faker.business().creditCardNumber(),
                            faker.business().creditCardType(),
                            faker.currency().code(),
                            date.getMonth(),
                            date.getYear(),
                            faker.random().nextBoolean(),
                            createBillingAddress()
                    )
            );
        }

        return creditCards;
    }

    private Address createBillingAddress()
    {
        return new Address(
                faker.name().fullName(),
                faker.address().fullAddress(),
                faker.address().city(),
                faker.address().state(),
                faker.address().zipCode(),
                faker.phoneNumber().phoneNumber(),
                faker.superhero().name()
        );
    }
}
