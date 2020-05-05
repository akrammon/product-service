package com.akoshrv.productservice.service;

import com.akoshrv.productservice.repository.Category;
import org.springframework.stereotype.Service;

import java.util.EnumSet;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class DummyCategoryServiceImpl implements CategoryService {

    @Override
    public List<String> getAllCategories() {
        return EnumSet.allOf(Category.class).stream()
                .map(Category::getStringValue)
                .collect(Collectors.toList());
    }

    @Override
    public boolean isValidCategory(String category) {
        return getAllCategories().stream()
                .filter(Objects::nonNull)
                .anyMatch(s -> s.equalsIgnoreCase(category));
    }
}
