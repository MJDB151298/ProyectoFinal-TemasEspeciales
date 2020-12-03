package com.example.proyectofinal;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.example.proyectofinal.connection.Manager;


public class MainActivity extends AppCompatActivity {

    TextView name,user,pass,mail;
    ImageView view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_register);
        name = findViewById(R.id.txtnameaux);
        user = findViewById(R.id.txtUsernameAux);
        pass = findViewById(R.id.txtPassAux);
        mail = findViewById(R.id.txtmailAux);
        view = findViewById(R.id.profile);
        System.out.println(Manager.getInstance(getApplicationContext()).getAuth().getName());
        /*name.setText(Manager.getInstance(getApplicationContext()).getAuth().getName());
        user.setText(Manager.getInstance(getApplicationContext()).getAuth().getUsername());
        pass.setText(Manager.getInstance(getApplicationContext()).getAuth().getPassword());
        mail.setText(Manager.getInstance(getApplicationContext()).getAuth().getMail());

        view.setImageBitmap(Manager.getInstance(getApplicationContext()).getAuth().getPp());*/



    }

    public void sendToRegister(View view){

    }
}