package com.renansouza.so.employee;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.transaction.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
@ExtendWith(SpringExtension.class)
class EmployeeRepositoryTest {

    @Autowired
    EmployeeRepository repository;
    Employee employee;

    @BeforeEach
    public void setUp() {
        employee = new Employee();
        employee.setName("Receptionist 1");
        employee.setActive(true);
    }

    @Test
    void findAll() {
        assertThat(repository.findAll()).isEmpty();
        repository.save(employee);

        Pageable pageable = PageRequest.of(0, 10);
        final Page<Employee> page = repository.findAll(pageable);

        assertThat(page).isNotEmpty();
        assertThat(page.getTotalElements()).isEqualTo(1);
    }

    @Test
    void findById() {
        assertThat(repository.findAll()).isEmpty();
        Employee expected = repository.save(employee);
        assertThat(repository.findById(expected.getId())).hasValue(employee);

    }

    @Test
    void findByNameIgnoreCase() {
        assertThat(repository.findAll()).isEmpty();
        repository.save(employee);
        assertThat(repository.findByNameIgnoreCase(java.util.Optional.of("receptionist 1"))).isPresent();
    }

    @Test
    void existsEmployeeByNameIgnoreCase() {
        assertThat(repository.findAll()).isEmpty();
        repository.save(employee);
        assertThat(repository.existsEmployeeByNameIgnoreCase(java.util.Optional.of("receptionist 1"))).isNotNull();
    }

    @Test
    void existsFindByNameIgnoreCase() {
        assertThat(repository.findAll()).isEmpty();
        repository.save(employee);
        assertThat(repository.existsEmployeeById(1)).isNotNull();
    }

    @Test
    void updateEmployee() {
        assertThat(repository.findAll()).isEmpty();
        repository.save(employee);

        employee.setActive(false);
        final Employee actual = repository.save(employee);

        assertThat(actual).isNotNull();
        assertThat(actual.isActive()).isFalse();
    }

    @Test
    void failToSaveWithoutName() {
        final DataIntegrityViolationException exception = assertThrows(DataIntegrityViolationException.class, () -> {
            assertThat(repository.findAll()).isEmpty();

            employee.setName(null);
            repository.save(employee);
        });

        assertThat(exception.getMessage()).containsIgnoringCase("not-null property references");
    }

}