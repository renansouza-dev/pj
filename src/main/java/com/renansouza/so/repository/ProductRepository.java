package com.renansouza.so.repository;

import com.renansouza.so.models.Product;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ProductRepository extends CrudRepository<Product, Integer> {

    Optional<Product> findByDescriptionIgnoreCase(Optional<String> description);

    @Query("select p from Product p where p.type = :type")
    Optional<Product> findByTypeIgnoreCase(String type);

    @Query("select p from Product p where p.brand = :brand")
    Optional<Product> findByBrandIgnoreCase(String brand);

}