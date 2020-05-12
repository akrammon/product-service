package com.akoshrv.productservice.service;

import com.akoshrv.productservice.model.Product;
import com.akoshrv.productservice.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Service("productService")
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryService categoryService;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository,
                              @Qualifier("categoryService") CategoryService categoryService) {
        this.productRepository = productRepository;
        this.categoryService = categoryService;
    }

    @Override
    public Set<Product> findAllProducts(String category, Integer minPrice, Integer maxPrice) {
        Specification<Product> specifications = Specification
                .where(Objects.isNull(category) ? null : Filters.hasCategorySpec(category))
                .and(Objects.isNull(minPrice) ? null : Filters.priceLargerThanOrEqualsSpec(minPrice))
                .and(Objects.isNull(maxPrice) ? null : Filters.priceSmallerThanOrEqualsSpec(maxPrice));
        return new HashSet<>(productRepository.findAll(specifications));
    }

    @Override
    public Set<Product> findProductsByCategory(String category, Integer minPrice, Integer maxPrice) {
        Specification<Product> specifications = Specification
                .where(Filters.hasCategorySpec(category))
                .and(Objects.isNull(minPrice) ? null : Filters.priceLargerThanOrEqualsSpec(minPrice))
                .and(Objects.isNull(maxPrice) ? null : Filters.priceSmallerThanOrEqualsSpec(maxPrice));

        return new HashSet<>(productRepository.findAll(specifications));
    }

    @Override
    public Product findProductByCategoryAndProductCode(String category, String productCode) {
        return productRepository.findByCategoryAndProductCode(category, productCode);
    }

    @Override
    public Product createProduct(Product product) {
        validateCategory(product.getCategory());

        return productRepository.save(product);
    }

    @Override
    public Product updateProduct(String category, String productCode, Product product) {
        validateCategory(product.getCategory());

        Product foundProduct = productRepository.findByCategoryAndProductCode(category, productCode);
        Product updatedProduct = new Product(foundProduct.getId(), product.getProductCode(), product.getCategory(), product.getPrice(), product.getName(), product.getDescription());

        return productRepository.save(updatedProduct);
    }

    @Override
    public void deleteProduct(String category, String productCode) {
        Product product = productRepository.findByCategoryAndProductCode(category, productCode);
        productRepository.delete(product);
    }

    private void validateCategory(String category) {
        if(!categoryService.isValidCategory(category)) {
            throw new IllegalArgumentException(String.format("Category %s is invalid", category));
        }
    }
}
