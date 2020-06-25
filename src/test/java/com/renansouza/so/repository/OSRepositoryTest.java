package com.renansouza.so.repository;

import com.renansouza.so.models.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.transaction.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
@ExtendWith(SpringExtension.class)
class OSRepositoryTest {

    @Autowired
    private OSRepository repository;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private ProductRepository productRepository;
    OS os;
    Client client;
    Employee employee;
    Product product;

    @BeforeEach
    void setUp() {
        client = new Client();
        client.setName("Client");
        client.setZipcode(89200000);
        client.setEmail("email@email.com");
        client.setPhone("47999999999");
        client.setAddress("1 Main Street");

        client = clientRepository.save(client);

        employee = new Employee();
        employee.setName("Receptionist");

        employee = employeeRepository.save(employee);

        product = new Product();
        product.setDescription("Product");
        product.setType("PC");
        product.setBrand("Brand");

        product = productRepository.save(product);

        os = new OS();
        os.setDefect("Broken");
        os.setClient(client);
        os.setEmployee(employee);
        os.setProduct(product);
    }

    @Test
    void findByDefectContainingIgnoreCase() {
        assertThat(repository.findAll()).isEmpty();
        repository.save(os);

        final List<OS> actual = repository.findByDefectContainingIgnoreCase(os.getDefect());
        assertThat(actual).isNotEmpty();
        assertThat(actual.get(0)).isEqualTo(os);
    }

    @Test
    void findByStatus() {
        assertThat(repository.findAll()).isEmpty();

        os.setStatus(Status.IN_PROGESS);
        repository.save(os);

        final List<OS> actual = repository.findByStatus(os.getStatus());
        assertThat(actual).isNotEmpty();
        assertThat(actual.get(0)).isEqualTo(os);
    }

    @Test
    void findByEmployee() {
        assertThat(repository.findAll()).isEmpty();

        repository.save(os);

        final List<OS> actual = repository.findByEmployeeName(os.getEmployee().getName());
        assertThat(actual).isNotEmpty();
        assertThat(actual.get(0)).isEqualTo(os);
    }

    @Test
    void findByClient() {
        assertThat(repository.findAll()).isEmpty();

        repository.save(os);

        final List<OS> actual = repository.findByClientName(os.getClient().getName());
        assertThat(actual).isNotEmpty();
        assertThat(actual.get(0)).isEqualTo(os);
    }

    @Test
    void findByProduct() {
        assertThat(repository.findAll()).isEmpty();

        repository.save(os);

        final List<OS> actual = repository.findByProductDescription(os.getProduct().getDescription());
        assertThat(actual).isNotEmpty();
        assertThat(actual.get(0)).isEqualTo(os);
    }

    @Test
    void findById() {
        assertThat(repository.findAll()).isEmpty();

        os.setStatus(Status.IN_PROGESS);
        final OS actual = repository.save(os);

        final Optional<OS> expected = repository.findById(actual.getId());
        assertThat(expected).isPresent();
        assertThat(expected.get()).isEqualTo(actual);
    }

    @Test
    void failToSaveWithoutOs() {
        final DataIntegrityViolationException exception = assertThrows(DataIntegrityViolationException.class, () -> {
            assertThat(repository.findAll()).isEmpty();

            os.setDefect(null);
            repository.save(os);
        });

        assertThat(exception.getMessage()).containsIgnoringCase("not-null property references");
    }
}