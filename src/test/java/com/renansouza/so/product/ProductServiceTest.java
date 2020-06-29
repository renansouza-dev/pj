package com.renansouza.so.product;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(SpringExtension.class)
class ProductServiceTest {

    @MockBean
    ProductRepository repository;

    @Autowired
    private ProductService service;
    Product product;

    @BeforeEach
    void setUp() {
        product = new Product();
        product.setDescription("Product");
        product.setBrand("Brand");
        product.setType("PC");
    }

    @Test
    void findAll() {
        when(repository.findAll()).thenReturn(Collections.singletonList(product));

        assertThat(service.all()).isNotEmpty();
    }

    @Test
    void findById() {
        when(repository.findById(anyInt())).thenReturn(Optional.of(product));

        final Optional<Product> expected = service.one(1);

        assertThat(expected).isPresent();
        assertThat(expected.get().getDescription()).isEqualTo(product.getDescription());
    }

    @Test
    void addProduct() {
        when(repository.existsProductByDescriptionIgnoreCase(Optional.of(anyString()))).thenReturn(false);
        when(repository.save(any(Product.class))).thenReturn(product);

        final Product expected = service.addProduct(product);

        assertThat(expected).isNotNull();
        assertThat(expected.getDescription()).isEqualTo(product.getDescription());
    }

    @Test
    void updateProduct() {
        when(repository.existsProductById(anyInt())).thenReturn(true);
        when(repository.findById(anyInt())).thenReturn(Optional.ofNullable(product));
        when(repository.save(any(Product.class))).thenReturn(product);

        product.setDescription("Another Product");
        final Product expected = service.updateProduct(1, product);

        assertThat(expected).isNotNull();
        assertThat(expected.getDescription()).isEqualTo(product.getDescription());
    }

    @Test
    void failToUpdateWithUnknownId() {
        final ProductNotFoundException exception = assertThrows(ProductNotFoundException.class, () -> {
            when(repository.existsProductById(anyInt())).thenReturn(false);

            service.updateProduct(1, product);
        });

        assertThat(exception.getMessage()).containsIgnoringCase("Could not find product ");
    }

    @Test
    void failToUpdateWithKnownId() {
        final ProductNotFoundException exception = assertThrows(ProductNotFoundException.class, () -> {
            when(repository.existsProductById(anyInt())).thenReturn(true);
            when(repository.findById(anyInt())).thenReturn(Optional.empty());

            service.updateProduct(1, product);
        });

        assertThat(exception.getMessage()).containsIgnoringCase("Could not find product ");
    }

    @Test
    void failToSaveWithDuplicateRecord() {
        final IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            when(repository.existsProductByDescriptionIgnoreCase(any())).thenReturn(true);

            service.addProduct(product);
        });

        assertThat(exception.getMessage()).containsIgnoringCase("Product already added.");
    }

    @Test
    void failToSaveWithNull() {
        final IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            service.addProduct(null);
        });

        assertThat(exception.getMessage()).containsIgnoringCase("Cannot insert a product with null data.");
    }

    @Test
    void failToFindWithWrongId() {
        final ProductNotFoundException exception = assertThrows(ProductNotFoundException.class, () -> {
            service.one(0);
        });

        assertThat(exception.getMessage()).containsIgnoringCase("Cannot search for a product with id");
    }
}