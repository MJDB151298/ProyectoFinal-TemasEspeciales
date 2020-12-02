package com.example.proyectofinal.models;

import android.media.Image;

import java.util.ArrayList;
import java.util.List;

public class Product {
    private String name;
    private Double price;
    private Category category;
    private List<Image> images = new ArrayList<>();

    public Product(String name, Double price,Category category) {
        this.name = name;
        this.price = price;
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public Double getPrice() {
        return price;
    }

    public Category getCategory() {
        return category;
    }
}
