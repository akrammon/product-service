package com.akoshrv.productservice.service;

import com.akoshrv.productservice.model.Product;
import com.akoshrv.productservice.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service("productService")
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryService categoryService;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository,
                              @Qualifier("dummyCategoryService") CategoryService categoryService) {
        this.productRepository = productRepository;
        this.categoryService = categoryService;
    }

    @Override
    public List<Product> findAllProducts(String category, Integer minPrice, Integer maxPrice) {
        return productRepository.findAll().stream()
                .filter(ProductFilters.hasCategory(category))
                .filter(ProductFilters.priceLargerThanOrEquals(minPrice))
                .filter(ProductFilters.priceSmallerThanOrEquals(maxPrice))
                .collect(Collectors.toList());
    }

    @Override
    public Product createProduct(Product product) {
        validateCategory(product.getCategory());

        return productRepository.save(product);
    }

    @Override
    public Product updateProduct(Product product) {
        validateCategory(product.getCategory());

        Product productToBeUpdated = productRepository.getOne(product.getId());
        productToBeUpdated.setCategory(product.getCategory());
        productToBeUpdated.setPrice(product.getPrice());
        productToBeUpdated.setName(product.getName());
        productToBeUpdated.setDescription(product.getDescription());

        return productRepository.save(productToBeUpdated);
    }

    private void validateCategory(String category) {
        if(!categoryService.isValidCategory(category)) {
            throw new IllegalArgumentException(String.format("Category %s is invalid", category));
        }
    }
}
