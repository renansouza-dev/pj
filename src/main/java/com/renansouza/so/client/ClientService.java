package com.renansouza.so.client;

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
public class ClientService {

    private final ClientRepository repository;

    List<Client> all() {
        return StreamSupport.stream(repository.findAll().spliterator(), false).collect(Collectors.toList());
    }

    Optional<Client> one(int id) {
        if (id <= 0) {
            throw new ClientNotFoundException("Cannot search for a client with id '" + id + "'.");
        }

        return repository.findById(id);
    }

    public Optional<Client> oneByName(String name) {
        if (Objects.isNull(name)) {
            throw new ClientNotFoundException("Cannot search for a client with name 'null'.");
        }

        return repository.findByNameIgnoreCase(Optional.of(name));
    }

    Client addClient(final Client client) {
        if (client == null || StringUtils.isEmpty(client.getName())) {
            throw new IllegalArgumentException("Cannot insert a client with null data.");
        }

        if (repository.existsClientByNameIgnoreCase(Optional.of(client.getName()))) {
            throw new IllegalArgumentException("Client already added.");
        }

        return repository.save(client);
    }

    Client updateClient(final int id, final Client client) {
        if (!repository.existsClientById(id)) {
            throw new ClientNotFoundException(id);
        }

        final Optional<Client> clientToUpdate = repository.findById(id);
        if (!clientToUpdate.isPresent()) {
            throw new ClientNotFoundException(id);
        }

        clientToUpdate.get().setName(client.getName());
        return repository.save(clientToUpdate.get());
    }

}
