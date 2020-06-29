package com.renansouza.so.client;

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
class ClientServiceTest {

    @MockBean
    ClientRepository repository;

    @Autowired
    private ClientService service;
    Client client;

    @BeforeEach
    void setUp() {
        client = new Client();
        client = new Client();
        client.setName("Client");
        client.setZipcode(89200000);
        client.setPhone("47999999999");
        client.setEmail("client@mail.com");
        client.setAddress("123 Main Street");
    }

    @Test
    void findAll() {
        when(repository.findAll()).thenReturn(Collections.singletonList(client));

        assertThat(repository.findAll()).isNotEmpty();
    }

    @Test
    void findById() {
        when(repository.findById(anyInt())).thenReturn(Optional.of(client));

        final Optional<Client> expected = service.one(1);

        assertThat(expected).isPresent();
        assertThat(expected.get().getName()).isEqualTo(client.getName());
    }

    @Test
    void addClient() {
        when(repository.existsClientByNameIgnoreCase(Optional.of(anyString()))).thenReturn(false);
        when(repository.save(any(Client.class))).thenReturn(client);

        final Client expected = service.addClient(client);

        assertThat(expected).isNotNull();
        assertThat(expected.getName()).isEqualTo(client.getName());
    }

    @Test
    void updateClient() {
        when(repository.existsClientById(anyInt())).thenReturn(true);
        when(repository.findById(anyInt())).thenReturn(Optional.ofNullable(client));
        when(repository.save(any(Client.class))).thenReturn(client);

        client.setName("Another Client");
        final Client expected = service.updateClient(1, client);

        assertThat(expected).isNotNull();
        assertThat(expected.getName()).isEqualTo(client.getName());
    }

    @Test
    void failToUpdateWithUnknownId() {
        final ClientNotFoundException exception = assertThrows(ClientNotFoundException.class, () -> {
            when(repository.existsClientById(anyInt())).thenReturn(false);

            service.updateClient(1, client);
        });

        assertThat(exception.getMessage()).containsIgnoringCase("Could not find client ");
    }

    @Test
    void failToUpdateWithKnownId() {
        final ClientNotFoundException exception = assertThrows(ClientNotFoundException.class, () -> {
            when(repository.existsClientById(anyInt())).thenReturn(true);
            when(repository.findById(anyInt())).thenReturn(Optional.empty());

            service.updateClient(1, client);
        });

        assertThat(exception.getMessage()).containsIgnoringCase("Could not find client ");
    }

    @Test
    void failToSaveWithDuplicateRecord() {
        final IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            when(repository.existsClientByNameIgnoreCase(any())).thenReturn(true);

            service.addClient(client);
        });

        assertThat(exception.getMessage()).containsIgnoringCase("Client already added.");
    }

    @Test
    void failToSaveWithNull() {
        final IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            service.addClient(null);
        });

        assertThat(exception.getMessage()).containsIgnoringCase("Cannot insert a client with null data.");
    }

    @Test
    void failToFindWithWrongId() {
        final ClientNotFoundException exception = assertThrows(ClientNotFoundException.class, () -> {
            service.one(0);
        });

        assertThat(exception.getMessage()).containsIgnoringCase("Cannot search for a client with id");
    }
}