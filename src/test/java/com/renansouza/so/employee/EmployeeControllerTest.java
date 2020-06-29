package com.renansouza.so.employee;

import com.renansouza.so.AbstractMvcTest;
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
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class EmployeeControllerTest extends AbstractMvcTest {

    @Autowired
    private MockMvc mvc;
    Employee employee;

    private static final String DOMAIN = "employees";

    @BeforeEach
    void setUp() {
        employee = new Employee();
        employee.setName("Receptionist");
    }

    @AfterEach
    void tearDown() {
        mvc = null;
        employee = null;
    }

    @Test
    public void contextLoads() throws Exception {
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
                .andExpect(jsonPath("$", is("Could not find employee " + notExpectedId)));
    }

    @Test
    void addAndOneAndUpdateAndDelete() throws Exception {
        final int expectedId = 1;

        mvc.perform(MockMvcRequestBuilders.post("/" + DOMAIN)
                .contentType(MediaType.APPLICATION_JSON).content(mapToJson(employee)))
                .andExpect(content().contentType(MediaTypes.HAL_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(expectedId)))
                .andExpect(jsonPath("$._links.self.href", is("http://localhost/" + DOMAIN + "/1")));

        mvc.perform(MockMvcRequestBuilders.get("/" + DOMAIN + "/" + expectedId))
                .andExpect(content().contentType(MediaTypes.HAL_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(expectedId)))
                .andExpect(jsonPath("$.name", is(employee.getName())))
                .andExpect(jsonPath("$._links.self.href", is("http://localhost/" + DOMAIN + "/1")));

        employee.setName("technician");
        mvc.perform(MockMvcRequestBuilders.put("/" + DOMAIN + "/" + expectedId)
                .contentType(MediaType.APPLICATION_JSON).content(mapToJson(employee)))
                .andExpect(content().contentType(MediaTypes.HAL_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is(employee.getName())))
                .andExpect(jsonPath("$._links.self.href", is("http://localhost/" + DOMAIN + "/1")));

        mvc.perform(MockMvcRequestBuilders.delete("/" + DOMAIN + "/" + expectedId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

}