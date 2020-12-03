package com.example.proyectofinal.models;

import android.media.Image;

public class Category {
    private int id;
    private String name;
    private Image image;

    public Category(int id, String name, Image image) {
        this.id = id;
        this.name = name;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public int getId() {
        return id;
    }
}
