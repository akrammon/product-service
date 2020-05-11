package com.akoshrv.productservice.integration;

import com.akoshrv.productservice.ProductServiceApplication;
import com.akoshrv.productservice.model.Product;
import com.akoshrv.productservice.repository.ProductRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;
import java.util.List;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, classes = ProductServiceApplication.class)
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
public class ProductApplicationIntegrationTest {

    private static final Product PRODUCT_1 = new Product(1L, "book", 10.0, "Title of Book", "Description of Book");
    private static final Product PRODUCT_2 = new Product(2L, "movie", 12.0, "Title of Movie", "Description of Movie");

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductRepository productRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @AfterEach
    public void resetDatabase() {
        productRepository.deleteAll();
    }

    @Test
    public void getAllShouldReturnAllProducts() throws Exception {
        saveTestProductsToDatabase(PRODUCT_1, PRODUCT_2);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/products")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn();

        String responseContent = mvcResult.getResponse().getContentAsString();
        List<Product> result = objectMapper.readValue(responseContent, new TypeReference<>() {});

        Assertions.assertThat(result).containsOnly(PRODUCT_1, PRODUCT_2);
    }

    @Test
    public void getOneShouldReturnSingleProduct() throws Exception {
        saveTestProductsToDatabase(PRODUCT_1, PRODUCT_2);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/product/{productNumber}", PRODUCT_1.getProductNumber())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn();

        String responseContent = mvcResult.getResponse().getContentAsString();
        Product result = objectMapper.readerFor(Product.class).readValue(responseContent);

        Assertions.assertThat(result).isEqualTo(PRODUCT_1);
    }

    @Test
    public void postShouldSaveProduct() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/product/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(PRODUCT_1)))
                .andExpect(MockMvcResultMatchers.status().isOk());

        List<Product> allProducts = productRepository.findAll();

        Assertions.assertThat(allProducts).containsOnly(PRODUCT_1);
    }

    @Test
    public void putShouldUpdateProduct() throws Exception {
        Product updatedProduct = new Product(PRODUCT_1.getProductNumber(), PRODUCT_1.getCategory(), 9.99, "A new title", "A new description");

        saveTestProductsToDatabase(PRODUCT_1, PRODUCT_2);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/product/{productNumber}", updatedProduct.getProductNumber())
                .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(updatedProduct)))
                .andExpect(MockMvcResultMatchers.status().isOk());

        List<Product> result = productRepository.findAll();
        Assertions.assertThat(result).containsOnly(PRODUCT_2, updatedProduct);
    }

    @Test
    public void deleteShouldDeleteProduct() throws Exception {
        saveTestProductsToDatabase(PRODUCT_1, PRODUCT_2);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/product/{productNumber}", PRODUCT_1.getProductNumber()))
                .andExpect(MockMvcResultMatchers.status().isOk());

        List<Product> result = productRepository.findAll();
        Assertions.assertThat(result).containsOnly(PRODUCT_2);
    }

    private void saveTestProductsToDatabase(Product... products) {
        Arrays.asList(products).forEach(productRepository::saveAndFlush);
    }
}
