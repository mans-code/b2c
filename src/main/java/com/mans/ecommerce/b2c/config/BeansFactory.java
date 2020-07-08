package com.mans.ecommerce.b2c.config;

import java.util.Arrays;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;

@Configuration
public class BeansFactory
{
    @Bean
    public ObjectMapper objectMapper()
    {
        return new ObjectMapper()
                       .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

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
