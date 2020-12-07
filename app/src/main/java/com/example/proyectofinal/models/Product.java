package com.example.proyectofinal.models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
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
    private List<String> images;

    public Product(int id, String name, String description, double price,Category category) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.category = category;
        this.images = new ArrayList<>();
    }

    public int getId() {
        return id;
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

    public List<String> getImages() {
        return images;
    }

    public static ArrayList<Product> getProducts(Context context){
        ArrayList<Product> products = new ArrayList<>();
        String[] productColumns = new String[]{dataBaseHelper.ID_PRODUCT, dataBaseHelper.NAME_PRODUCT, dataBaseHelper.DESCRIPRION,
                dataBaseHelper.PRICE, dataBaseHelper.ID_CATEGORY, dataBaseHelper.IMG_PRODUCT};
        String[] categoryColumns = new String[]{dataBaseHelper.NAME_CATEGOTY, dataBaseHelper.ID_CATEGORY};
        Cursor cursorProduct = Manager.getInstance(context).open().fetchObject(productColumns, dataBaseHelper.TABLE_NAME_PRODUCT);
        try{
            while(cursorProduct.moveToNext()){
                Cursor categoryCursor = Manager.getInstance(context).open().fetchObjectById(categoryColumns, dataBaseHelper.TABLE_NAME_CATEGOIES,
                        dataBaseHelper.ID_CATEGORY, cursorProduct.getInt(4));
                Category category = new Category(categoryCursor.getInt(1), categoryCursor.getString(0), null);
                Product product = new Product(cursorProduct.getInt(0), cursorProduct.getString(1), cursorProduct.getString(2),
                        cursorProduct.getDouble(3), category);

                //Getting images by id
                String[] imagesColumns = {dataBaseHelper.PRODUCT_IMAGE_ID, dataBaseHelper.ID_PRODUCT};
                Cursor imagesCursor = Manager.getInstance(context).open().fetchObjectById(imagesColumns,
                        dataBaseHelper.TABLE_NAME_PRODUCT_IMAGE, dataBaseHelper.ID_PRODUCT, product.getId());
                try{
                    product.getImages().add(imagesCursor.getString(0));
                    while(imagesCursor.moveToNext()){
                            product.getImages().add(imagesCursor.getString(0));
                    }
                }finally {imagesCursor.close();}
                products.add(product);
            }
        }finally {
            cursorProduct.close();
        }
        Manager.getInstance(context).close();
        return products;
    }

    public static Product getProductById(Context context, Integer id){
        String[] productColumns = new String[]{dataBaseHelper.ID_PRODUCT, dataBaseHelper.NAME_PRODUCT, dataBaseHelper.DESCRIPRION,
                dataBaseHelper.PRICE, dataBaseHelper.ID_CATEGORY, dataBaseHelper.IMG_PRODUCT};
        String[] categoryColumns = new String[]{dataBaseHelper.NAME_CATEGOTY, dataBaseHelper.ID_CATEGORY};
        Cursor cursorProduct = Manager.getInstance(context).open().fetchObjectById(productColumns,
                dataBaseHelper.TABLE_NAME_PRODUCT, dataBaseHelper.ID_PRODUCT, id);
        Cursor categoryCursor = Manager.getInstance(context).open().fetchObjectById(categoryColumns, dataBaseHelper.TABLE_NAME_CATEGOIES,
                dataBaseHelper.ID_CATEGORY, cursorProduct.getInt(4));
        Category category = new Category(categoryCursor.getInt(1), categoryCursor.getString(0), null);
        Product product = new Product(cursorProduct.getInt(0), cursorProduct.getString(1), cursorProduct.getString(2),
                cursorProduct.getDouble(3), category);

        //Getting images by id
        String[] imagesColumns = {dataBaseHelper.PRODUCT_IMAGE_ID, dataBaseHelper.ID_PRODUCT};
        Cursor imagesCursor = Manager.getInstance(context).open().fetchObjectById(imagesColumns,
                dataBaseHelper.TABLE_NAME_PRODUCT_IMAGE, dataBaseHelper.ID_PRODUCT, product.getId());
        try{
            if(product.getId() == 1){
                product.getImages().add(cursorProduct.getString(5));
            }else{
                product.getImages().add(imagesCursor.getString(0));
                System.out.println("El size de las imagenes es: " + product.getImages().size());
                while(imagesCursor.moveToNext()){
                    product.getImages().add(imagesCursor.getString(0));
                    System.out.println("El size de las imagenes es: " + product.getImages().size());
                }
            }

        }finally {imagesCursor.close();}
        Manager.getInstance(context).close();
        return product;
    }

    public static long saveProduct(Context context, Product product, String imageId){
        ContentValues contentValues = new ContentValues();
        contentValues.put(dataBaseHelper.NAME_PRODUCT, product.getName());
        contentValues.put(dataBaseHelper.DESCRIPRION, product.getDescription());
        contentValues.put(dataBaseHelper.PRICE, product.getPrice());
        contentValues.put(dataBaseHelper.ID_CATEGORY, product.getCategory().getId());
        contentValues.put(dataBaseHelper.IMG_PRODUCT, imageId);
        long id = Manager.getInstance(context).open().getDatabase().insert(dataBaseHelper.TABLE_NAME_PRODUCT, null, contentValues);
        Manager.getInstance(context).close();
        return id;
    }

    public static boolean saveImages(Context context, List<String> images, long product_id){
        ContentValues contentValues = new ContentValues();
        for(String image : images){
            contentValues.put(dataBaseHelper.ID_PRODUCT, product_id);
            contentValues.put(dataBaseHelper.PRODUCT_IMAGE_ID, image);
            Manager.getInstance(context).open().getDatabase().insert(dataBaseHelper.TABLE_NAME_PRODUCT_IMAGE, null, contentValues);
        }
        return true;
    }
}
