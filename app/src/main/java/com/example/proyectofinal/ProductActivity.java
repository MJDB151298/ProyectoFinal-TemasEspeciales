package com.example.proyectofinal;

import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.example.proyectofinal.fragments.products.AddProductFragment;
import com.example.proyectofinal.fragments.products.ListProductFragment;
import com.example.proyectofinal.helpers.FragmentHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ProductActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

       /** FloatingActionButton button = findViewById(R.id.callCreateProductButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentHelper.ReplaceFragment(new AddProductFragment(), ProductActivity.this);
            }
        });
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        ListProductFragment listProductFragment = new ListProductFragment();
        fragmentTransaction.replace(R.id.fragment_container, listProductFragment);
        fragmentTransaction.commit();**/
    }
}