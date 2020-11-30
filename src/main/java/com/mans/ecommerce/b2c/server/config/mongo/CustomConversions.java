package com.mans.ecommerce.b2c.server.config.mongo;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;

@Configuration
public class CustomConversions
{
    @Bean
    public MongoCustomConversions mongoCustomConversions()
    {
        return new MongoCustomConversions(Arrays.asList(
                new CustomConverter.BigDecimalDecimal128Converter(),
                new CustomConverter.Decimal128BigDecimalConverter(),
                new CustomConverter.PricingStrategyStringConverter(),
                new CustomConverter.StringPricingStrategy()
        ));
    }
}
