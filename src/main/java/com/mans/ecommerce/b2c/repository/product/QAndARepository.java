package com.mans.ecommerce.b2c.repository.product;

import java.util.List;

import com.mans.ecommerce.b2c.domain.entity.product.QAndA;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QAndARepository extends PagingAndSortingRepository<QAndA, String>
{
    <S extends QAndA> List<S> saveAll(Iterable<S> entities);

    <S extends QAndA> S save(S entity);

    List<QAndA> findBySku(String sku, Pageable pageable);
}
