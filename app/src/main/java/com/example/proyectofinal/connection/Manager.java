package com.example.proyectofinal.connection;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.example.proyectofinal.models.Category;
import com.example.proyectofinal.models.Product;
import com.example.proyectofinal.models.User;

import java.util.ArrayList;

public class Manager {
    private dataBaseHelper helper;
    private Context context;
    private SQLiteDatabase database;
    private ArrayList<Product> allProducts;
    private ArrayList<Category> allCategories;
    private User auth;
    private static Manager instance = null;

    public Manager(Context context){
        this.context = context;
    }
    public static Manager getInstance(Context context){
        if(instance == null ){
            instance = new Manager(context);
        }
        return instance;
    }

    public Manager open() throws SQLException {
        helper = new dataBaseHelper(this.context);
        database = helper.getWritableDatabase();
        return this;
    }
    public void close() {
        helper.close();
    }

    public ArrayList<Product> getAllProducts() {
        return allProducts;
    }

    public ArrayList<Category> getAllCategories() {
        return allCategories;
    }

    public User getAuth() {
        return auth;
    }


}
