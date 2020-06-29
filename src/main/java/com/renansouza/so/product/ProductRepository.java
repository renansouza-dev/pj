package com.renansouza.so.product;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;

@RepositoryRestResource(collectionResourceRel = "product", path = "product")
public interface ProductRepository extends PagingAndSortingRepository<Product, Integer> {

    Optional<Product> findByDescriptionIgnoreCase(Optional<String> description);

    Optional<Product> findByTypeIgnoreCase(String type);

    Optional<Product> findByBrandIgnoreCase(String brand);

    boolean existsProductByDescriptionIgnoreCase(Optional<String> name);

    boolean existsProductById(int id);

}