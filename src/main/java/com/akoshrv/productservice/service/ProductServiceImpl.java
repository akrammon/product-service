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
        return productRepository.findAll();
    }

    @Override
    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    @Override
    public Product updateProduct(Product product) {
        Product productToBeUpdated = productRepository.getOne(product.getId());
        productToBeUpdated.setCategory(product.getCategory());
        productToBeUpdated.setPrice(product.getPrice());
        productToBeUpdated.setName(product.getName());
        productToBeUpdated.setDescription(product.getDescription());
        return productRepository.save(productToBeUpdated);
    }
}
