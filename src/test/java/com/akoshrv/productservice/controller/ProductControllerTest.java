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

import java.util.List;
import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(ProductController.class)
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    @Qualifier("productService")
    private ProductService productService;

    private static final String CATEGORY_BOOK = "book";
    private static final String CATEGORY_MOVIE = "movie";
    private static final Product PRODUCT_1 = new Product("PRODUCT_CODE_1", CATEGORY_BOOK, 10.0, "Title of Book 1", "Description of Book 1");
    private static final Product PRODUCT_2 = new Product("PRODUCT_CODE_2", CATEGORY_MOVIE, 11.0, "Title of Movie 1", "Description of Movie 1");

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void verifyGetAllEndpoint() throws Exception {
        mockMvc.perform(get("/api/v1/products"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void verifyGetProductsForCategoryEndpoint() throws Exception {
        mockMvc.perform(get("/api/v1/products/{category}", CATEGORY_BOOK))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void verifyGetSingleProductEndpoint() throws Exception {
        mockMvc.perform(get("/api/v1/products/{category}/{productCode}", CATEGORY_BOOK, PRODUCT_1.getProductCode()))
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
        mockMvc.perform(put("/api/v1/product/{category}/{productCode}", CATEGORY_BOOK, PRODUCT_1.getProductCode())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(PRODUCT_1)))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void verifyDeleteEndpoint() throws Exception {
        mockMvc.perform(delete("/api/v1/product/{category}/{productCode}", CATEGORY_BOOK, PRODUCT_1.getProductCode()))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void verifyGetEndpointOutput() throws Exception {
        Mockito.when(productService.findAllProducts(null, null, null))
                .thenReturn(Set.of(PRODUCT_1, PRODUCT_2));

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
        Product newProduct = new Product("PRODUCT_CODE_3", "book", 13.0, "New Book", "Description of new book");
        Product newProductCreated = new Product("PRODUCT_CODE_3", "book", 13.0, "New Book", "Description of new book");
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
        String productCode = "PRODUCT_CODE_1";
        Product updatedProduct = new Product(productCode, "book", 10.0, "Title of Book 1", "Updated description of Book 1");
        Mockito.when(productService.updateProduct("book", productCode, updatedProduct))
                .thenReturn(updatedProduct);

        MvcResult mvcResult = mockMvc.perform(put("/api/v1/product/{category}/{productCode}", CATEGORY_BOOK, productCode)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedProduct)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String responseContent = mvcResult.getResponse().getContentAsString();
        Product result = objectMapper.readerFor(Product.class).readValue(responseContent);

        Assertions.assertThat(result).isEqualTo(updatedProduct);
    }
}