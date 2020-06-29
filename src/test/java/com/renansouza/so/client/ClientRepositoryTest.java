package com.renansouza.so.client;

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
class ClientRepositoryTest {

    @Autowired
    private ClientRepository repository;
    Client client;

    @BeforeEach
    void setUp() {
        client = new Client();
        client.setName("Client");
        client.setZipcode(89200000);
        client.setPhone("47999999999");
        client.setEmail("client@mail.com");
        client.setAddress("123 Main Street");
    }

    @Test
    void findById() {
        assertThat(repository.findAll()).isEmpty();
        final Client actual = repository.save(client);

        assertThat(repository.findById(actual.getId())).isPresent();
    }

    @Test
    void findByNameIgnoreCase() {
        assertThat(repository.findByNameIgnoreCase(java.util.Optional.ofNullable(client.getName()))).isEmpty();
        repository.save(client);
        assertThat(repository.findByNameIgnoreCase(java.util.Optional.ofNullable(client.getName()))).isPresent();
    }

    @Test
    void existsClientByNameIgnoreCase() {
        assertThat(repository.findAll()).isEmpty();
        repository.save(client);
        assertThat(repository.existsClientByNameIgnoreCase(java.util.Optional.of("Client"))).isNotNull();
    }

    @Test
    void existsFindByNameIgnoreCase() {
        assertThat(repository.findAll()).isEmpty();
        repository.save(client);
        assertThat(repository.existsClientById(1)).isNotNull();
    }

    @Test
    void updateClient() {
        assertThat(repository.findAll()).isEmpty();
        repository.save(client);

        client.setName("Another Client");
        final Client actual = repository.save(client);

        assertThat(actual).isNotNull();
        assertThat(actual.getName()).isEqualTo(client.getName());
    }

    @Test
    void failToSaveWithoutName() {
        final DataIntegrityViolationException exception = assertThrows(DataIntegrityViolationException.class, () -> {
            assertThat(repository.findAll()).isEmpty();

            client.setName(null);
            repository.save(client);
        });

        assertThat(exception.getMessage()).containsIgnoringCase("not-null property references");
    }

}