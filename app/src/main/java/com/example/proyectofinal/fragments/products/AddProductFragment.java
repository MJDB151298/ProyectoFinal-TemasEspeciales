package com.example.proyectofinal.fragments.products;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.proyectofinal.R;
import com.example.proyectofinal.helpers.ButtonHelper;
import com.example.proyectofinal.helpers.FragmentHelper;
import com.example.proyectofinal.helpers.SpinnerHelper;
import com.example.proyectofinal.models.Category;
import com.example.proyectofinal.models.Product;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddProductFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddProductFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AddProductFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddProductFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddProductFragment newInstance(String param1, String param2) {
        AddProductFragment fragment = new AddProductFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_product, container, false);

        //Ocultando el float action button
        ButtonHelper.SwitchCallCreateProductButton((FloatingActionButton) getActivity().findViewById(R.id.callCreateProductButton), true);

        Bundle bundle = getArguments();
        if(bundle != null && bundle.containsKey("PRODUCT_NAME")){
            TextView productNameTextView = view.findViewById(R.id.productNameTextView);
            productNameTextView.setText(bundle.getString("PRODUCT_NAME"));
        }

        if(bundle != null && bundle.containsKey("PRODUCT_PRICE")){
            TextView productPriceText = view.findViewById(R.id.productPriceTextView);
            productPriceText.setText(bundle.getString("PRODUCT_PRICE"));
        }

        //Llenando el spinner con las categorias
        Spinner categorySpinner = view.findViewById(R.id.categorySpinner);
        ArrayAdapter<String> adapter = SpinnerHelper.fillCategorySpinner(categorySpinner, getContext());

        if(bundle != null && bundle.containsKey("CATEGORY_NAME")){
            int position = adapter.getPosition(bundle.getString("CATEGORY_NAME"));
            categorySpinner.setSelection(position);
        }

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

        final TextView productNameTextView = getView().findViewById(R.id.productNameTextView);
        final TextView productPriceText = getView().findViewById(R.id.productPriceTextView);
        final Button saveProductButton = getView().findViewById(R.id.saveProductButton);
        Button addCategoryButton = getView().findViewById(R.id.addCategoryButton);
        Button deleteCategoryButton = getView().findViewById(R.id.deleteCategoryButton);
        final Spinner categorySpinner = getView().findViewById(R.id.categorySpinner);
        saveProductButton.setEnabled(false);



        productNameTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {


            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(productNameTextView.getText().toString().length() > 0 && productPriceText.getText().toString().length() > 0){
                    saveProductButton.setEnabled(true);
                }
                else{
                    saveProductButton.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        productPriceText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(productNameTextView.getText().toString().length() > 0 && productPriceText.getText().toString().length() > 0){
                    saveProductButton.setEnabled(true);
                }
                else{
                    saveProductButton.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        saveProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String productName = productNameTextView.getText().toString();
                int productPrice = 0;
                try{
                    productPrice = Integer.parseInt(productPriceText.getText().toString());
                }catch(Exception e){
                    System.out.println("OOPS");
                }

                //TODO: USE DATABASE
                /**Category category = Category.getCategoryByName(v.getContext(), categorySpinner.getSelectedItem().toString());
                System.out.println(category.getId() + " - " + category.getName());
                DBManagerProducts dbManagerProducts = new DBManagerProducts(v.getContext()).open();
                dbManagerProducts.insert(productName, productPrice, category.getId());
                dbManagerProducts.close();**/

                FragmentHelper.AddFragment(new ListProductFragment(), getActivity());
            }
        });

        addCategoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: SAUL LLAMA TU FORMA DE GUARDAR CATEGORIAS DESDE AQUI
            }
        });

        deleteCategoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String productName = productNameTextView.getText().toString();
                int productPrice = 0;
                try{
                    productPrice = Integer.parseInt(productPriceText.getText().toString());
                }catch(Exception e){
                    System.out.println("OOPS");
                }

               /* final Category category = Category.getCategoryByName(getContext(), categorySpinner.getSelectedItem().toString());
                if(Product.isCategoryInProduct(getContext(), category)){
                    new AlertDialog.Builder(getContext())
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setTitle("Error")
                            .setMessage("Categoria se encuentra en uso")
                            .setNeutralButton("Ok", null)
                            .show();
                }
                else{
                    new AlertDialog.Builder(getContext())
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setTitle("Borrar Categoria")
                            .setMessage("Esta seguro que desea eliminar esta categoria?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                            {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    System.out.println(category.getId() + " - " + category.getName());
                                    DBManagerCategory dbManagerCategory = new DBManagerCategory(getContext()).open();
                                    dbManagerCategory.delete(category.getId());
                                    SpinnerHelper.fillCategorySpinner(categorySpinner, getContext());
                                    dbManagerCategory.close();
                                }

                            })
                            .setNegativeButton("No", null)
                            .show();

                }*/
            }
        });
    }
}