package com.example.proyectofinal.helpers;

import android.view.View;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ButtonHelper {
    public static void SwitchCallCreateProductButton(FloatingActionButton button, boolean status){
        if(status){
            button.setEnabled(false);
            button.setVisibility(View.INVISIBLE);
        }
        else{
            button.setEnabled(true);
            button.setVisibility(View.VISIBLE);
        }
    }
}
