package com.example.proyectofinal.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import com.example.proyectofinal.R;
import com.example.proyectofinal.connection.Manager;
import com.example.proyectofinal.fragments.car.CarFragment;
import com.example.proyectofinal.fragments.products.ListProductFragment;
import com.example.proyectofinal.helpers.FragmentHelper;
import com.example.proyectofinal.models.CarItem;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class CarAdapter extends RecyclerView.Adapter<CarAdapter.CarViewHolder>{

    private static Context context;
    private static List<CarItem> carItems;
    private static TextView carInfoText;
    private static FragmentActivity activity;
    private static StorageReference storageReference;

    public CarAdapter(Context context, List<CarItem> carItems, TextView carInfoText, FragmentActivity activity){
        this.context = context;
        this.carItems = carItems;
        this.carInfoText = carInfoText;
        this.activity = activity;
    }

    @NonNull
    @Override
    public CarAdapter.CarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view  = inflater.inflate(R.layout.caritem_row, parent, false);
        return new CarAdapter.CarViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CarAdapter.CarViewHolder holder, int position) {
        holder.bindData(carItems.get(position));
    }

    @Override
    public int getItemCount() {
        return carItems.size();
    }

    public static class CarViewHolder extends RecyclerView.ViewHolder{
        private TextView carItemDescriptionText;
        private TextView carItemPriceText;
        private TextView carQuantityText;
        private ImageView carItemImage;
        private Button carMinusButton;
        private Button carPlusButton;
        private Button carDeleteButton;

        public CarViewHolder(@NonNull View itemView) {
            super(itemView);
            carItemDescriptionText = itemView.findViewById(R.id.carItemDescriptionText);
            carItemPriceText = itemView.findViewById(R.id.carItemPriceText);
            carQuantityText = itemView.findViewById(R.id.carQuantityText);
            carItemImage = itemView.findViewById(R.id.carItemImage);
            carMinusButton = itemView.findViewById(R.id.carMinusButton);
            carPlusButton = itemView.findViewById(R.id.carPlusButton);
            carDeleteButton = itemView.findViewById(R.id.carDeleteButton);

        }

        public void bindData(final CarItem carItem){
            System.out.println("Binding: " + carItem.getProduct().getName());
            final String image = carItem.getProduct().getImages().get(0);
            storageReference = FirebaseStorage.getInstance().getReference().child("images/" + image);
            try {
                final File localFile = File.createTempFile("Image" + carItem.getProduct().getName(), "jpg");
                storageReference.getFile(localFile)
                        .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                                carItemImage.setImageBitmap(bitmap);
                            }
                        });
            } catch (IOException e) {
                System.out.println("guaaaay");
            }
            carItemDescriptionText.setText(carItem.getProduct().getDescription());
            carItemPriceText.setText(Double.toString(carItem.getProduct().getPrice()));
            carQuantityText.setText(Integer.toString(carItem.getQuantity()));

            carMinusButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int total = Integer.parseInt(carQuantityText.getText().toString());
                    if(total > 1){
                        total -= 1;
                        carQuantityText.setText(Integer.toString(total));
                    }
                    double totalPrice = 0;
                    carItem.setQuantity(Integer.parseInt(carQuantityText.getText().toString()));
                    for(CarItem carItem : carItems){
                        totalPrice += (carItem.getProduct().getPrice() * carItem.getQuantity());
                    }

                    carInfoText.setText("Sub total (" + Integer.toString(carItem.getQuantity()) + " items): " + Double.toString(totalPrice));


                }
            });

            carPlusButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int total = Integer.parseInt(carQuantityText.getText().toString());
                    total += 1;
                    carQuantityText.setText(Integer.toString(total));
                    double totalPrice = 0;
                    carItem.setQuantity(Integer.parseInt(carQuantityText.getText().toString()));
                    for(CarItem carItem : carItems){
                        totalPrice += (carItem.getProduct().getPrice() * carItem.getQuantity());
                    }
                    carInfoText.setText("Sub total (" + Integer.toString(carItem.getQuantity()) + " items): " + Double.toString(totalPrice));
                }
            });

            carDeleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new AlertDialog.Builder(context)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setTitle("Remover del carrito")
                            .setMessage("Esta seguro que desea remover este producto del carrito de compras?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                            {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    carItems.remove(carItem);
                                    FragmentHelper.AddFragment(new CarFragment(), activity);
                                }

                            })
                            .setNegativeButton("No", null)
                            .show();

                }
            });
        }
    }
}









