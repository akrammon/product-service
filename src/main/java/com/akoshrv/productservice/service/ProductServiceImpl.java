package com.akoshrv.productservice.service;

import com.akoshrv.productservice.model.Product;
import com.akoshrv.productservice.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

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
    public List<Product> findAllProducts(String category, Integer minPrice, Integer maxPrice) {
        Specification<Product> specifications = Specification
                .where(Objects.isNull(category) ? null : Filters.hasCategorySpec(category))
                .and(Objects.isNull(minPrice) ? null : Filters.priceLargerThanOrEqualsSpec(minPrice))
                .and(Objects.isNull(maxPrice) ? null : Filters.priceSmallerThanOrEqualsSpec(maxPrice));
        return productRepository.findAll(specifications);
    }

    @Override
    public Product findProductByProductNumber(Long productNumber) {
        return productRepository.findByProductNumber(productNumber);
    }

    @Override
    public Product createProduct(Product product) {
        validateCategory(product.getCategory());

        return productRepository.save(product);
    }

    @Override
    public Product updateProduct(Product product) {
        validateCategory(product.getCategory());

        Product foundProduct = productRepository.findByProductNumber(product.getProductNumber());
        Product updatedProduct = new Product(foundProduct.getId(), product.getProductNumber(), product.getCategory(), product.getPrice(), product.getName(), product.getDescription());

        return productRepository.save(updatedProduct);
    }

    private void validateCategory(String category) {
        if(!categoryService.isValidCategory(category)) {
            throw new IllegalArgumentException(String.format("Category %s is invalid", category));
        }
    }
}
