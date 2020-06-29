package com.renansouza.so.oslog;

import com.renansouza.so.client.Client;
import com.renansouza.so.client.ClientRepository;
import com.renansouza.so.employee.Employee;
import com.renansouza.so.employee.EmployeeRepository;
import com.renansouza.so.os.OS;
import com.renansouza.so.os.OSRepository;
import com.renansouza.so.product.Product;
import com.renansouza.so.product.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.transaction.Transactional;
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
    Employee technician;
    Employee receptionist;

    @BeforeEach
    void setUp() {
        client = new Client();
        client.setName("Client");
        client.setZipcode(89200000);
        client.setEmail("email@email.com");
        client.setPhone("47999999999");
        client.setAddress("1 Main Street");

        client = clientRepository.save(client);

        receptionist = new Employee();
        receptionist.setName("Receptionist");

        receptionist = employeeRepository.save(receptionist);

        technician = new Employee();
        technician.setName("Technician");

        technician = employeeRepository.save(technician);

        product = new Product();
        product.setDescription("Product");
        product.setType("PC");
        product.setBrand("Brand");

        product = productRepository.save(product);

        osLog = new OSLog();
        osLog.setEmployee(technician);
        osLog.setMessage("Fixed");

        os = new OS();
        os.setClient(client);
        os.setProduct(product);
        os.setDefect("Broken");
        os.setTechnician(technician);
        os.setReceptionist(receptionist);

        osRepository.save(os);

        osLog.setOs(os);
    }

    @Test
    void findById() {
        assertThat(repository.findAll()).isEmpty();
        final OSLog actualOSLog = repository.save(osLog);

        final Optional<OSLog> actual = repository.findById(actualOSLog.getId());
        assertThat(actual).isPresent();
        assertThat(actual.get().getOs()).isNotNull();
        assertThat(actual.get().getMessage()).isNotNull();
        assertThat(actual.get().getEmployee()).isNotNull();
        assertThat(actual.get().getOs()).isEqualTo(osLog.getOs());
    }

    @Test
    void existsOSLogById() {
        assertThat(repository.findAll()).isEmpty();
        final OSLog actualOSLog = repository.save(osLog);

        assertThat(repository.existsOSLogById(actualOSLog.getId())).isTrue();
    }

    @Test
    void failToSaveWithoutOs() {
        final DataIntegrityViolationException exception = assertThrows(DataIntegrityViolationException.class, () -> {
            assertThat(repository.findAll()).isEmpty();

            osLog.setOs(null);
            repository.save(osLog);
        });

        assertThat(exception.getMessage()).containsIgnoringCase("not-null property references");
    }

}