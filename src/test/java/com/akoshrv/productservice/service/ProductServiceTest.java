package com.akoshrv.productservice.service;

import com.akoshrv.productservice.model.Product;
import com.akoshrv.productservice.repository.ProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    private static final Long TEST_ID = 1L;
    private static final Product OLD_PRODUCT = new Product(TEST_ID, "book", 10.0, "old name", "old description");

    @Mock
    private ProductRepository productRepository;
    @Mock
    private CategoryService categoryService;

    private ProductService productService;

    @BeforeEach
    void initialize() {
        productService = new ProductServiceImpl(productRepository, categoryService);
    }

    @Test
    public void updateProductShouldUpdateInTheRepository() {
        Product newProduct = new Product(TEST_ID, "book", 9.5, "old name", "new description");

        givenThatProductForIdIs(TEST_ID, OLD_PRODUCT);
        givenThatCategoryIsValid(newProduct);

        productService.updateProduct(newProduct);

        Mockito.verify(productRepository, Mockito.times(1)).getOne(TEST_ID);
        Mockito.verify(productRepository, Mockito.times(1)).save(newProduct);
        Mockito.verifyNoMoreInteractions(productRepository);
    }

    @Test
    public void updateShouldThrowExceptionIfCategoryIsInvalid() {
        Product newProduct = new Product(TEST_ID, "invalid category", 9.5, "old name", "new description");

        givenThatCategoryIsInvalid(newProduct);

        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            productService.updateProduct(newProduct);
        });

        Assertions.assertTrue(exception.getMessage().contains(newProduct.getCategory()));
    }

    @Test
    public void createShouldThrowExceptionIfCategoryIsInvalid() {
        Product newProduct = new Product(TEST_ID, "invalid category", 9.5, "old name", "new description");

        givenThatCategoryIsInvalid(newProduct);

        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            productService.createProduct(newProduct);
        });

        Assertions.assertTrue(exception.getMessage().contains(newProduct.getCategory()));
    }

    private void givenThatProductForIdIs(Long id, Product product) {
        Mockito.when(productRepository.getOne(id))
                .thenReturn(product);
    }

    private void givenThatCategoryIsValid(Product product) {
        Mockito.when(categoryService.isValidCategory(product.getCategory()))
                .thenReturn(Boolean.TRUE);
    }

    private void givenThatCategoryIsInvalid(Product product) {
        Mockito.when(categoryService.isValidCategory(product.getCategory()))
                .thenReturn(Boolean.FALSE);
    }
}