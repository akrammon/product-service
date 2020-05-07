package com.akoshrv.productservice.service;

import com.akoshrv.productservice.model.Product;
import org.springframework.data.jpa.domain.Specification;

public final class Filters {

    public static Specification<Product> hasCategorySpec(String desiredCategory) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(criteriaBuilder.lower(root.get("category")), desiredCategory.toLowerCase());
    }

    public static Specification<Product> priceLargerThanOrEqualsSpec(Integer minPrice) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(root.get("price"), minPrice);
    }

    public static Specification<Product> priceSmallerThanOrEqualsSpec(Integer maxPrice) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(root.get("price"), maxPrice);
    }
}
