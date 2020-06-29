package com.renansouza.so.oslog;

import com.renansouza.so.AbstractMvcTest;
import com.renansouza.so.client.Client;
import com.renansouza.so.employee.Employee;
import com.renansouza.so.os.OS;
import com.renansouza.so.os.Status;
import com.renansouza.so.product.Product;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class OSLogControllerTest extends AbstractMvcTest {

    @Autowired
    private MockMvc mvc;
    OS os;
    OSLog osLog;
    Client client;
    Product product;
    Employee technician;
    Employee receptionist;

    private static final String DOMAIN = "oslogs";

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
        osLog.setMessage("Working on it!");

        os = new OS();
        os.setClient(client);
        os.setProduct(product);
        os.setDefect("Broken");
        os.setTechnician(technician);
        os.setStatus(Status.IN_PROGESS);
        os.setReceptionist(receptionist);

    }

    @AfterEach
    void tearDown() {
        mvc = null;
        os = null;
        osLog = null;
        client = null;
        product = null;
        technician = null;
        receptionist = null;
    }

    @Test
    public void contextLoads() {
        assertThat(mvc).isNotNull();
    }

    @Test
    void all() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/" + DOMAIN))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaTypes.HAL_JSON))
                .andExpect(jsonPath("$._links.self.href", is("http://localhost/" + DOMAIN)));
    }

    @Test
    public void canRetrieveByIdWhenDoesNotExist() throws Exception {
        final int notExpectedId = 2;
        mvc.perform(MockMvcRequestBuilders.get("/" + DOMAIN + "/" + notExpectedId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$", is("Could not find os log " + notExpectedId)));
    }

    @Test
    void addAndOneAndUpdateAndDelete() throws Exception {
        // Add Employees dependency
        mvc.perform(MockMvcRequestBuilders.post("/employees")
                .contentType(MediaType.APPLICATION_JSON).content(mapToJson(technician)))
                .andExpect(content().contentType(MediaTypes.HAL_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$._links.self.href", is("http://localhost/employees/1")));

        mvc.perform(MockMvcRequestBuilders.post("/employees")
                .contentType(MediaType.APPLICATION_JSON).content(mapToJson(receptionist)))
                .andExpect(content().contentType(MediaTypes.HAL_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(2)))
                .andExpect(jsonPath("$._links.self.href", is("http://localhost/employees/2")));

        // Add Product dependency
        mvc.perform(MockMvcRequestBuilders.post("/products")
                .contentType(MediaType.APPLICATION_JSON).content(mapToJson(product)))
                .andExpect(content().contentType(MediaTypes.HAL_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(3)))
                .andExpect(jsonPath("$._links.self.href", is("http://localhost/products/3")));

        // Add Client dependency
        mvc.perform(MockMvcRequestBuilders.post("/clients")
                .contentType(MediaType.APPLICATION_JSON).content(mapToJson(client)))
                .andExpect(content().contentType(MediaTypes.HAL_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(4)))
                .andExpect(jsonPath("$._links.self.href", is("http://localhost/clients/4")));

        // Add OS dependency
        final int expectedOsId = 5;
        mvc.perform(MockMvcRequestBuilders.post("/oss")
                .contentType(MediaType.APPLICATION_JSON).content(mapToJson(os)))
                .andExpect(content().contentType(MediaTypes.HAL_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(expectedOsId)))
                .andExpect(jsonPath("$._links.self.href", is("http://localhost/oss/" + expectedOsId)));

        osLog.setOs(os);
        final int expectedLogId = 6;
        mvc.perform(MockMvcRequestBuilders.post("/" + DOMAIN + "/" + expectedOsId)
                .contentType(MediaType.APPLICATION_JSON).content(mapToJson(osLog)))
                .andExpect(content().contentType(MediaTypes.HAL_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(expectedLogId)))
                .andExpect(jsonPath("$._links.self.href", is("http://localhost/" + DOMAIN + "/" + expectedLogId)));

        mvc.perform(MockMvcRequestBuilders.get("/" + DOMAIN + "/" + expectedLogId)
                .contentType(MediaType.APPLICATION_JSON).content(mapToJson(os)))
                .andExpect(content().contentType(MediaTypes.HAL_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(expectedLogId)))
                .andExpect(jsonPath("$._links.self.href", is("http://localhost/" + DOMAIN + "/" + expectedLogId)));

        mvc.perform(MockMvcRequestBuilders.delete("/" + DOMAIN + "/" + expectedLogId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

    }

}