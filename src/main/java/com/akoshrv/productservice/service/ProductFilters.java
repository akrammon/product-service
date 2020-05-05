package com.akoshrv.productservice.service;

import com.akoshrv.productservice.model.Product;
import org.springframework.util.StringUtils;

import java.util.Objects;
import java.util.function.Predicate;

public final class ProductFilters {

    public static Predicate<Product> hasCategory(String desiredCategory) {
        return product -> {
            if(StringUtils.hasLength(desiredCategory)) {
                return Objects.equals(product.getCategory().toLowerCase(), desiredCategory.toLowerCase());
            } else {
                return Boolean.TRUE;
            }
        };
    }
}
