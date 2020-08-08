package com.mans.ecommerce.b2c.repository.customer;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.mans.ecommerce.b2c.domain.entity.customer.Cart;
import com.mans.ecommerce.b2c.repository.customer.custom.CartRepositoryCustom;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository  extends CartRepositoryCustom, MongoRepository<Cart,ObjectId>
{
    <S extends Cart> List<S> saveAll(Iterable<S> entities);

    <S extends Cart> S save(S entity);

    @Override Optional<Cart> findById(ObjectId objectId);

    boolean extendsExpirationDateAndGetActivationStatus(ObjectId id, Date date);
}
