package com.renansouza.so.employee;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(SpringExtension.class)
class EmployeeServiceTest {

    @MockBean
    EmployeeRepository repository;

    @Autowired
    private EmployeeService service;
    Employee employee;

    @BeforeEach
    void setUp() {
        employee = new Employee();
        employee.setName("Receptionist");
        employee.setActive(true);
    }

    @Test
    void findAllPageable() {
        List<Employee> employees = new ArrayList<>();
        employees.add(employee);
        Page<Employee> employeePage = new PageImpl(employees);

        when(repository.findAll(any(Pageable.class))).thenReturn(employeePage);

        Pageable pageable = PageRequest.of(0, 10);
        final Page<Employee> page = repository.findAll(pageable);

        assertThat(page).isNotEmpty();
        assertThat(page.getTotalElements()).isEqualTo(1);
    }

    @Test
    void findById() {
        when(repository.findById(anyInt())).thenReturn(Optional.of(employee));

        final Optional<Employee> expected = service.one(1);

        assertThat(expected).isPresent();
        assertThat(expected.get().getName()).isEqualTo("Receptionist");
    }

    @Test
    void addEmployee() {
        when(repository.existsEmployeeByNameIgnoreCase(Optional.of(anyString()))).thenReturn(false);
        when(repository.save(any(Employee.class))).thenReturn(employee);

        final Employee expected = service.add(employee);

        assertThat(expected).isNotNull();
        assertThat(expected.getName()).isEqualTo("Receptionist");
    }

    @Test
    void updateEmployee() {
        when(repository.existsEmployeeById(anyInt())).thenReturn(true);
        when(repository.findById(anyInt())).thenReturn(Optional.ofNullable(employee));
        when(repository.save(any(Employee.class))).thenReturn(employee);

        employee.setName("Technician");
        final Employee expected = service.update(1, employee);

        assertThat(expected).isNotNull();
        assertThat(expected.getName()).isEqualTo("Technician");
    }

    @Test
    void deleteEmployee() {
        when(repository.existsEmployeeById(anyInt())).thenReturn(true);
        when(repository.findById(anyInt())).thenReturn(Optional.ofNullable(employee));
        when(repository.save(any(Employee.class))).thenReturn(employee);

        employee.setActive(false);
        final Employee expected = service.delete(1);

        assertThat(expected).isNotNull();
        assertThat(expected.isActive()).isFalse();
    }

    @Test
    void failToUpdateWithUnknownId() {
        final EmployeeNotFoundException exception = assertThrows(EmployeeNotFoundException.class, () -> {
            when(repository.existsEmployeeById(anyInt())).thenReturn(false);

            service.update(1, employee);
        });

        assertThat(exception.getMessage()).containsIgnoringCase("Could not find employee ");
    }

    @Test
    void failToUpdateWithKnownId() {
        final EmployeeNotFoundException exception = assertThrows(EmployeeNotFoundException.class, () -> {
            when(repository.existsEmployeeById(anyInt())).thenReturn(true);
            when(repository.findById(anyInt())).thenReturn(Optional.empty());

            service.update(1, employee);
        });

        assertThat(exception.getMessage()).containsIgnoringCase("Could not find employee ");
    }

    @Test
    void failToSaveWithDuplicateRecord() {
        final IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            when(repository.existsEmployeeByNameIgnoreCase(any())).thenReturn(true);

            service.add(employee);
        });

        assertThat(exception.getMessage()).containsIgnoringCase("Employee already added.");
    }

    @Test
    void failToSaveWithNull() {
        final IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            service.add(null);
        });

        assertThat(exception.getMessage()).containsIgnoringCase("Cannot insert an employee with null data.");
    }

    @Test
    void failToFindWithWrongId() {
        final EmployeeNotFoundException exception = assertThrows(EmployeeNotFoundException.class, () -> {
            service.one(0);
        });

        assertThat(exception.getMessage()).containsIgnoringCase("Cannot search for an employee with id");
    }
}