package com.example.proyectofinal.models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.media.Image;
import com.example.proyectofinal.connection.Manager;
import com.example.proyectofinal.connection.dataBaseHelper;

import java.util.ArrayList;
import java.util.List;

public class Product {
    private int id;
    private String name;
    private String description;
    private Double price;
    private Category category;
    private List<Image> images = new ArrayList<>();

    public Product(int id, String name, String description, double price,Category category) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.category = category;
    }

    public String getName()  {
        return name;
    }

    public Double getPrice() {
        return price;
    }

    public Category getCategory() {
        return category;
    }

    public String getDescription() {
        return description;
    }

    public static ArrayList<Product> getProducts(Context context){
        ArrayList<Product> products = new ArrayList<>();
        String[] productColumns = new String[]{dataBaseHelper.ID_PRODUCT, dataBaseHelper.NAME_PRODUCT, dataBaseHelper.DESCRIPRION, dataBaseHelper.PRICE, dataBaseHelper.ID_CATEGORY};
        String[] categoryColumns = new String[]{dataBaseHelper.NAME_CATEGOTY, dataBaseHelper.ID_CATEGORY};
        Cursor cursorProduct = Manager.getInstance(context).open().fetchObject(productColumns, dataBaseHelper.TABLE_NAME_PRODUCT);
        try{
            while(cursorProduct.moveToNext()){
                Cursor categoryCursor = Manager.getInstance(context).open().fetchObjectById(categoryColumns, dataBaseHelper.TABLE_NAME_CATEGOIES,
                        dataBaseHelper.ID_CATEGORY, cursorProduct.getInt(4));
                Category category = new Category(categoryCursor.getInt(1), categoryCursor.getString(0), null);
                Product product = new Product(cursorProduct.getInt(0), cursorProduct.getString(1), cursorProduct.getString(2),
                        cursorProduct.getDouble(3), category);
                products.add(product);
            }
        }finally {
            cursorProduct.close();
        }
        return products;
    }

    public static boolean saveProduct(Context context, Product product){
        ContentValues contentValues = new ContentValues();
        contentValues.put(dataBaseHelper.NAME_PRODUCT, product.getName());
        contentValues.put(dataBaseHelper.DESCRIPRION, product.getDescription());
        contentValues.put(dataBaseHelper.PRICE, product.getPrice());
        contentValues.put(dataBaseHelper.ID_CATEGORY, product.getCategory().getId());
        Manager.getInstance(context).open().getDatabase().insert(dataBaseHelper.TABLE_NAME_PRODUCT, null, contentValues);
        return true;
    }
}
