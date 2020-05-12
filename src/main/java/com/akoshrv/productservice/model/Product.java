package com.akoshrv.productservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import java.util.Objects;
import java.util.UUID;

@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonIgnore
    private UUID id;
    @NaturalId(mutable = true)
    @Column(nullable = false, unique = true, updatable = true)
    private String productCode;
    private String category;
    private Double price;
    private String name;
    private String description;

    //needed by JPA
    private Product() {
    }

    public Product(String category, Double price, String name, String description) {
        this.category = category;
        this.price = price;
        this.name = name;
        this.description = description;
    }

    public Product(String productCode, String category, Double price, String name, String description) {
        this.productCode = productCode;
        this.category = category;
        this.price = price;
        this.name = name;
        this.description = description;
    }

    public Product(UUID id, String productCode, String category, Double price, String name, String description) {
        this.id = id;
        this.productCode = productCode;
        this.category = category;
        this.price = price;
        this.name = name;
        this.description = description;
    }

    public UUID getId() {
        return id;
    }

    public String getProductCode() {
        return productCode;
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

    public void setId(UUID id) {
        this.id = id;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Objects.equals(productCode, product.productCode) &&
                Objects.equals(category, product.category) &&
                Objects.equals(price, product.price) &&
                Objects.equals(name, product.name) &&
                Objects.equals(description, product.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productCode, category, price, name, description);
    }
}
