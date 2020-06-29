package com.renansouza.so.product;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@AllArgsConstructor
public class ProductService {

    private final ProductRepository repository;

    List<Product> all() {
        return StreamSupport.stream(repository.findAll().spliterator(), false).collect(Collectors.toList());
    }

    Optional<Product> one(int id) {
        if (id <= 0) {
            throw new ProductNotFoundException("Cannot search for a product with id '" + id + "'.");
        }

        return repository.findById(id);
    }

    public Optional<Product> oneByName(String name) {
        if (Objects.isNull(name)) {
            throw new ProductNotFoundException("Cannot search for a product with name 'null'.");
        }

        return repository.findByDescriptionIgnoreCase(Optional.of(name));
    }

    Product addProduct(final Product product) {
        if (product == null || StringUtils.isEmpty(product.getDescription())) {
            throw new IllegalArgumentException("Cannot insert a product with null data.");
        }

        if (repository.existsProductByDescriptionIgnoreCase(Optional.of(product.getDescription()))) {
            throw new IllegalArgumentException("Product already added.");
        }

        return repository.save(product);
    }

    Product updateProduct(final int id, final Product product) {
        if (!repository.existsProductById(id)) {
            throw new ProductNotFoundException(id);
        }

        final Optional<Product> productToUpdate = repository.findById(id);
        if (!productToUpdate.isPresent()) {
            throw new ProductNotFoundException(id);
        }

        productToUpdate.get().setDescription(product.getDescription());
        return repository.save(productToUpdate.get());
    }

}
