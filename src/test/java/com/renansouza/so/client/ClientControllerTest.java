package com.renansouza.so.client;

import com.renansouza.so.AbstractMvcTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ClientControllerTest extends AbstractMvcTest {

    @Autowired
    private MockMvc mvc;
    Client client;

    private static final String DOMAIN = "clients";

    @BeforeEach
    void setUp() {
        client = new Client();
        client.setName("Client");
        client.setZipcode(89200000);
        client.setPhone("47999999999");
        client.setEmail("client@mail.com");
        client.setAddress("123 Main Street");
    }

    @AfterEach
    void tearDown() {
        mvc = null;
        client = null;
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
        final int notExpectedId = 1;
        mvc.perform(MockMvcRequestBuilders.get("/" + DOMAIN + "/" + notExpectedId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$", is("Could not find client " + notExpectedId)));
    }

    @Test
    void addAndOneAndUpdate() throws Exception {
        final int expectedId = 1;

        mvc.perform(MockMvcRequestBuilders.post("/" + DOMAIN)
                .contentType(MediaType.APPLICATION_JSON).content(mapToJson(client)))
                .andExpect(content().contentType(MediaTypes.HAL_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(expectedId)))
                .andExpect(jsonPath("$._links.clients.href", is("http://localhost/" + DOMAIN)));

        mvc.perform(MockMvcRequestBuilders.get( "/" + DOMAIN + "/" + expectedId))
                .andExpect(content().contentType(MediaTypes.HAL_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(expectedId)))
                .andExpect(jsonPath("$.name", is(client.getName())))
                .andExpect(jsonPath("$._links.clients.href", is("http://localhost/" + DOMAIN)));

        client.setName("Another Client");
        mvc.perform(MockMvcRequestBuilders.put("/" + DOMAIN + "/" + expectedId)
                .contentType(MediaType.APPLICATION_JSON).content(mapToJson(client)))
                .andExpect(content().contentType(MediaTypes.HAL_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is(client.getName())))
                .andExpect(jsonPath("$._links.clients.href", is("http://localhost/" + DOMAIN)));
    }

}