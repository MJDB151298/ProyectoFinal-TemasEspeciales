package com.example.proyectofinal.fragments.products;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.proyectofinal.R;
import com.example.proyectofinal.connection.Manager;
import com.example.proyectofinal.helpers.ButtonHelper;
import com.example.proyectofinal.models.CarItem;
import com.example.proyectofinal.models.Product;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DetailProductFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetailProductFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public DetailProductFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DetailProductFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DetailProductFragment newInstance(String param1, String param2) {
        DetailProductFragment fragment = new DetailProductFragment();
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
        View view = inflater.inflate(R.layout.fragment_detail_product, container, false);
        Bundle bundle = getArguments();
        TextView productDetailDescription = view.findViewById(R.id.productDetailDescription);
        TextView productDetailPrice = view.findViewById(R.id.productDetailPrice);


        //TODO: Poner imagenes que no sean placeholders, ni jabones, ni grappas con limon - Marcos
        final int[] testImages = {R.drawable.soap, R.drawable.grappa_con_limon};
        CarouselView productDetailImages = view.findViewById(R.id.productPhotosCarousel);
        productDetailImages.setPageCount(testImages.length);
        productDetailImages.setImageListener(new ImageListener() {
            @Override
            public void setImageForPosition(int position, ImageView imageView) {
                imageView.setImageResource(testImages[position]);
            }
        });

        Button plusButton = view.findViewById(R.id.plusButton);
        Button minusButton = view.findViewById(R.id.minusButton);
        Button addToCartButton = view.findViewById(R.id.addToCartButton);
        final TextView productQuantityText = view.findViewById(R.id.productQuantityText);

        productDetailDescription.setText(bundle.getString("PRODUCT_DESCRIPTION"));
        productDetailPrice.setText(bundle.getString("PRODUCT_PRICE"));

        plusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int total = Integer.parseInt(productQuantityText.getText().toString());
                total += 1;
                productQuantityText.setText(Integer.toString(total));
            }
        });

        minusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int total = Integer.parseInt(productQuantityText.getText().toString());
                if(total > 1){
                    total -= 1;
                    productQuantityText.setText(Integer.toString(total));
                }
            }
        });

        addToCartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Product product = null;//new Product(); //TODO: Buscar Producto por id - Marcos
                Manager.getInstance(getContext()).getCarItems().add(new CarItem(product, Integer.parseInt(productQuantityText.getText().toString())));
                //TODO: Saul actualizar la burbujita aqui
            }
        });
        return view;
    }
}