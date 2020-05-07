package com.akoshrv.productservice.service;

import com.akoshrv.productservice.repository.Category;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.EnumSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service("categoryService")
public class CategoryServiceImpl implements CategoryService {

    private Set<String> allCategories;

    public CategoryServiceImpl() {
        this.allCategories = EnumSet.allOf(Category.class)
                .stream()
                .map(Category::getStringValue)
                .map(String::toLowerCase)
                .collect(Collectors.toUnmodifiableSet());
    }

    @Override
    public Set<String> getAllCategories() {
        return Set.copyOf(allCategories);
    }

    @Override
    public boolean isValidCategory(String category) {
        if(StringUtils.hasLength(category)) {
            return allCategories.contains(category.toLowerCase());
        }

        return Boolean.FALSE;
    }
}
