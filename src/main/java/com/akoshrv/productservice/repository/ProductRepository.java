package com.akoshrv.productservice.repository;

import com.akoshrv.productservice.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID>, JpaSpecificationExecutor<Product> {

    Product findByProductCode(String productCode);

    Product findByCategoryAndProductCode(String category, String productCode);

    List<Product> findByCategory(String category);
}
