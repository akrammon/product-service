package com.akoshrv.productservice.model;

public class Product {

    private final Long id;
    private final String category;
    private final Double price;
    private final String name;
    private final String description;

    public Product(Long id, String category, Double price, String name, String description) {
        this.id = id;
        this.category = category;
        this.price = price;
        this.name = name;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public String getCategory() {
        return category;
    }

    public Double getPrice() {
        return price;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }


}
