package com.mans.ecommerce.b2c.server.config.mongo;

import javax.annotation.PostConstruct;

import com.mans.ecommerce.b2c.domain.entity.customer.Customer;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.index.Index;

@Configuration
@DependsOn("reactiveMongoTemplate")
public class Indexing
{
    private ReactiveMongoTemplate reactiveMongoTemplate;

    Indexing(ReactiveMongoTemplate reactiveMongoTemplate)
    {
        this.reactiveMongoTemplate = reactiveMongoTemplate;
    }

    @PostConstruct
    public void initIndexes()
    {
        reactiveMongoTemplate.indexOps(Customer.class)
                             .ensureIndex(
                                     new Index().unique()
                                                .named("username")
                                                .on("username", Sort.Direction.ASC)
                             ).subscribe();
    }
}
