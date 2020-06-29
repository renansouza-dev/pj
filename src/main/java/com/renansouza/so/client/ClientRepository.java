package com.renansouza.so.client;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;

@RepositoryRestResource(collectionResourceRel = "client", path = "client")
public interface ClientRepository extends PagingAndSortingRepository<Client, Integer> {

    Optional<Client> findByNameIgnoreCase(Optional<String> name);

    boolean existsClientByNameIgnoreCase(Optional<String> name);

    boolean existsClientById(int id);

}