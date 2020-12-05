package com.example.proyectofinal.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.proyectofinal.R;
import com.example.proyectofinal.models.CarItem;

import java.util.List;

public class CarAdapter extends RecyclerView.Adapter<CarAdapter.CarViewHolder>{

    private Context context;
    private List<CarItem> carItems;

    public CarAdapter(Context context, List<CarItem> carItems){
        this.context = context;
        this.carItems = carItems;
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

        public CarViewHolder(@NonNull View itemView) {
            super(itemView);
            carItemDescriptionText = itemView.findViewById(R.id.carItemDescriptionText);
            carItemPriceText = itemView.findViewById(R.id.carItemPriceText);
            carQuantityText = itemView.findViewById(R.id.carQuantityText);
            carItemImage = itemView.findViewById(R.id.carItemImage);
            carMinusButton = itemView.findViewById(R.id.carMinusButton);
            carPlusButton = itemView.findViewById(R.id.carPlusButton);

            carMinusButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int total = Integer.parseInt(carQuantityText.getText().toString());
                    if(total > 1){
                        total -= 1;
                        carQuantityText.setText(Integer.toString(total));
                    }
                }
            });

            carPlusButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int total = Integer.parseInt(carQuantityText.getText().toString());
                    total += 1;
                    carQuantityText.setText(Integer.toString(total));
                }
            });
        }

        public void bindData(CarItem carItem){
            carItemImage.setImageResource(R.drawable.soap);
            carItemDescriptionText.setText(carItem.getProduct().getDescription());
            carItemPriceText.setText(Double.toString(carItem.getProduct().getPrice()));
            carQuantityText.setText(Integer.toString(carItem.getQuantity()));
        }
    }
}









