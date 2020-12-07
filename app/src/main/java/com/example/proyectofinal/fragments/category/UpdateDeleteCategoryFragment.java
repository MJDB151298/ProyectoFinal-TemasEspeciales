package com.example.proyectofinal.fragments.category;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.proyectofinal.R;
import com.example.proyectofinal.connection.Manager;
import com.example.proyectofinal.connection.dataBaseHelper;
import com.example.proyectofinal.fragments.products.ListProductFragment;
import com.example.proyectofinal.helpers.FragmentHelper;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.storage.*;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UpdateDeleteCategoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UpdateDeleteCategoryFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private EditText editCategoryName;
    private ImageView imgEditCategory;
    private Button btnEditImage;
    private Button btnUpdateCategory;
    private Button btnDeleteCategory;
    private boolean cambioImagen = false;
    private Uri imageUri;
    private String idImagen;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private String idCategory;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public UpdateDeleteCategoryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UpdateDeleteCategoryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UpdateDeleteCategoryFragment newInstance(String param1, String param2) {
        UpdateDeleteCategoryFragment fragment = new UpdateDeleteCategoryFragment();
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
        View view = inflater.inflate(R.layout.fragment_update_delete_category, container, false);
        Bundle bundle = getArguments();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        if(bundle != null && bundle.containsKey("CATEGORY_NAME")){
            editCategoryName = view.findViewById(R.id.txtUpdateCategory);
            editCategoryName.setText(bundle.getString("CATEGORY_NAME"));
        }

        if(bundle != null && bundle.containsKey("CATEGORY_ID")){
            idCategory = bundle.getString("CATEGORY_ID");
        }

        if(bundle != null && bundle.containsKey("CATEGORY_PHOTO")){
            imgEditCategory = view.findViewById(R.id.imgUpdateCategory);
            storageReference = FirebaseStorage.getInstance().getReference().child("images/" + bundle.getString("CATEGORY_PHOTO"));

            try {
                final File localFile = File.createTempFile("Image" + bundle.getString("CATEGORY_PHOTO"), "jpg");
                storageReference.getFile(localFile)
                        .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                                imgEditCategory.setImageBitmap(bitmap);
                            }
                        });
            } catch (IOException e) {
                Toast.makeText(getContext(), "Error image" + bundle.getString("CATEGORY_NAME"), Toast.LENGTH_SHORT).show();
            }
        }

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        editCategoryName = getView().findViewById(R.id.txtUpdateCategory);
        imgEditCategory = getView().findViewById(R.id.imgUpdateCategory);
        btnEditImage = getView().findViewById(R.id.btnChangeImage);
        btnUpdateCategory = getView().findViewById(R.id.btnUpdateCategory);
        btnDeleteCategory = getView().findViewById(R.id.btnBorrarCategoria);
        btnUpdateCategory.setEnabled(true);

        editCategoryName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                btnUpdateCategory.setEnabled(editCategoryName.getText().length() > 0);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        btnEditImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosePic();
            }
        });
        
        btnUpdateCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateCategory();
            }
        });

        btnDeleteCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getContext())
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Borrar Categoria")
                        .setMessage("Esta seguro que desea eliminar esta categoria?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Bundle receivedBundle = getArguments();
                                if( Manager.getInstance(getContext()).open().validateDeleteCategory(receivedBundle.getString("CATEGORY_ID")))
                                {
                                    Manager.getInstance(getContext()).open().deleteCategory(receivedBundle.getString("CATEGORY_ID"));
                                    Manager.getInstance(getContext()).close();
                                    Toast.makeText(Objects.requireNonNull(getActivity()).getApplicationContext(), "DELETED: " + receivedBundle.getString("CATEGORY_ID"), Toast.LENGTH_SHORT).show();
                                    FragmentHelper.AddFragment(new ListCategoryFragment(), getActivity());
                                }
                                else {
                                    Toast.makeText(Objects.requireNonNull(getActivity()).getApplicationContext(), "HAS PRODUCT ASOCIATED, CAN'T BE ELIMINATED", Toast.LENGTH_SHORT).show();
                                }
                            }

                        })
                        .setNegativeButton("No", null)
                        .show();

            }
        });
    }

    private void updateCategory() {
        String nuevoNombre = editCategoryName.getText().toString();
        if(cambioImagen)
        {
            uploadPic();
        }

        dataBaseHelper conn = new dataBaseHelper(getContext());
        SQLiteDatabase db = conn.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(dataBaseHelper.NAME_CATEGOTY, nuevoNombre);
        if(cambioImagen)
        {
            values.put(dataBaseHelper.IMG_CATEGORY, idImagen);
        }
        String[] arreglohelper = new String[1];
        arreglohelper[0] = idCategory;
        long idResultado = db.update(dataBaseHelper.TABLE_NAME_CATEGOIES, values, " id_category =? ", arreglohelper);
        Toast.makeText(Objects.requireNonNull(getActivity()).getApplicationContext(), "UPDATED: " + idResultado, Toast.LENGTH_SHORT).show();

    }

    private void uploadPic() {
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Uploading image...");
        progressDialog.show();

        String randomkey = UUID.randomUUID().toString();
        idImagen = randomkey;
        storageReference = storage.getReference();
        StorageReference riversRef = storageReference.child("images/" + randomkey);
        UploadTask uploadTask = riversRef.putFile(imageUri);

        // Register observers to listen for when the download is done or if it fails
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                progressDialog.dismiss();
                Toast.makeText(getContext(), "Failed to Upload", Toast.LENGTH_LONG).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                progressDialog.dismiss();
                Toast.makeText(getContext(), "Uploaded image", Toast.LENGTH_LONG).show();
                FragmentHelper.ReplaceFragment(new ListCategoryFragment(), getActivity());
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull @NotNull UploadTask.TaskSnapshot snapshot) {
                double progressPercent = (100.00 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                progressDialog.setMessage("Progress: " + (int) progressPercent + "%");
            }
        });
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
            imgEditCategory.setImageURI(imageUri);
            setCambioImagen(true);
        }
    }

    public boolean isCambioImagen() {
        return cambioImagen;
    }

    public void setCambioImagen(boolean cambioImagen) {
        this.cambioImagen = cambioImagen;
    }
}