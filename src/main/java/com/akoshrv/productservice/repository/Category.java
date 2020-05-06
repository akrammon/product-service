package com.akoshrv.productservice.repository;

public enum Category {

    BOOK("book"),
    MOVIE("movie"),
    CLOTHING("clothing"),
    ELECTRONICS("electronics"),
    GROCERY("grocery");

    private final String stringValue;

    Category(String stringValue) {
        this.stringValue = stringValue;
    }

    public String getStringValue() {
        return stringValue;
    }
}
