package com.akoshrv.productservice.service;

import com.akoshrv.productservice.model.Product;

import java.util.Set;

public interface ProductService {

    /**
     * Find all products in the system and filter them based on the supplied parameters.
     *
     * All filter parameters are optional: if {@code null} is provided then no filtering is performed.
     *
     * @param category The desired category to filter for
     * @param minPrice The minimum desired price, inclusive
     * @param maxPrice The maximum desired price, inclusive
     *
     * @return All the products that fulfill the specified filter criteria
     */
    Set<Product> findAllProducts(String category, Integer minPrice, Integer maxPrice);

    Set<Product> findProductsByCategory(String category, Integer minPrice, Integer maxPrice);

    Product findProductByCategoryAndProductCode(String category, String productCode);


    /**
     * Creates a new product in the system with a generated ID.
     *
     * @param product
     * @return The created product with its generated ID.
     * @throws IllegalArgumentException if the provided category is invalid
     */
    Product createProduct(Product product);

    /**
     * Updates the product with the supplied Product ID.
     *
     * @param product
     * @return The updated Product
     * @throws IllegalArgumentException if the provided category is invalid
     */
    Product updateProduct(String category, String productCode, Product product);

    void deleteProduct(String category, String productCode);
}
