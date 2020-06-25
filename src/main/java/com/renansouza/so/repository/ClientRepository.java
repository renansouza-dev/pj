package com.renansouza.so.repository;

import com.renansouza.so.models.Client;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ClientRepository extends CrudRepository<Client, Integer> {

    Optional<Client> findByNameIgnoreCase(Optional<String> name);
    Optional<Client> findByZipcode(int zipcode);

}