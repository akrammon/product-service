package com.akoshrv.productservice.repository;

public enum Category {

    BOOK("book"),
    MOVIE("movie");

    private final String stringValue;

    Category(String stringValue) {
        this.stringValue = stringValue;
    }

    public String getStringValue() {
        return stringValue;
    }
}
