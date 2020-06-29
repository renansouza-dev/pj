package com.renansouza.so.employee;

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
public class EmployeeService {

    private final EmployeeRepository repository;

    List<Employee> all() {
        return StreamSupport.stream(repository.findAll().spliterator(), false).collect(Collectors.toList());
    }

    Optional<Employee> one(int id) {
        if (id <= 0) {
            throw new EmployeeNotFoundException("Cannot search for an employee with id '" + id + "'.");
        }

        return repository.findById(id);
    }

    public Optional<Employee> oneByName(String name) {
        if (Objects.isNull(name)) {
            throw new EmployeeNotFoundException("Cannot search for a employee with name 'null'.");
        }

        return repository.findByNameIgnoreCase(Optional.of(name));
    }

    Employee add(final Employee employee) {
        if (employee == null || StringUtils.isEmpty(employee.getName())) {
            throw new IllegalArgumentException("Cannot insert an employee with null data.");
        }

        if (repository.existsEmployeeByNameIgnoreCase(Optional.of(employee.getName()))) {
            throw new IllegalArgumentException("Employee already added.");
        }

        return repository.save(employee);
    }
    
    Employee update(final int id, final Employee employee) {
        final Employee employeeToUpdate = get(id);

        employeeToUpdate.setName(employee.getName());
        return repository.save(employeeToUpdate);
    }

    Employee delete(final int id) {
        final Employee employeeToUpdate = get(id);

        employeeToUpdate.setActive(false);
        return repository.save(employeeToUpdate);
    }

    private Employee get(int id) {
        if (!repository.existsEmployeeById(id)) {
            throw new EmployeeNotFoundException(id);
        }

        final Optional<Employee> employeeToUpdate = repository.findById(id);
        if (!employeeToUpdate.isPresent()) {
            throw new EmployeeNotFoundException(id);
        }
        return employeeToUpdate.get();

    }

}
