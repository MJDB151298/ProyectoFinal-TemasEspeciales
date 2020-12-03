package com.example.proyectofinal.connection;

import android.content.Context;
import android.database.Cursor;
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

    public Cursor fetchObject(String[] columns, String table){
        Cursor cursor = database.query(table, columns, null, null, null, null, null);
        return cursor;
    }

    public Cursor fetchObjectById(String[] columns, String table, String id_type, Integer id){
        Cursor cursor = database.query(table, columns, id_type + "=?", new String[]{id.toString()}, null, null, null);
        return cursor;
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

    public dataBaseHelper getHelper() {
        return helper;
    }

    public SQLiteDatabase getDatabase() {
        return database;
    }
}
