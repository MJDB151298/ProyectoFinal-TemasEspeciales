package com.example.proyectofinal.models;

import android.content.Context;
import android.database.Cursor;
import android.media.Image;
import com.example.proyectofinal.connection.Manager;

public class Category {
    private int id;
    private String name;
    private String image;

    public Category(int id, String name, String image) {
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public static Category getCategoryByName(String name, Context context){
        Cursor categories = Manager.getInstance(context).open().fetchCategoryByName(name);
        Category category = new Category(categories.getInt(1), categories.getString(0), null);
        Manager.getInstance(context).close();
        return category;
    }
}
