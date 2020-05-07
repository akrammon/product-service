package com.akoshrv.productservice.service;

import com.akoshrv.productservice.model.Product;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.util.Objects;
import java.util.function.Predicate;

public final class ProductFilters {

    public static Specification<Product> hasCategorySpec(String desiredCategory) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("category"), desiredCategory.toLowerCase());
    }

    public static Specification<Product> priceLargerThanOrEqualsSpec(Integer minPrice) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(root.get("price"), minPrice);
    }

    public static Specification<Product> priceSmallerThanOrEqualsSpec(Integer maxPrice) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(root.get("price"), maxPrice);
    }

    public static Predicate<Product> hasCategory(String desiredCategory) {
        return product -> {
            if(StringUtils.hasLength(desiredCategory)) {
                return Objects.equals(product.getCategory().toLowerCase(), desiredCategory.toLowerCase());
            } else {
                return Boolean.TRUE;
            }
        };
    }

    public static Predicate<Product> priceLargerThanOrEquals(Integer price) {
        return product -> {
            if(Objects.isNull(price)) {
                return Boolean.TRUE;
            } else {
                return price <= product.getPrice();
            }
        };
    }

    public static Predicate<Product> priceSmallerThanOrEquals(Integer price) {
        return product -> {
            if(Objects.isNull(price)) {
                return Boolean.TRUE;
            } else {
                return price >= product.getPrice();
            }
        };
    }
}
