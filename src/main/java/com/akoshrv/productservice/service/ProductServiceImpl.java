package com.akoshrv.productservice.service;

import com.akoshrv.productservice.model.Product;
import com.akoshrv.productservice.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public List<Product> findAllProducts() {
        saveTestData();
        return productRepository.findAll();
    }

    private void saveTestData() {
        productRepository.save(new Product("Book", 10.0, "Book 1", "Description of the first book"));
        productRepository.save(new Product("Book", 12.0, "Book 2", "Description of the second book"));
    }
}
