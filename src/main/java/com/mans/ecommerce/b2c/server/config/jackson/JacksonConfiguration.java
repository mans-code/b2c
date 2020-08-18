package com.mans.ecommerce.b2c.server.config.jackson;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.bson.types.ObjectId;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.web.reactive.config.WebFluxConfigurer;

@Configuration
public class JacksonConfiguration
{

    @Bean
    Jackson2ObjectMapperBuilder jackson2ObjectMapperBuilder()
    {

        return new Jackson2ObjectMapperBuilder()
                       .serializationInclusion(JsonInclude.Include.NON_EMPTY)
                       .serializationInclusion(JsonInclude.Include.NON_NULL)
                       .serializerByType(LocalDateTime.class, new LocalDateTimeJson.LocalDateTimeSerializer())
                       .serializerByType(Instant.class, new InstantJson.InstanceSerializer())
                       .serializerByType(LocalDate.class, new LocalDateJson.LocalDateSerializer())
                       .serializerByType(ObjectId.class, new ObjectIdJson.ObjectIDSerializer())
                       .deserializerByType(LocalDate.class, new LocalDateJson.LocalDateDeserializer())
                       .deserializerByType(Instant.class, new InstantJson.InstanceDeserializer())
                       .deserializerByType(LocalDateTime.class, new LocalDateTimeJson.LocalDateTimeDeserializer())
                       .deserializerByType(ObjectId.class, new ObjectIdJson.ObjectIDDeserializer());
    }

    @Bean
    Jackson2JsonEncoder jackson2JsonEncoder(ObjectMapper mapper)
    {
        return new Jackson2JsonEncoder(mapper);
    }

    @Bean
    Jackson2JsonDecoder jackson2JsonDecoder(ObjectMapper mapper)
    {
        return new Jackson2JsonDecoder(mapper);
    }

    @Bean
    WebFluxConfigurer webFluxConfigurer(Jackson2JsonEncoder encoder, Jackson2JsonDecoder decoder)
    {
        return new WebFluxConfigurer()
        {
            @Override
            public void configureHttpMessageCodecs(ServerCodecConfigurer configurer)
            {
                configurer.defaultCodecs().jackson2JsonEncoder(encoder);
                configurer.defaultCodecs().jackson2JsonDecoder(decoder);
            }
        };
    }
}
