package com.renansouza.so.oslog;

import com.renansouza.so.client.Client;
import com.renansouza.so.employee.Employee;
import com.renansouza.so.os.OS;
import com.renansouza.so.os.Status;
import com.renansouza.so.product.Product;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(SpringExtension.class)
class OSLogServiceTest {

    @MockBean
    OSLogRepository repository;

    @Autowired
    private OSLogService service;
    OS os;
    OSLog osLog;
    Client client;
    Product product;
    Employee technician;
    Employee receptionist;

    @BeforeEach
    void setUp() {
        receptionist = new Employee();
        receptionist.setName("Receptionist");

        technician = new Employee();
        technician.setName("Technician");

        client = new Client();
        client.setName("Client");
        client.setZipcode(89200000);
        client.setPhone("47999999999");
        client.setEmail("client@mail.com");
        client.setAddress("123 Main Street");

        product = new Product();
        product.setDescription("Product");
        product.setBrand("Brand");
        product.setType("PC");

        osLog = new OSLog();
        osLog.setEmployee(technician);
        osLog.setMessage("Fixed");

        os = new OS();
        os.setLogs(Collections.singletonList(osLog));
        os.setDefect("Broken");
        os.setProduct(product);
        os.setClient(client);
        os.setTechnician(technician);
        os.setReceptionist(receptionist);
        os.setStatus(Status.IN_PROGESS);

        osLog.setOs(os);
    }

    @AfterEach
    void tearDown() {
        repository = null;
        service = null;
        os = null;
        osLog = null;
        client = null;
        product = null;
        technician = null;
        receptionist = null;
    }

    @Test
    void all() {
        when(repository.findAll()).thenReturn(Collections.singletonList(osLog));

        assertThat(service.all()).isNotEmpty();
    }

    @Test
    void one() {
        when(repository.findById(anyInt())).thenReturn(java.util.Optional.ofNullable(osLog));

        final Optional<OSLog> actual = service.one(1);
        assertThat(actual).isPresent();
        assertThat(actual.get().getMessage()).isEqualTo(osLog.getMessage());
        assertThat(actual.get().getEmployee()).isEqualTo(osLog.getEmployee());
        assertThat(actual.get().getOs()).isEqualTo(osLog.getOs());
    }

    @Test
    void add() {
        when(repository.save(any(OSLog.class))).thenReturn(osLog);
        final OSLog actual = service.add(1, osLog);

        assertThat(actual).isEqualTo(osLog);
    }

    @Test
    void update() {
        when(repository.existsOSLogById(anyInt())).thenReturn(true);
        when(repository.findById(anyInt())).thenReturn(Optional.of(osLog));

        final Employee technician2 = new Employee();
        technician2.setName("Technician 2");

        osLog.setMessage("Fixed !");
        osLog.setEmployee(technician2);
        service.update(1, osLog);
    }

    @Test
    void delete() {
        os.setLogs(Collections.singletonList(osLog));
        when(repository.save(any(OSLog.class))).thenReturn(osLog);
        when(repository.existsOSLogById(anyInt())).thenReturn(true);
        when(repository.findById(anyInt())).thenReturn(Optional.ofNullable(osLog)).thenReturn(Optional.empty());

        service.deleteOSLog(1);
        assertThat(service.one(1)).isEmpty();
    }

    @Test
    void failToFindWithWrongId() {
        final OSLogNotFoundException exception = assertThrows(OSLogNotFoundException.class, () -> {
            service.one(0);
        });

        assertThat(exception.getMessage()).containsIgnoringCase("Cannot search for an os log with id");
    }

    @Test
    void failToSaveWithNull() {
        final IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            service.add(1, null);
        });

        assertThat(exception.getMessage()).containsIgnoringCase("Cannot insert an os log with null data.");
    }

    @Test
    void failToSaveWithNullMessage() {
        final IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            osLog.setMessage(null);
            service.add(1, osLog);
        });

        assertThat(exception.getMessage()).containsIgnoringCase("Cannot insert an os log with null data.");
    }

    @Test
    void failToDeleteWithIdUnknown() {
        final OSLogNotFoundException exception = assertThrows(OSLogNotFoundException.class, () -> {
            when(repository.existsOSLogById(anyInt())).thenReturn(false);

            service.deleteOSLog(1);
        });

        assertThat(exception.getMessage()).containsIgnoringCase("Could not find os log");
    }

    @Test
    void failToDeleteWithoutLog() {
        final OSLogNotFoundException exception = assertThrows(OSLogNotFoundException.class, () -> {
            when(repository.existsOSLogById(anyInt())).thenReturn(true);
            when(repository.findById(anyInt())).thenReturn(Optional.empty());

            service.deleteOSLog(1);
        });

        assertThat(exception.getMessage()).containsIgnoringCase("Could not find os log");
    }

    @Test
    void failToUpdateLog() {
        final IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            service.update(0, osLog);
        });

        assertThat(exception.getMessage()).containsIgnoringCase("Cannot insert an os log with null data.");
    }

}