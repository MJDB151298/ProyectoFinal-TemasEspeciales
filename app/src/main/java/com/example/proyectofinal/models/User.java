package com.example.proyectofinal.models;

import android.graphics.Bitmap;
import android.media.Image;

public class User {
    private String name;
    private String username;
    private Bitmap pp;
    private String mail;
    private String password;

    public User(String name, String username, Bitmap pp, String mail, String password) {
        this.name = name;
        this.username = username;
        this.pp = pp;
        this.mail = mail;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Bitmap getPp() {
        return pp;
    }

    public void setPp(Bitmap pp) {
        this.pp = pp;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
