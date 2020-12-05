package com.example.proyectofinal.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.proyectofinal.R;
import com.example.proyectofinal.models.Category;

import java.io.Serializable;
import java.util.List;

public class ListCategoryAdapter extends RecyclerView.Adapter<ListCategoryAdapter.MyViewHolder> implements Serializable {

    private final Context context;
    private ClickOnRowListener clickOnRowListener;
    private List<Category> elements;

    public ListCategoryAdapter(Context context, List<Category> elements, ClickOnRowListener clickOnRowListener)
    {
        this.context = context;
        this.elements = elements;
        this.clickOnRowListener = clickOnRowListener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView txtNombreCategoria;
        ImageView image_categoria;
        ClickOnRowListener clickOnRowListener;

        public MyViewHolder(@NonNull @org.jetbrains.annotations.NotNull View itemView, ClickOnRowListener clickOnRowListener) {
            super(itemView);
            txtNombreCategoria = itemView.findViewById(R.id.txtNombreCategoria);
            image_categoria = itemView.findViewById(R.id.img_categoria);
            this.clickOnRowListener = clickOnRowListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            clickOnRowListener.ClickOnRow(getAdapterPosition());
        }

    }

    @NonNull
    @org.jetbrains.annotations.NotNull
    @Override
    public ListCategoryAdapter.MyViewHolder onCreateViewHolder(@NonNull @org.jetbrains.annotations.NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_row, parent, false);
        return new MyViewHolder(view, clickOnRowListener);
    }

    @Override
    public void onBindViewHolder(@NonNull @org.jetbrains.annotations.NotNull ListCategoryAdapter.MyViewHolder holder, int position) {
        Category element = elements.get(position);
        holder.txtNombreCategoria.setText(String.valueOf(element.getName()));
        holder.image_categoria.setBackgroundResource(R.drawable.grappa_con_limon);
    }

    @Override
    public int getItemCount() {
        return elements.size();
    }

    public interface ClickOnRowListener {
        void ClickOnRow(int position);
    }

}
