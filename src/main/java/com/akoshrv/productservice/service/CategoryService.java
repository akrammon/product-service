package com.akoshrv.productservice.service;

import java.util.Set;

public interface CategoryService {

    /**
     * Gets all the valid product categories in the system.
     *
     * @return
     */
    Set<String> getAllCategories();

    /**
     * Determines if a category matches any of the categories in the system, ignoring case.
     *
     * @param category
     * @return
     */
    boolean isValidCategory(String category);

}
