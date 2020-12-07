package com.example.proyectofinal.helpers;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.example.proyectofinal.R;

public class FragmentHelper {
    //Removes previous fragment and loads new one
    public static void AddFragment(Fragment fragment, FragmentActivity activity){
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.remove(fragmentManager.findFragmentById(R.id.nav_host_fragment));
        fragmentTransaction.add(R.id.nav_host_fragment, fragment);
        fragmentTransaction.commit();
    }

    //Loads new fragment while keeping previous fragment in memory
    public static void ReplaceFragment(Fragment fragment, FragmentActivity activity){
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.nav_host_fragment, fragment).addToBackStack(null);
        fragmentTransaction.commit();
    }
}
