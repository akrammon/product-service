package com.akoshrv.productservice.service;

import com.akoshrv.productservice.model.Product;

import java.util.List;

public interface ProductService {

    List<Product> findAllProducts();

    Product createProduct(Product product);

    Product updateProduct(Product product);
}
