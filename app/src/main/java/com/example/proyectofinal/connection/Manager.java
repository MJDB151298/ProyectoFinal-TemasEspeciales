package com.example.proyectofinal.connection;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.util.Log;

import com.example.proyectofinal.models.Category;
import com.example.proyectofinal.models.Product;
import com.example.proyectofinal.models.User;

import java.io.ByteArrayOutputStream;
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

    public void setAuth(User auth) {
        this.auth = auth;
    }
    public void createUser(Bitmap pp, String name, String username, String mail, String pass) {
        ContentValues values = new ContentValues();
        byte[] data = getBitmapAsByteArray(pp);
        values.put(dataBaseHelper.IMG_USER, data);
        values.put(dataBaseHelper.NAME_USER, name);
        values.put(dataBaseHelper.USERNAME,username);
        values.put(dataBaseHelper.MAIL, mail);
        values.put(dataBaseHelper.PASSWORD_USER, pass);
        database.insert(dataBaseHelper.TABLE_NAME_USER, null, values);
       /* database.execSQL("DROP TABLE IF EXISTS CATEGORY");
        database.execSQL("DROP TABLE IF EXISTS PRODUCT");
        database.execSQL("DROP TABLE IF EXISTS USER_APP");*/
        System.out.println("Se creo el user");
    }

    public User findUserByUsername(String username){
            User aux = null;
            String query = "SELECT * FROM "+dataBaseHelper.TABLE_NAME_USER+" WHERE "+dataBaseHelper.USERNAME+" = ?";
            Cursor cursor = database.rawQuery(query,new String[] { username });
            Log.e(dataBaseHelper.class.getName(), query);

            while (cursor.moveToNext()){
                byte[] imgByte = cursor.getBlob(5);
                aux = new User(cursor.getString(1),cursor.getString(3),BitmapFactory.decodeByteArray(imgByte, 0, imgByte.length),cursor.getString(2),cursor.getString(4));

            }
            cursor.close();
            System.out.println("Se encontro"+aux.getName());
            return aux;
    }

    public static byte[] getBitmapAsByteArray(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
        return outputStream.toByteArray();
    }

}
