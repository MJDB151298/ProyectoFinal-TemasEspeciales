package com.example.proyectofinal.connection;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class dataBaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "ECOMERCE.DB";
    private static final int DB_VERSION = 1;
    // tabla category
    public static final String TABLE_NAME_CATEGOIES = "CATEGORY";
    public static final String NAME_CATEGOTY = "name";
    public static final String ID_CATEGORY = "id_category";
    public static final String IMG_CATEGORY = "image_category";
    // tabla product
    public static final String TABLE_NAME_PRODUCT = "PRODUCT";
    public static final String NAME_PRODUCT = "name";
    public static final String ID_PRODUCT = "id_product";
    public static final String PRICE = "price";
    public static final String DESCRIPRION = "description";
    // tabla user
    public static final String TABLE_NAME_USER = "USER_APP";
    public static final String NAME_USER = "name";
    public static final String MAIL = "mail";
    public static final String USERNAME = "username";
    public static final String PASSWORD_USER = "password";
    public static final String ID_USER = "id_user";
    public static final String IMG_USER = "image_user";


    private static final String TABLE_CATEGORY = "CREATE TABLE " + TABLE_NAME_CATEGOIES +
            "( " + ID_CATEGORY + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            NAME_CATEGOTY + " TEXT NOT NULL, "+IMG_CATEGORY+" BLOB NOT NULL )";

    private static final String TABLE_PRODUCT = "CREATE TABLE " + TABLE_NAME_PRODUCT +
            "( " + ID_PRODUCT + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            NAME_PRODUCT + " TEXT NOT NULL, "+DESCRIPRION+ " TEXT NOT NULL, "+PRICE+" REAL NOT NULL, "+ID_CATEGORY+" INTEGER NOT NULL," +
            " FOREIGN KEY ("+ID_CATEGORY+") REFERENCES "+TABLE_NAME_CATEGOIES+"("+ID_CATEGORY+") ) ";

    private static final String TABLE_USER = "CREATE TABLE " + TABLE_NAME_USER +
            "( " + ID_USER + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            NAME_USER + " TEXT NOT NULL, "+MAIL+ " TEXT UNIQUE NOT NULL, "+USERNAME+" TEXT UNIQUE NOT NULL, "+PASSWORD_USER+" TEXT NOT NULL, "+IMG_USER+" BLOB NOT NULL )";


    public dataBaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CATEGORY);
        db.execSQL(TABLE_PRODUCT);
        db.execSQL(TABLE_USER);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
       // db.execSQL("DROP TABLE IF EXISTS CATEGORY");
        db.execSQL("DROP TABLE IF EXISTS PRODUCT");
        db.execSQL("DROP TABLE IF EXISTS USER_APP");
        onCreate(db);
    }
}
