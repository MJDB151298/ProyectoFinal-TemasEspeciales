package com.example.proyectofinal.fragments.products;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
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
import com.example.proyectofinal.helpers.FragmentHelper;
import com.example.proyectofinal.models.CarItem;
import com.example.proyectofinal.models.Category;
import com.example.proyectofinal.models.Product;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
        final Bundle bundle = getArguments();
        TextView productDetailDescription = view.findViewById(R.id.productDetailDescription);
        TextView productDetailPrice = view.findViewById(R.id.productDetailPrice);

        List<Product> products = Product.getProducts(getContext());
        Product product = null;
        for(Product search : products){
            if(search.getId() == bundle.getInt("PRODUCT_ID")){
                System.out.println("El size de search: " + search.getImages().size());
                product = search;
            }

        }
        System.out.println("El size: " + product.getImages().size());
        //Product product = Product.getProductById(getContext(), bundle.getInt("PRODUCT_ID"));
        final List<Bitmap> images = new ArrayList<>();
        final CarouselView productDetailImages = view.findViewById(R.id.productPhotosCarousel);
        final ImageListener imageListener = new ImageListener() {
            @Override
            public void setImageForPosition(int position, ImageView imageView) {
                imageView.setImageBitmap(images.get(position));
            }
        };

        for(String imageId : product.getImages()){
            StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("images/" + imageId);
            try {
                final File localFile = File.createTempFile("Image" + product.getName(), "jpg");
                final Product finalProduct = product;
                storageReference.getFile(localFile)
                        .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                                images.add(bitmap);
                                productDetailImages.setImageListener(imageListener);
                                productDetailImages.setPageCount(images.size());

                            }
                        });
            } catch (IOException e) {
                System.out.println("guaaaay");
            }
        }


        System.out.println(images.size());



        Button plusButton = view.findViewById(R.id.plusButton);
        Button minusButton = view.findViewById(R.id.minusButton);
        final Button addToCartButton = view.findViewById(R.id.addToCartButton);
        //Si el producto se encuentra en el carro de compras, inhabilita el boton
        for(CarItem carItem : Manager.getInstance(getContext()).getCarItems()){
            if(carItem.getProduct().getId() == bundle.getInt("PRODUCT_ID")){
                addToCartButton.setEnabled(false);
                addToCartButton.setText("PRODUCT ALREADY IN CART");
            }
        }
        final TextView productQuantityText = view.findViewById(R.id.productQuantityText);

        productDetailDescription.setText(bundle.getString("PRODUCT_DESCRIPTION"));
        productDetailPrice.setText(Double.toString(bundle.getDouble("PRODUCT_PRICE")));

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
                Category category = Category.getCategoryByName(bundle.getString("CATEGORY_NAME"), getContext());//new Category(1, "Bebidas", null);
                Product product = new Product(bundle.getInt("PRODUCT_ID"), bundle.getString("PRODUCT_NAME"), bundle.getString("PRODUCT_DESCRIPTION"),
                        bundle.getDouble("PRODUCT_PRICE"), category);
                product.getImages().add(bundle.getString("PRODUCT_IMAGE"));
                Manager.getInstance(getContext()).getCarItems().add(new CarItem(product, Integer.parseInt(productQuantityText.getText().toString())));
                addToCartButton.setEnabled(false);
                addToCartButton.setText("PRODUCT ALREADY IN CART");
                //TODO: Saul actualizar la burbujita aqui
            }
        });
        return view;
    }
}