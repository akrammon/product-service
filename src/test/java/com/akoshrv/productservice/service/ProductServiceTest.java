package com.akoshrv.productservice.service;

import com.akoshrv.productservice.model.Product;
import com.akoshrv.productservice.repository.ProductRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

import java.util.Arrays;
import java.util.Set;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    private static final UUID TEST_UUID = UUID.randomUUID();
    private static final String TEST_PRODUCT_CODE_1 = "PRODUCT_CODE_1";
    private static final String TEST_PRODUCT_CODE_2 = "PRODUCT_CODE_2";
    private static final String TEST_CATEGORY = "book";
    private static final Product PRODUCT_1 = new Product(TEST_UUID, TEST_PRODUCT_CODE_1, TEST_CATEGORY, 10.0, "old name", "old description");
    private static final Product PRODUCT_2 = new Product(UUID.randomUUID(), TEST_PRODUCT_CODE_2, TEST_CATEGORY, 21.0, "Book 2 Title", "Book 2 Description");

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
    public void findProductsByCategoryShouldNotReturnDuplicates() {
        Mockito.when(productRepository.findAll(Mockito.any(Specification.class)))
                .thenReturn(Arrays.asList(PRODUCT_1, PRODUCT_1, PRODUCT_2));

        Set<Product> result = productService.findProductsByCategory(TEST_CATEGORY, null, null);

        Assertions.assertThat(result)
                .hasSize(2)
                .containsOnly(PRODUCT_1, PRODUCT_2);
    }

    @Test
    public void updateProductShouldUpdateInTheRepository() {
        Product newProduct = new Product(TEST_UUID, TEST_PRODUCT_CODE_1, TEST_CATEGORY, 9.5, "old name", "new description");

        givenThatProductForCategoryAndProductCodeIs(TEST_CATEGORY, TEST_PRODUCT_CODE_1, PRODUCT_1);
        givenThatCategoryIsValid(newProduct);

        productService.updateProduct(TEST_CATEGORY, TEST_PRODUCT_CODE_1, newProduct);

        Mockito.verify(productRepository, Mockito.times(1)).findByCategoryAndProductCode(TEST_CATEGORY, TEST_PRODUCT_CODE_1);
        Mockito.verify(productRepository, Mockito.times(1)).save(newProduct);
        Mockito.verifyNoMoreInteractions(productRepository);
    }

    @Test
    public void updateShouldThrowExceptionIfCategoryIsInvalid() {
        Product newProduct = new Product(TEST_UUID, TEST_PRODUCT_CODE_1, "invalid category", 9.5, "old name", "new description");

        givenThatCategoryIsInvalid(newProduct);

        IllegalArgumentException exception = org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class, () -> {
            productService.updateProduct(TEST_CATEGORY, TEST_PRODUCT_CODE_1, newProduct);
        });

        Assertions.assertThat(exception.getMessage()).contains(newProduct.getCategory());
    }

    @Test
    public void createShouldThrowExceptionIfCategoryIsInvalid() {
        Product newProduct = new Product(TEST_PRODUCT_CODE_1, "invalid category", 9.5, "old name", "new description");

        givenThatCategoryIsInvalid(newProduct);

        IllegalArgumentException exception = org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class, () -> {
            productService.createProduct(newProduct);
        });

        Assertions.assertThat(exception.getMessage()).contains(newProduct.getCategory());
    }

    @Test
    public void createShouldSaveTheCorrectProduct() {
        givenThatCategoryIsValid(PRODUCT_1);

        productService.createProduct(PRODUCT_1);

        Mockito.verify(productRepository, Mockito.times(1)).save(PRODUCT_1);
        Mockito.verifyNoMoreInteractions(productRepository);
    }

    @Test
    public void deleteShouldDeleteTheCorrectProduct() {
        givenThatProductForCategoryAndProductCodeIs(TEST_CATEGORY, TEST_PRODUCT_CODE_1, PRODUCT_1);

        productService.deleteProduct(TEST_CATEGORY, TEST_PRODUCT_CODE_1);

        Mockito.verify(productRepository, Mockito.times(1)).delete(PRODUCT_1);
        Mockito.verifyNoMoreInteractions(productRepository);
    }

    private void givenThatProductForCategoryAndProductCodeIs(String category, String productCode, Product product) {
        Mockito.when(productRepository.findByCategoryAndProductCode(category, productCode))
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