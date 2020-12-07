package com.example.proyectofinal.connection;

import android.content.ContentValues;
import android.content.Context;
import android.content.Entity;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.util.Log;

import com.example.proyectofinal.models.CarItem;
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
    private ArrayList<CarItem> carItems;
    private User auth;
    private static Manager instance = null;

    public Manager(Context context) {
        this.context = context;
        carItems = new ArrayList<>();
    }

    public static Manager getInstance(Context context) {
        if (instance == null) {
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

    //GET ALL OBJECTS
    //columns: las columnas de la fila que quieren ser devueltas
    //table: El nombre de la tabla que se desea buscar
    public Cursor fetchObject(String[] columns, String table) {
        Cursor cursor = database.query(table, columns, null, null, null, null, null);
        return cursor;
    }

    //GET ANY OBJECT BY ID
    //columns: las columnas de la fila que quieren ser devueltas
    //table: El nombre de la tabla que se desea buscar
    //id_type: El nombre del id que se busca
    //id: El id por el cual se busca
    public Cursor fetchObjectById(String[] columns, String table, String id_type, Integer id) {
        Cursor cursor = database.query(table, columns, id_type + "=?", new String[]{id.toString()}, null, null, null);
        if(cursor != null){
            cursor.moveToFirst();
        }
        return cursor;
    }

    public ArrayList<Product> getAllProducts() {
        return allProducts;
    }

    public ArrayList<Category> getAllCategories() {
        return allCategories;
    }

    public ArrayList<CarItem> getCarItems() {
        return carItems;
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
        values.put(dataBaseHelper.USERNAME, username);
        values.put(dataBaseHelper.MAIL, mail);
        values.put(dataBaseHelper.PASSWORD_USER, pass);
        database.insert(dataBaseHelper.TABLE_NAME_USER, null, values);
        System.out.println("Se creo el user");
    }

    public User findUserByUsername(String username) {
        User aux = null;
        String query = "SELECT * FROM " + dataBaseHelper.TABLE_NAME_USER + " WHERE " + dataBaseHelper.USERNAME + " = ?";
        Cursor cursor = database.rawQuery(query, new String[]{username});
        Log.e(dataBaseHelper.class.getName(), query);

        while (cursor.moveToNext()) {
            byte[] imgByte = cursor.getBlob(5);
            aux = new User(cursor.getString(1), cursor.getString(3), BitmapFactory.decodeByteArray(imgByte, 0, imgByte.length), cursor.getString(2), cursor.getString(4));

        }
        cursor.close();
        //System.out.println("Se encontro" + aux.getName());
        return aux;
    }

    public User findUserByEmail(String email) {
        User aux = null;
        String query = "SELECT * FROM " + dataBaseHelper.TABLE_NAME_USER + " WHERE " + dataBaseHelper.MAIL + " = ?";
        Cursor cursor = database.rawQuery(query, new String[]{email});
        Log.e(dataBaseHelper.class.getName(), query);

        while (cursor.moveToNext()) {
            byte[] imgByte = cursor.getBlob(5);
            aux = new User(cursor.getString(1), cursor.getString(3), BitmapFactory.decodeByteArray(imgByte, 0, imgByte.length), cursor.getString(2), cursor.getString(4));

        }
        cursor.close();
       // System.out.println("Se encontro" + aux.getName());
        return aux;
    }

    public Boolean userExistByEmail(String email){

        User aux = null;
        aux = findUserByEmail(email.trim());
        return aux != null;

    }

    public Boolean userExistByUsername(String username){
        User aux = null;
        aux = findUserByUsername(username.trim());
        return aux != null;
    }

    public void updateUser(Bitmap pp,String name,String username,String mail){
        String selection = dataBaseHelper.USERNAME+" LIKE ? ";
        byte[] data = getBitmapAsByteArray(pp);
        String args[] = {auth.getUsername()};
        ContentValues values = new ContentValues();
        values.put(dataBaseHelper.NAME_USER,name);
        values.put(dataBaseHelper.USERNAME,username);
        values.put(dataBaseHelper.MAIL,mail);
        values.put(dataBaseHelper.IMG_USER,data);
        int num = database.update(dataBaseHelper.TABLE_NAME_USER,values,selection,args);
        setAuth(findUserByUsername(username));
    }

    public static byte[] getBitmapAsByteArray(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
        return outputStream.toByteArray();
    }

    public dataBaseHelper getHelper () {
        return helper;
    }

    public SQLiteDatabase getDatabase () {
        return database;
    }

    public User logIn(String credential, String pass){
        User aux = null;
        aux = findUserByUsername(credential);
        if (aux == null){
            aux = findUserByEmail(credential);
            if(aux == null){
                return null;
            }
            else if( aux != null && aux.getPassword().equals(pass)){
                return aux;
            }
            else{
                return new User("Bad Credentials",null,null,null,null);
            }
        }
        else if( aux != null && aux.getPassword().equals(pass)){
            return aux;
        }
        else{
            return new User("Bad Credentials",null,null,null,null);
        }
    }


    //Fetch categories by name
    public Cursor fetchCategoryByName(String name) {
        String[] columns = new String[]{dataBaseHelper.NAME_CATEGOTY, dataBaseHelper.ID_CATEGORY};
        Cursor cursor = database.query("CATEGORY",columns,dataBaseHelper.NAME_CATEGOTY + " = ? " , new String[]{name},null,null,null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public Boolean validateDeleteCategory(String id) {
        String arreglohelper[] = new String[1];
        arreglohelper[0] = id;
        Cursor cursor = database.rawQuery("SELECT * FROM " +dataBaseHelper.TABLE_NAME_PRODUCT+ " WHERE " +dataBaseHelper.ID_CATEGORY+ " =?", arreglohelper);
        return !cursor.moveToNext();
    }

    //Delete Product
    public Boolean deleteProduct(Integer id) {
        database.delete(dataBaseHelper.TABLE_NAME_PRODUCT, dataBaseHelper.ID_PRODUCT +"= ?", new String[]{id.toString()});
        return Boolean.TRUE;
    }

    //update product
    public void updateProduct(Product product){
        ContentValues values = new ContentValues();
        values.put(dataBaseHelper.NAME_PRODUCT, product.getName());
        values.put(dataBaseHelper.DESCRIPRION, product.getDescription());
        values.put(dataBaseHelper.PRICE, product.getPrice());
        values.put(dataBaseHelper.ID_CATEGORY, product.getCategory().getId());
        database.update(dataBaseHelper.TABLE_NAME_PRODUCT, values, dataBaseHelper.ID_PRODUCT+ "= ?", new String[]{Integer.toString(product.getId())});

    //Delete Category
    public Boolean deleteCategory(String id) {
        database.delete(dataBaseHelper.TABLE_NAME_CATEGOIES, dataBaseHelper.ID_CATEGORY + " = ?", new String[]{id});
        return Boolean.TRUE;
    }

}
