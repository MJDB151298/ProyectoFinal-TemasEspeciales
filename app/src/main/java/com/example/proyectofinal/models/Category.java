package com.example.proyectofinal.models;

import android.content.Context;
import android.database.Cursor;
import android.media.Image;
import com.example.proyectofinal.connection.Manager;
import com.example.proyectofinal.connection.dataBaseHelper;

import java.util.ArrayList;
import java.util.List;

public class Category {
    private int id;
    private String name;
    private String image;

    public Category(int id, String name, String image) {
        this.id = id;
        this.name = name;
        this.image = image;
    }

    public Category() {

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

    public static List<Category> getCategories(Context context){
        ArrayList<Category> categories = new ArrayList<>();
        String[] categoryColumns = new String[]{dataBaseHelper.NAME_CATEGOTY, dataBaseHelper.ID_CATEGORY};
        Cursor cursorCategory = Manager.getInstance(context).open().fetchObject(categoryColumns, dataBaseHelper.TABLE_NAME_CATEGOIES);
        try{
            while(cursorCategory.moveToNext()){
                Category category = new Category(cursorCategory.getInt(1), cursorCategory.getString(0), null);
                categories.add(category);
            }
        }finally {
            cursorCategory.close();
        }
        Manager.getInstance(context).close();
        return categories;
    }
}
