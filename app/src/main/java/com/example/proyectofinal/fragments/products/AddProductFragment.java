package com.example.proyectofinal.fragments.products;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;
import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.util.UUID;

import static android.app.Activity.RESULT_OK;

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

    //Components
    ImageButton selectImageButton;
    ImageView productImageView;
    private boolean hayImagen = false;
    private StorageReference storageReference;
    private String idImagen;
    public Uri imageUri;

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
        final TextView productDescriptionText = getView().findViewById(R.id.productDescriptionTextView);
        final Button saveProductButton = getView().findViewById(R.id.saveProductButton);
        Button addCategoryButton = getView().findViewById(R.id.addCategoryButton);
        final Spinner categorySpinner = getView().findViewById(R.id.categorySpinner);
        saveProductButton.setEnabled(false);

        selectImageButton = getView().findViewById(R.id.selectImageButton);
        selectImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getGallery();
            }
        });

        productImageView = getView().findViewById(R.id.productImageView);



        productNameTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {


            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(productNameTextView.getText().toString().length() > 0 && productPriceText.getText().toString().length() > 0
                        && productDescriptionText.getText().toString().length() > 0){
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
                if(productNameTextView.getText().toString().length() > 0 && productPriceText.getText().toString().length() > 0
                && productDescriptionText.getText().toString().length() > 0){
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

        productDescriptionText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(productNameTextView.getText().toString().length() > 0 && productPriceText.getText().toString().length() > 0
                        && productDescriptionText.getText().toString().length() > 0){
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
                String productDescription = productDescriptionText.getText().toString();
                double productPrice = 0;
                try{
                    productPrice = Double.parseDouble(productPriceText.getText().toString());
                }catch(Exception e){
                    System.out.println("OOPS");
                }

                if(hayImagen)
                    uploadPic();


                Category category = Category.getCategoryByName(categorySpinner.getSelectedItem().toString(), v.getContext());
                Product product = new Product(-1, productName, productDescription, productPrice, category);
                Product.saveProduct(v.getContext(), product, idImagen);

               // BitmapDrawable drawable = (BitmapDrawable) productImageView.getDrawable();
                //Bitmap bitmap = drawable.getBitmap();
               // ByteArrayOutputStream stream = new ByteArrayOutputStream();
               // bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
               // String image = Base64.encodeToString(stream.toByteArray(), Base64.DEFAULT);

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
    }

    private void uploadPic() {

        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Uploading image...");
        progressDialog.show();

        String randomkey = UUID.randomUUID().toString();
        idImagen = randomkey;
        StorageReference riversRef = storageReference.child("images/" + randomkey);
        UploadTask uploadTask = riversRef.putFile(imageUri);

        // Register observers to listen for when the download is done or if it fails
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                progressDialog.dismiss();
                Toast.makeText(getContext(), "Failed to Upload", Snackbar.LENGTH_LONG).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                progressDialog.dismiss();
                Toast.makeText(getContext(), "Uploaded image", Toast.LENGTH_LONG).show();
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull @NotNull UploadTask.TaskSnapshot snapshot) {
                double progressPercent = (100.00 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                progressDialog.setMessage("Progress: " + (int) progressPercent + "%");
            }
        });
    }

    private void getGallery(){
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        intent.setType("image/");
        hayImagen = true;
        startActivityForResult(intent.createChooser(intent,"Seleccione la aplicacion"),200);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            Uri patch = data.getData();
            productImageView.setImageURI(patch);
            productImageView.setVisibility(View.VISIBLE);
            selectImageButton.setVisibility(View.INVISIBLE);
        }
    }
}