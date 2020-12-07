package com.example.proyectofinal.helpers;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import com.example.proyectofinal.models.Category;

import java.util.ArrayList;
import java.util.List;

public class SpinnerHelper {
    public static ArrayAdapter<String> fillCategorySpinner(Spinner spinner, Context context){
        List<Category> categoryList = Category.getCategories(context);
        List<String> categoryName = new ArrayList<>();
        for(Category category : categoryList){
            categoryName.add(category.getName());
        }
        System.out.println(categoryName.size());
        //TODO: Llenar categoria con base de datos - Marcos
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                context, android.R.layout.simple_spinner_item, categoryName);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        return adapter;
    }
}
