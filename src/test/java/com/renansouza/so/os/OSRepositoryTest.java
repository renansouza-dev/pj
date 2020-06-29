package com.renansouza.so.os;

import com.renansouza.so.client.Client;
import com.renansouza.so.client.ClientRepository;
import com.renansouza.so.employee.Employee;
import com.renansouza.so.employee.EmployeeRepository;
import com.renansouza.so.product.Product;
import com.renansouza.so.product.ProductRepository;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    Employee technician;
    Employee receptionist;
    Product product;

    @BeforeEach
    void setUp() {
        os = new OS();

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

        os.setClient(client);
        os.setProduct(product);
        os.setDefect("Broken");
        os.setTechnician(technician);
        os.setReceptionist(receptionist);
    }

    @Test
    void findAll() {
        assertThat(repository.findAll()).isEmpty();

        repository.save(os);

        final Iterable<OS> actual = repository.findAll();
        assertThat(actual).isNotEmpty();
        assertThat(actual.iterator().next()).isEqualTo(os);
    }

    @Test
    void existsOSById() {
        assertThat(repository.findAll()).isEmpty();

        final OS actual = repository.save(os);

        assertThat(repository.existsOSById(actual.getId())).isTrue();
    }

    @Test
    void update() {
        assertThat(repository.findAll()).isEmpty();

        final OS update = repository.save(os);
        update.setStatus(Status.IN_PROGESS);

        repository.save(update);

        final Optional<OS> actual = repository.findById(update.getId());
        assertThat(actual).isPresent();
        assertThat(actual.get()).isEqualTo(update);

    }

    @Test
    void delete() {
        assertThat(repository.findAll()).isEmpty();

        final OS update = repository.save(os);
        update.setStatus(Status.CANCELED);

        repository.save(update);

        final Optional<OS> actual = repository.findById(update.getId());
        assertThat(actual).isPresent();
        assertThat(actual.get()).isEqualTo(update);
    }

    @Test
    void findAllByTechnicianAndPending() {
        assertThat(repository.findAll()).isEmpty();

        final OS update = repository.save(os);

        final ArrayList<Status> statuses = Lists.newArrayList(Status.OPEN, Status.IN_PROGESS, Status.IMPEDED);
        List<OS> pending = repository.findByStatusIn(statuses);

        List<OS> collect = pending
                .stream()
                .filter(o -> o.getTechnician().getId().equals(this.os.getTechnician().getId()))
                .collect(Collectors.toList());

        assertThat(collect).isNotEmpty();
        assertThat(collect.get(0)).isEqualTo(this.os);

        update.setStatus(Status.CANCELED);
        repository.save(update);

        pending = repository.findByStatusIn(statuses);

        collect = pending
                .stream()
                .filter(o -> o.getTechnician().getId().equals(this.os.getTechnician().getId()))
                .collect(Collectors.toList());

        assertThat(collect).isEmpty();
    }


    @Test
    void findById() {
        assertThat(repository.findAll()).isEmpty();

        final OS actual = repository.save(os);

        final Optional<OS> expected = repository.findById(actual.getId());
        assertThat(expected).isPresent();
        assertThat(expected.get().getClient()).isEqualTo(client);
        assertThat(expected.get().getProduct()).isEqualTo(product);
        assertThat(expected.get().getDefect()).isEqualTo(os.getDefect());
        assertThat(expected.get().getStatus()).isEqualTo(os.getStatus());
        assertThat(expected.get().getTechnician()).isEqualTo(technician);
        assertThat(expected.get().getReceptionist()).isEqualTo(receptionist);
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