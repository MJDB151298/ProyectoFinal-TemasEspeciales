package com.example.proyectofinal;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

//TODO: Decidir actividad principal de la aplicacion para saber donde poner el floating action button - Marcos

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView textview_forgotPasswordLogin = findViewById(R.id.textview_forgotPasswordLogin);
        TextView textview_registerLogin = findViewById(R.id.textview_registerLogin);
        Button button_login = findViewById(R.id.button_login);
    }

    public void sendToRegister(View view){

    }
}