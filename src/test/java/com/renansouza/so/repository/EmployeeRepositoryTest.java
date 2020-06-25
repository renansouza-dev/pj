package com.renansouza.so.repository;

import com.renansouza.so.models.Employee;
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
class EmployeeRepositoryTest {

    @Autowired
    EmployeeRepository repository;
    Employee employee;

    @BeforeEach
    public void setUp() {
        employee = new Employee();
        employee.setName("Receptionist 1");
    }

    @Test
    void saveAndFindById() {
        assertThat(repository.findAll()).isEmpty();
        Employee expected = repository.save(employee);
        assertThat(repository.findById(expected.getId())).hasValue(employee);

    }

    @Test
    void saveAndFindByNameIgnoreCase() {
        assertThat(repository.findAll()).isEmpty();
        repository.save(employee);
        assertThat(repository.findByNameIgnoreCase(java.util.Optional.of("receptionist 1"))).isPresent();
    }

    @Test
    void saveAndFindAll() {
        assertThat(repository.findAll()).isEmpty();
        repository.save(employee);

        Iterable<Employee> actual = repository.findAll();
        assertThat(actual.iterator().hasNext()).isTrue();
        assertThat(actual.iterator().next().getName()).isEqualTo("Receptionist 1");
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