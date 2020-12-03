package com.example.proyectofinal.adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import com.example.proyectofinal.fragments.products.DetailProductFragment;
import com.example.proyectofinal.fragments.products.UpdateDeleteProductFragment;
import com.example.proyectofinal.helpers.ButtonHelper;
import com.example.proyectofinal.helpers.FragmentHelper;
import com.example.proyectofinal.models.Product;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.example.proyectofinal.R;

import java.util.List;

public class ListProductAdapter extends RecyclerView.Adapter<ListProductAdapter.ProductViewHolder> {
    private Context context;
    private List<Product> products;
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
        holder.bindData(products.get(position));
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        private TextView productNameText;
        private TextView productPriceText;
        private TextView productCategoryText;
        private TextView productIdText;
        private ImageView productImage;
        private Button productHandlerButton;

        public ProductViewHolder(@NonNull final View itemView) {
            super(itemView);
            productNameText = itemView.findViewById(R.id.productNameText);
            productPriceText = itemView.findViewById(R.id.productPriceText);
            productCategoryText = itemView.findViewById(R.id.productCategoryText);
            productIdText = itemView.findViewById(R.id.productIdText);
            productImage = itemView.findViewById(R.id.productImage);
            productHandlerButton = itemView.findViewById(R.id.productHandlerButton);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Ocultando el float action button
                    ButtonHelper.SwitchCallCreateProductButton((FloatingActionButton) activity.findViewById(R.id.callCreateProductButton), true);

                    Bundle bundle = new Bundle();
                    bundle.putString("PRODUCT_PRICE", productPriceText.getText().toString());

                    DetailProductFragment productDetailFragment = new DetailProductFragment();
                    productDetailFragment.setArguments(bundle);
                    FragmentHelper.ReplaceFragment(productDetailFragment, activity);
                }
            });
        }

        public void bindData(Product product){
            productImage.setImageResource(R.drawable.soap);
            System.out.println("ALOOOOO");
            productNameText.setText(product.getName());
            productPriceText.setText(Double.toString(product.getPrice()));
            productCategoryText.setText(product.getCategory().getName());
            productIdText.setText(Integer.toString(product.getId()));

            productHandlerButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putString("PRODUCT_NAME", productNameText.getText().toString());
                    bundle.putString("PRODUCT_PRICE", productPriceText.getText().toString());
                    bundle.putString("CATEGORY_NAME", productCategoryText.getText().toString());
                    bundle.putInt("PRODUCT_ID", Integer.parseInt(productIdText.getText().toString()));

                    UpdateDeleteProductFragment updateDeleteProductFragment = new UpdateDeleteProductFragment();
                    updateDeleteProductFragment.setArguments(bundle);
                    FragmentHelper.ReplaceFragment(updateDeleteProductFragment, activity);
                }
            });
        }
    }
}
