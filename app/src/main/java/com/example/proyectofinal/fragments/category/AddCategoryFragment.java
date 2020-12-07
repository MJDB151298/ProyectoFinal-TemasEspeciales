package com.example.proyectofinal.fragments.category;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.proyectofinal.R;
import com.example.proyectofinal.connection.dataBaseHelper;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.UUID;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddCategoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddCategoryFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private EditText txtCategoryName;
    private Button btnAddImage;
    private Button btnSave;
    private ImageView imgPreviewImage;
    public Uri imageUri;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private boolean hayImagen = false;
    private String idImagen;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AddCategoryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddCategoryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddCategoryFragment newInstance(String param1, String param2) {
        AddCategoryFragment fragment = new AddCategoryFragment();
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
        return inflater.inflate(R.layout.fragment_add_category, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        txtCategoryName = getView().findViewById(R.id.txtCategoryName);
        imgPreviewImage = getView().findViewById(R.id.img_category_preview);
        btnAddImage = getView().findViewById(R.id.btn_selectCategoryImage);
        btnSave = getView().findViewById(R.id.btnSaveCategory);

        txtCategoryName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(txtCategoryName.getText().length() > 0)
                {
                    btnSave.setEnabled(true);
                }
                else {
                    btnSave.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveCategory();
            }
        });

        btnAddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosePic();
            }
        });
    }

    private void saveCategory() {
        String nombre = txtCategoryName.getText().toString();
        if(hayImagen)
        {
            uploadPic();
        }

        dataBaseHelper conn = new dataBaseHelper(getContext());
        SQLiteDatabase db = conn.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(dataBaseHelper.NAME_CATEGOTY, nombre);
        if(hayImagen)
        {
            values.put(dataBaseHelper.IMG_CATEGORY, idImagen);
        }
        long idResultado = db.insert(dataBaseHelper.TABLE_NAME_CATEGOIES, null, values);
        Toast.makeText(Objects.requireNonNull(getActivity()).getApplicationContext(), "CREATED: " + idResultado, Toast.LENGTH_SHORT).show();
        txtCategoryName.setText("");
        Bitmap bitmap = BitmapFactory.decodeFile("src/main/res/drawable/baseline_add_photo_alternate_black_18dp.png");
        imgPreviewImage.setImageBitmap(bitmap);
    }

    private void choosePic() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1 && resultCode==RESULT_OK && data!=null && data.getData()!=null)
        {
            imageUri = data.getData();
            imgPreviewImage.setImageURI(imageUri);
            setHayImagen(true);
        }
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

    public boolean isHayImagen() {
        return hayImagen;
    }

    public void setHayImagen(boolean hayImagen) {
        this.hayImagen = hayImagen;
    }
}