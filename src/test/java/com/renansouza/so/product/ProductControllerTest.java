package com.renansouza.so.product;

import com.renansouza.so.AbstractMvcTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ProductControllerTest extends AbstractMvcTest {

    @Autowired
    private MockMvc mvc;
    Product product;

    private static final String DOMAIN = "products";

    @BeforeEach
    void setUp() {
        product = new Product();
        product.setDescription("Product");
        product.setBrand("Brand");
        product.setType("PC");
    }

    @AfterEach
    void tearDown() {
        mvc = null;
        product = null;
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
                .andExpect(jsonPath("$", is("Could not find product " + notExpectedId)));
    }

    @Test
    void addAndOneAndUpdate() throws Exception {
        final int expectedId = 1;

        mvc.perform(MockMvcRequestBuilders.post("/" + DOMAIN)
                .contentType(MediaType.APPLICATION_JSON).content(mapToJson(product)))
                .andExpect(content().contentType(MediaTypes.HAL_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(expectedId)))
                .andExpect(jsonPath("$._links.products.href", is("http://localhost/" + DOMAIN)));

        mvc.perform(MockMvcRequestBuilders.get( "/" + DOMAIN + "/" + expectedId))
                .andExpect(content().contentType(MediaTypes.HAL_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(expectedId)))
                .andExpect(jsonPath("$._links.products.href", is("http://localhost/" + DOMAIN)));

        product.setDescription("Another Product");
        mvc.perform(MockMvcRequestBuilders.put("/" + DOMAIN + "/" + expectedId)
                .contentType(MediaType.APPLICATION_JSON).content(mapToJson(product)))
                .andExpect(content().contentType(MediaTypes.HAL_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.description", is(product.getDescription())))
                .andExpect(jsonPath("$._links.products.href", is("http://localhost/" + DOMAIN)));
    }

}