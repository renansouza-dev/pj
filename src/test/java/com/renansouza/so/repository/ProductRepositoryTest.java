package com.renansouza.so.repository;

import com.renansouza.so.models.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.transaction.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
@ExtendWith(SpringExtension.class)
class ProductRepositoryTest {

    @Autowired
    private ProductRepository repository;
    Product product;

    @BeforeEach
    void setUp() {
        product = new Product();
        product.setDescription("Product");
        product.setBrand("Brand");
        product.setType("PC");
    }

    @Test
    void findByDescriptionIgnoreCase() {
        assertThat(repository.findByDescriptionIgnoreCase(java.util.Optional.of(product.getDescription()))).isEmpty();
        repository.save(product);
        assertThat(repository.findByDescriptionIgnoreCase(java.util.Optional.of(product.getDescription()))).isPresent();
    }

    @Test
    void findByTypeIgnoreCase() {
        assertThat(repository.findAll()).isEmpty();
    }

    @Test
    void findByBrandIgnoreCase() {
        assertThat(repository.findAll()).isEmpty();
        final Product actual = repository.save(product);

        assertThat(repository.findByBrandIgnoreCase(actual.getBrand())).isPresent();
        assertThat(actual.getDescription()).isEqualTo(product.getDescription());
        assertThat(actual.getBrand()).isEqualTo(product.getBrand());
        assertThat(actual.getType()).isEqualTo(product.getType());
    }

    @Test
    void failToSaveWithoutDescription() {
        final DataIntegrityViolationException exception = assertThrows(DataIntegrityViolationException.class, () -> {
            assertThat(repository.findAll()).isEmpty();

            product.setDescription(null);
            repository.save(product);
        });

        assertThat(exception.getMessage()).containsIgnoringCase("not-null property references");
    }
}