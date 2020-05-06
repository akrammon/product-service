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

import java.util.Arrays;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    private static final Long TEST_ID = 1L;
    private static final Product PRODUCT_1 = new Product(TEST_ID, "book", 10.0, "old name", "old description");
    private static final Product PRODUCT_2 = new Product(2L, "movie", 12.0, "Movie title", "Description of movie");
    private static final Product PRODUCT_3 = new Product(3L, "book", 13.0, "Title of second book", "Description of second book");

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
    public void findAllShouldNotFilterOutAnythingIfParamsAreNull() {
        givenThatAllProductsAre(PRODUCT_1, PRODUCT_2, PRODUCT_3);

        List<Product> result = productService.findAllProducts(null, null, null);

        Assertions.assertThat(result)
                .containsOnly(PRODUCT_1, PRODUCT_2, PRODUCT_3);
    }

    @Test
    public void findAllShouldFilterCorrectlyBasedOnCategory() {
        givenThatAllProductsAre(PRODUCT_1, PRODUCT_2, PRODUCT_3);

        List<Product> result = productService.findAllProducts("book", null, null);

        Assertions.assertThat(result)
                .containsOnly(PRODUCT_1, PRODUCT_3);
    }

    @Test
    public void findAllShouldFilterCorrectlyBasedOnMinPrice() {
        givenThatAllProductsAre(PRODUCT_1, PRODUCT_2, PRODUCT_3);

        List<Product> result = productService.findAllProducts(null, 12, null);

        Assertions.assertThat(result)
                .containsOnly(PRODUCT_2, PRODUCT_3);
    }

    @Test
    public void findAllShouldFilterCorrectlyBasedOnMaxPrice() {
        givenThatAllProductsAre(PRODUCT_1, PRODUCT_2, PRODUCT_3);

        List<Product> result = productService.findAllProducts(null, null, 12);

        Assertions.assertThat(result)
                .containsOnly(PRODUCT_1, PRODUCT_2);
    }

    @Test
    public void findAllShouldFilterCorrectlyBasedOnMinAndMaxPrice() {
        givenThatAllProductsAre(PRODUCT_1, PRODUCT_2, PRODUCT_3);

        List<Product> result = productService.findAllProducts(null, 12, 12);

        Assertions.assertThat(result)
                .containsOnly(PRODUCT_2);
    }

    @Test
    public void findAllShouldFilterCorrectlyBasedOnAllFilters() {
        givenThatAllProductsAre(PRODUCT_1, PRODUCT_2, PRODUCT_3);

        List<Product> result = productService.findAllProducts("book", 12, 12);

        Assertions.assertThat(result)
                .isEmpty();
    }

    @Test
    public void updateProductShouldUpdateInTheRepository() {
        Product newProduct = new Product(TEST_ID, "book", 9.5, "old name", "new description");

        givenThatProductForIdIs(TEST_ID, PRODUCT_1);
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

        IllegalArgumentException exception = org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class, () -> {
            productService.updateProduct(newProduct);
        });

        Assertions.assertThat(exception.getMessage()).contains(newProduct.getCategory());
    }

    @Test
    public void createShouldThrowExceptionIfCategoryIsInvalid() {
        Product newProduct = new Product(TEST_ID, "invalid category", 9.5, "old name", "new description");

        givenThatCategoryIsInvalid(newProduct);

        IllegalArgumentException exception = org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class, () -> {
            productService.createProduct(newProduct);
        });

        Assertions.assertThat(exception.getMessage()).contains(newProduct.getCategory());
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

    private void givenThatAllProductsAre(Product... products) {
        Mockito.when(productRepository.findAll())
                .thenReturn(Arrays.asList(products));
    }
}