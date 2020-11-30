package com.mans.ecommerce.b2c.server;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanFactory
{
    private final Contact CONTACT = new Contact()
                                            .name("Mans")
                                            .email("mans.coder@yahoo.com")
                                            .url("https://mans100.web.app/");

    @Bean
    public OpenAPI springShopOpenAPI()
    {
        return new OpenAPI()
                       .info(new Info().title("SpringShop API")
                                       .description("Spring shop sample application")
                                       .version("v0.0.1")
                                       .license(new License().name("Apache 2.0").url("http://springdoc.org")))

                       .externalDocs(new ExternalDocumentation()
                                             .description("SpringShop Wiki Documentation")
                                             .url("https://springshop.wiki.github.org/docs"));
    }
}
