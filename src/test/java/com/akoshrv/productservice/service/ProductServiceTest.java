package com.akoshrv.productservice.service;

import com.akoshrv.productservice.model.Product;
import com.akoshrv.productservice.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    private static final Long TEST_ID = 1L;
    private static final Product OLD_PRODUCT = new Product(TEST_ID, "old category", 10.0, "old name", "old description");

    @Mock
    private ProductRepository productRepository;

    private ProductService productService;

    @BeforeEach
    void initialize() {
        Mockito.when(productRepository.getOne(TEST_ID))
                .thenReturn(OLD_PRODUCT);

        productService = new ProductServiceImpl(productRepository);
    }

    @Test
    public void updateProductShouldUpdateInTheRepository() {
        Product newProduct = new Product(TEST_ID, "old category", 9.5, "old name", "new description");
        productService.updateProduct(newProduct);

        Mockito.verify(productRepository, Mockito.times(1)).getOne(TEST_ID);
        Mockito.verify(productRepository, Mockito.times(1)).save(newProduct);
        Mockito.verifyNoMoreInteractions(productRepository);
    }
}