package com.renansouza.so.repository;

import com.renansouza.so.models.Employee;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface EmployeeRepository extends CrudRepository<Employee, Integer> {

    Optional<Employee> findByNameIgnoreCase(Optional<String> name);

}