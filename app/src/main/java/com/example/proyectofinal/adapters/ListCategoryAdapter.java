package com.example.proyectofinal.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.proyectofinal.MainMenu;
import com.example.proyectofinal.R;
import com.example.proyectofinal.fragments.category.AddCategoryFragment;
import com.example.proyectofinal.fragments.category.ListCategoryFragment;
import com.example.proyectofinal.models.Category;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;

public class ListCategoryAdapter extends RecyclerView.Adapter<ListCategoryAdapter.MyViewHolder> implements Serializable {

    private final Context context;
    private ClickOnRowListener clickOnRowListener;
    private List<Category> elements;
    private StorageReference storageReference;

    public ListCategoryAdapter(Context context, List<Category> elements, ClickOnRowListener clickOnRowListener)
    {
        this.context = context;
        this.elements = elements;
        this.clickOnRowListener = clickOnRowListener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView txtNombreCategoria;
        ImageView image_categoria;
        Button categoryHandler;
        ClickOnRowListener clickOnRowListener;

        public MyViewHolder(@NonNull @org.jetbrains.annotations.NotNull View itemView, ClickOnRowListener clickOnRowListener) {
            super(itemView);
            txtNombreCategoria = itemView.findViewById(R.id.txtNombreCategoria);
            image_categoria = itemView.findViewById(R.id.img_categoria);
            categoryHandler = itemView.findViewById(R.id.categoryhandlerbutton);
            this.clickOnRowListener = clickOnRowListener;
            itemView.setOnClickListener(this);

//            categoryHandler.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    AddCategoryFragment addCategoryFragment = new AddCategoryFragment();
//                    FragmentManager fragmentManager = addCategoryFragment.getParentFragmentManager();
//                    fragmentManager.beginTransaction().replace(R.id.nav_host_fragment, addCategoryFragment, null).commit();
//                }
//            });
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
    public void onBindViewHolder(@NonNull @org.jetbrains.annotations.NotNull final ListCategoryAdapter.MyViewHolder holder, int position) {
        Category element = elements.get(position);
        holder.txtNombreCategoria.setText(String.valueOf(element.getName()));

        storageReference = FirebaseStorage.getInstance().getReference().child("images/" + element.getImage());

        try {
            final File localFile = File.createTempFile("Image" + element.getName(), "jpg");
            storageReference.getFile(localFile)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                            holder.image_categoria.setImageBitmap(bitmap);
                        }
                    });
        } catch (IOException e) {
            Toast.makeText(context, "Error image" + element.getName(), Toast.LENGTH_SHORT).show();
        }

        holder.categoryHandler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Aqui es donde cambias el fragmento desde el boton
                ((MainMenu) context).ChangeToAddCategoryFragment();
            }
        });
    }

    @Override
    public int getItemCount() {
        return elements.size();
    }

    public interface ClickOnRowListener {
        void ClickOnRow(int position);
    }

}
