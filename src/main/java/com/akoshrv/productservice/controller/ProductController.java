package com.akoshrv.productservice.controller;

import com.akoshrv.productservice.model.Product;
import com.akoshrv.productservice.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("productController")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(@Qualifier("productService") ProductService productService) {
        this.productService = productService;
    }

    @GetMapping(value = "/api/v1/products", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Product> getProducts(@RequestParam(name="category", required = false) String category,
                                     @RequestParam(name="minPrice", required = false) Integer minPrice,
                                     @RequestParam(name = "maxPrice", required = false) Integer maxPrice) {
        return productService.findAllProducts(category, minPrice, maxPrice);
    }

    @GetMapping(value = "/api/v1/product/{productNumber}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Product getProduct(@PathVariable("productNumber") Long productNumber) {
        return productService.findProductByProductNumber(productNumber);
    }

    @PostMapping(value = "/api/v1/product", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Product createProduct(@RequestBody Product product) {
        return productService.createProduct(product);
    }

    @PutMapping(value = "/api/v1/product/{productNumber}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Product updateProduct(@PathVariable("productNumber") Long productNumber, @RequestBody Product product) {
        return productService.updateProduct(productNumber, product);
    }

    @DeleteMapping(value="/api/v1/product/{productNumber}")
    public void delete(@PathVariable("productNumber") Long productNumber) {
        productService.deleteProduct(productNumber);
    }
}
