package com.example.proyectofinal.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import com.example.proyectofinal.fragments.products.DetailProductFragment;
import com.example.proyectofinal.fragments.products.UpdateDeleteProductFragment;
import com.example.proyectofinal.helpers.ButtonHelper;
import com.example.proyectofinal.helpers.FragmentHelper;
import com.example.proyectofinal.models.Product;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.example.proyectofinal.R;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class ListProductAdapter extends RecyclerView.Adapter<ListProductAdapter.ProductViewHolder> {
    private Context context;
    private List<Product> products;
    private static StorageReference storageReference;
    private static FragmentActivity activity;

    public ListProductAdapter(Context context, List<Product> products, FragmentActivity activity) {
        this.context = context;
        this.products = products;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view  = inflater.inflate(R.layout.product_row, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListProductAdapter.ProductViewHolder holder, int position) {
        System.out.println("Añadiendo en la posicion: " + position);
        holder.bindData(products.get(position), holder.itemView);
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        private TextView productNameText;
        private TextView productDescriptionText;
        private TextView productPriceText;
        private TextView productCategoryText;
        private TextView productIdText;
        private ImageView productImage;
        private Button productHandlerButton;

        public ProductViewHolder(@NonNull final View itemView) {
            super(itemView);
            productNameText = itemView.findViewById(R.id.productNameText);
            productDescriptionText = itemView.findViewById(R.id.productDescriptionText);
            productPriceText = itemView.findViewById(R.id.productPriceText);
            productCategoryText = itemView.findViewById(R.id.productCategoryText);
            productIdText = itemView.findViewById(R.id.productIdText);
            productImage = itemView.findViewById(R.id.productImage);
            productHandlerButton = itemView.findViewById(R.id.productHandlerButton);


        }

        public void bindData(final Product product, View view){
            System.out.println("Este es el id de la imagen! " + product.getImages().get(0));
            final String image = product.getImages().get(0);
            storageReference = FirebaseStorage.getInstance().getReference().child("images/" + image);
            try {
                final File localFile = File.createTempFile("Image" + product.getName(), "jpg");
                storageReference.getFile(localFile)
                        .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                                productImage.setImageBitmap(bitmap);
                            }
                        });
            } catch (IOException e) {
                System.out.println("guaaaay");
            }
            //productImage.setImageResource(R.drawable.soap);
            //productImage.setImageBitmap(BitmapFactory.decodeByteArray(valueDecoded, 0, valueDecoded.length));
            productNameText.setText(product.getName());
            productDescriptionText.setText(product.getDescription());
            productPriceText.setText(Double.toString(product.getPrice()));
            productCategoryText.setText(product.getCategory().getName());
            productIdText.setText(Integer.toString(product.getId()));

            productHandlerButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putString("PRODUCT_NAME", productNameText.getText().toString());
                    bundle.putDouble("PRODUCT_PRICE", Double.parseDouble(productPriceText.getText().toString()));
                    bundle.putString("CATEGORY_NAME", productCategoryText.getText().toString());
                    bundle.putString("PRODUCT_DESCRIPTION", productDescriptionText.getText().toString());
                    bundle.putString("PRODUCT_IMAGE", image);
                    bundle.putInt("PRODUCT_ID", Integer.parseInt(productIdText.getText().toString()));

                    UpdateDeleteProductFragment updateDeleteProductFragment = new UpdateDeleteProductFragment();
                    updateDeleteProductFragment.setArguments(bundle);
                    FragmentHelper.ReplaceFragment(updateDeleteProductFragment, activity);
                }
            });

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Ocultando el float action button
                    ButtonHelper.SwitchCallCreateProductButton((FloatingActionButton) activity.findViewById(R.id.callCreateProductButton), true);

                    Bundle bundle = new Bundle();
                    bundle.putString("PRODUCT_NAME", productNameText.getText().toString());
                    bundle.putDouble("PRODUCT_PRICE", Double.parseDouble(productPriceText.getText().toString()));
                    bundle.putString("CATEGORY_NAME", productCategoryText.getText().toString());
                    bundle.putString("PRODUCT_IMAGE", image);
                    bundle.putString("PRODUCT_DESCRIPTION", productDescriptionText.getText().toString());
                    bundle.putInt("PRODUCT_ID", Integer.parseInt(productIdText.getText().toString()));

                    DetailProductFragment productDetailFragment = new DetailProductFragment();
                    productDetailFragment.setArguments(bundle);
                    FragmentHelper.ReplaceFragment(productDetailFragment, activity);
                }
            });
        }
    }
}
