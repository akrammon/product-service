package com.akoshrv.productservice.service;

import com.akoshrv.productservice.model.Product;
import com.akoshrv.productservice.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryService categoryService;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository, CategoryService categoryService) {
        this.productRepository = productRepository;
        this.categoryService = categoryService;
    }

    @Override
    public List<Product> findAllProducts(String category) {
        return productRepository.findAll().stream()
                .filter(ProductFilters.hasCategory(category))
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
