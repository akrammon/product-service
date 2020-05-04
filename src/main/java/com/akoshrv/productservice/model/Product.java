package com.akoshrv.productservice.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String category;
    private Double price;
    private String name;
    private String description;

    protected Product() {
    }

    public Product(String category, Double price, String name, String description) {
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
