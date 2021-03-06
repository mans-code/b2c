package com.mans.ecommerce.b2c.server.config.mongo;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

import com.mans.ecommerce.b2c.domain.enums.Currency;
import com.mans.ecommerce.b2c.domain.enums.PricingStrategy;
import com.mans.ecommerce.b2c.domain.enums.SortBy;
import org.bson.types.Decimal128;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@Configuration
@EnableMongoAuditing
public class CustomConverter
{

    public static class StringToSortConverter implements Converter<String, SortBy>
    {
        @Override
        public SortBy convert(String source)
        {
            try
            {
                return SortBy.valueOf(source);
            }
            catch (Exception e)
            {
                return SortBy.HELPFUL;
            }
        }
    }

    @WritingConverter
    static class PricingStrategyStringConverter implements Converter<PricingStrategy, String>
    {
        @Override public String convert(PricingStrategy source)
        {
            return source.getPricingStrategy();
        }
    }

    @ReadingConverter
    static class StringPricingStrategy implements Converter<String, PricingStrategy>
    {

        @Override public PricingStrategy convert(String source)
        {
            try
            {
                return PricingStrategy.valueOf(source);
            }
            catch (Exception e)
            {
                return PricingStrategy.STATIC;
            }
        }
    }

    @WritingConverter
    static class CurrencyStringConverter implements Converter<Currency, String>
    {

        @Override public String convert(Currency source)
        {
            return source.getCurrencyCode();
        }
    }

    @ReadingConverter
    static class StringCurrency implements Converter<String, Currency>
    {

        @Override public Currency convert(String source)
        {
            try
            {
                return Currency.valueOf(source);
            }
            catch (Exception e)
            {
                return Currency.USD;
            }
        }
    }

    @WritingConverter
    static class BigDecimalDecimal128Converter implements Converter<BigDecimal, Decimal128>
    {

        @Override
        public Decimal128 convert(@NotNull BigDecimal source)
        {
            return new Decimal128(source);
        }
    }

    @ReadingConverter
    static class Decimal128BigDecimalConverter implements Converter<Decimal128, BigDecimal>
    {

        @Override
        public BigDecimal convert(@NotNull Decimal128 source)
        {
            return source.bigDecimalValue();
        }

    }
}
