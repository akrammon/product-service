package com.akoshrv.productservice.service;

import com.akoshrv.productservice.model.Product;

import java.util.List;

public interface ProductService {

    /**
     * Find all products.
     *
     * @return A list of all products
     */
    List<Product> findAllProducts(String category, Integer minPrice, Integer maxPrice);

    /**
     * Creates a new product in the system with a generated ID.
     *
     * @param product
     * @return
     */
    Product createProduct(Product product);

    /**
     * Updates the product with the supplied Product ID.
     *
     * @param product
     * @return
     */
    Product updateProduct(Product product);
}
