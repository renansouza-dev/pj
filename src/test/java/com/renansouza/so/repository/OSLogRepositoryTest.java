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
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
@ExtendWith(SpringExtension.class)
class OSLogRepositoryTest {

    @Autowired
    private OSLogRepository repository;
    @Autowired
    private OSRepository osRepository;
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private ProductRepository productRepository;
    OS os;
    OSLog osLog;
    Client client;
    Product product;
    Employee employee;

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
        employee.setName("Technician 1");

        employee = employeeRepository.save(employee);

        product = new Product();
        product.setDescription("Product");
        product.setType("PC");
        product.setBrand("Brand");

        product = productRepository.save(product);

        final LocalDateTime start = LocalDateTime.now();
        final LocalDateTime end = start.plusDays(1);

        osLog = new OSLog();
        osLog.setEmployee(employee);
        osLog.setMessage("Fixed");
        osLog.setStart(start);
        osLog.setEnd(end);

        os = new OS();
        os.setDefect("Broken");
        os.setLogs(List.of(osLog));
        os.setClient(client);
        os.setEmployee(employee);
        os.setProduct(product);

        osLog.setOs(os);
    }

    @Test
    void findByMessageContainingIgnoreCase() {
        assertThat(repository.findAll()).isEmpty();
        osRepository.save(os);

        final List<OSLog> expected = repository.findByMessageContainingIgnoreCase(osLog.getMessage());
        assertThat(expected).isNotEmpty();
        assertThat(expected.get(0).getOs()).isEqualTo(osLog.getOs());
        assertThat(expected.get(0).getEnd()).isEqualTo(osLog.getEnd());
        assertThat(expected.get(0).getStart()).isEqualTo(osLog.getStart());
        assertThat(expected.get(0).getMessage()).isEqualTo(osLog.getMessage());
        assertThat(expected.get(0).getEmployee()).isEqualTo(osLog.getEmployee());
    }

    @Test
    void findById() {
        assertThat(repository.findAll()).isEmpty();
        employeeRepository.save(employee);
        final OS actualOs = osRepository.save(os);

        final Optional<OSLog> actual = repository.findById(actualOs.getLogs().get(0).getId());
        assertThat(actual).isPresent();
        assertThat(actual.get()).isEqualTo(os.getLogs().get(0));
    }

    @Test
    void failToSaveWithoutOs() {
        final DataIntegrityViolationException exception = assertThrows(DataIntegrityViolationException.class, () -> {
            assertThat(repository.findAll()).isEmpty();

            osLog.setOs(null);
            employeeRepository.save(employee);
            osRepository.save(os);
        });

        assertThat(exception.getMessage()).containsIgnoringCase("not-null property references");
    }

}