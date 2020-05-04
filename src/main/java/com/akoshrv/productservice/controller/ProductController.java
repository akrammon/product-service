package com.akoshrv.productservice.controller;

import com.akoshrv.productservice.model.Product;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
public class ProductController {

    @GetMapping("/products")
    public List<Product> getProducts() {
        final Product exampleProduct = new Product(1L, "Book", 10.0, "Test book", "A test book");
        return Arrays.asList(exampleProduct);
    }

}
