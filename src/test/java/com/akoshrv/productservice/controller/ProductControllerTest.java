package com.akoshrv.productservice.controller;

import com.akoshrv.productservice.model.Product;
import com.akoshrv.productservice.service.ProductService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(ProductController.class)
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    @Qualifier("productService")
    private ProductService productService;

    private static final Product PRODUCT_1 = new Product(1L, "book", 10.0, "Title of Book 1", "Description of Book 1");
    private static final Product PRODUCT_2 = new Product(2L, "movie", 11.0, "Title of Movie 1", "Description of Movie 1");

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void verifyGetEndpoint() throws Exception {
        mockMvc.perform(get("/api/v1/products")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void verifyPostEndpoint() throws Exception {
        mockMvc.perform(post("/api/v1/product")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(PRODUCT_1)))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void verifyPutEndpoint() throws Exception {
        mockMvc.perform(put("/api/v1/product/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(PRODUCT_1)))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void verifyGetEndpointOutput() throws Exception {
        Mockito.when(productService.findAllProducts(null, null, null))
                .thenReturn(Arrays.asList(PRODUCT_1, PRODUCT_2));

        MvcResult mvcResult = mockMvc.perform(get("/api/v1/products")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String responseContent = mvcResult.getResponse().getContentAsString();
        List<Product> result = objectMapper.readValue(responseContent, new TypeReference<>() {});

        Assertions.assertThat(result).containsOnly(PRODUCT_1, PRODUCT_2);
    }

    @Test
    public void verifyPostEndpointOutput() throws Exception {
        Product newProduct = new Product(3L, "book", 13.0, "New Book", "Description of new book");
        Product newProductCreated = new Product(3L, "book", 13.0, "New Book", "Description of new book");
        Mockito.when(productService.createProduct(newProduct))
                .thenReturn(newProductCreated);

        MvcResult mvcResult = mockMvc.perform(post("/api/v1/product")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newProduct)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String responseContent = mvcResult.getResponse().getContentAsString();
        Product result = objectMapper.readerFor(Product.class).readValue(responseContent);

        Assertions.assertThat(result).isEqualTo(newProductCreated);
    }

    @Test
    public void verifyPutEndpointOutput() throws Exception {
        Long productNumber = 1L;
        Product updatedProduct = new Product(productNumber, "book", 10.0, "Title of Book 1", "Updated description of Book 1");
        Mockito.when(productService.updateProduct(productNumber, updatedProduct))
                .thenReturn(updatedProduct);

        MvcResult mvcResult = mockMvc.perform(put("/api/v1/product/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedProduct)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String responseContent = mvcResult.getResponse().getContentAsString();
        Product result = objectMapper.readerFor(Product.class).readValue(responseContent);

        Assertions.assertThat(result).isEqualTo(updatedProduct);
    }
}