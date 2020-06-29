package com.renansouza.so.employee;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;

@RepositoryRestResource(collectionResourceRel = "employee", path = "employee")
public interface EmployeeRepository extends PagingAndSortingRepository<Employee, Integer> {

    Optional<Employee> findByNameIgnoreCase(Optional<String> name);

    boolean existsEmployeeByNameIgnoreCase(Optional<String> name);

    boolean existsEmployeeById(int id);

}