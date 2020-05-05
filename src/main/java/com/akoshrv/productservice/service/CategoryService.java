package com.akoshrv.productservice.service;

import java.util.List;

public interface CategoryService {

    List<String> getAllCategories();

    boolean isValidCategory(String category);
}
