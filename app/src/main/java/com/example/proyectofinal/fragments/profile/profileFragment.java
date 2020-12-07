package com.example.proyectofinal.fragments.profile;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.proyectofinal.MainMenu;
import com.example.proyectofinal.R;
import com.example.proyectofinal.connection.Manager;

import java.util.ArrayList;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;

public class profileFragment extends Fragment {

    private TextView name,mail,username;
    private Button btnEdit,btnCancel;
    private ImageView view;
    private Boolean editable = false;
    private View myview;
    ArrayList<TextView> views = new ArrayList<>();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_profile, container, false);
        myview = root;
        name = root.findViewById(R.id.txtnameProfile);
        views.add(name);
        mail = root.findViewById(R.id.txtmailProfile);

        username = root.findViewById(R.id.txtuserProfile);
        btnEdit = root.findViewById(R.id.btnEditUser);
        btnCancel = root.findViewById(R.id.btnCancelP);
        name.setText(Manager.getInstance(getActivity()).getAuth().getName());
        mail.setText(Manager.getInstance(getActivity()).getAuth().getMail());
        username.setText(Manager.getInstance(getActivity()).getAuth().getUsername());
        view = root.findViewById(R.id.imagePP);
        view.setImageBitmap(Manager.getInstance(getActivity()).getAuth().getPp());
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(editable){
                    getGallery();
                }

            }
        });
        //editProfile();
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(editable){
                    Boolean mailExtist = Manager.getInstance(getActivity()).userExistByEmail(mail.getText().toString().trim()),
                            usernameExist = Manager.getInstance(getActivity()).userExistByUsername(username.getText().toString().trim());
                    if(mailExtist){
                        mailExtist = mail.getText().toString().trim().equals(Manager.getInstance(getActivity()).getAuth().getMail())?false:true;

                    }
                    if(usernameExist){
                        usernameExist = username.getText().toString().trim().equals(Manager.getInstance(getActivity()).getAuth().getUsername())?false:true;
                    }

                    if( !(isEmpty()) && !(mailExtist) && !(usernameExist)  ){
                        view.buildDrawingCache();
                        Bitmap bmap = view.getDrawingCache();
                        Manager.getInstance(getActivity()).updateUser(bmap,name.getText().toString().trim(),username.getText().toString().trim(),mail.getText().toString().trim());
                        new AlertDialog.Builder(getActivity())
                                .setTitle("Actualizado")
                                .setMessage(name.getText().toString()+" se actualizo con exito")
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                }).show();
                        name.setText(Manager.getInstance(getActivity()).getAuth().getName());
                        mail.setText(Manager.getInstance(getActivity()).getAuth().getMail());
                        username.setText(Manager.getInstance(getActivity()).getAuth().getUsername());
                        view.setImageBitmap(Manager.getInstance(getActivity()).getAuth().getPp());

                    }
                    else if(mailExtist){
                        new AlertDialog.Builder(getActivity())
                                .setTitle("Email Existe")
                                .setMessage("Este correo ya existe ")
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                }).show();
                        name.setText(Manager.getInstance(getActivity()).getAuth().getName());
                        mail.setText(Manager.getInstance(getActivity()).getAuth().getMail());
                        username.setText(Manager.getInstance(getActivity()).getAuth().getUsername());
                        view.setImageBitmap(Manager.getInstance(getActivity()).getAuth().getPp());
                    }
                    else if(usernameExist){
                        new AlertDialog.Builder(getActivity())
                                .setTitle("Username Existe")
                                .setMessage("Este nombre de usuario ya existe ")
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                }).show();
                        name.setText(Manager.getInstance(getActivity()).getAuth().getName());
                        mail.setText(Manager.getInstance(getActivity()).getAuth().getMail());
                        username.setText(Manager.getInstance(getActivity()).getAuth().getUsername());
                        view.setImageBitmap(Manager.getInstance(getActivity()).getAuth().getPp());

                    }

                }
                editProfile();

            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //btnCancel.setVisibility(View.INVISIBLE);
                editProfile();
                name.setText(Manager.getInstance(getActivity()).getAuth().getName());
                mail.setText(Manager.getInstance(getActivity()).getAuth().getMail());
                username.setText(Manager.getInstance(getActivity()).getAuth().getUsername());
                view.setImageBitmap(Manager.getInstance(getActivity()).getAuth().getPp());
            }
        });

        return root;
    }


    public void editProfile(){

        editable = !editable;
        btnEdit.setText(editable?"Update":"Start Editing");
        if(editable){

            name.setEnabled(true);
            username.setEnabled(true);
            mail.setEnabled(true);
            btnCancel.setVisibility(View.VISIBLE);
        }
        else {
            name.setEnabled(false);
            username.setEnabled(false);
            mail.setEnabled(false);
            btnCancel.setVisibility(View.INVISIBLE);

        }

    }

    private void getGallery(){
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        intent.setType("image/");
        startActivityForResult(intent.createChooser(intent,"Seleccione la aplicacion"),200);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            Uri patch = data.getData();
            //url = patch.toString();
            view.setImageURI(patch);
        }
    }

    public boolean isEmpty(){
        ViewGroup viewgroup= (ViewGroup) myview;

        int v = viewgroup.getChildCount();
        for (int i=0;i<viewgroup.getChildCount();i++) {
            View aux=viewgroup.getChildAt(i);

            if(aux instanceof EditText){
               if( ((EditText) aux).getText().toString().isEmpty() ) {
                   ((EditText) aux).setError("This cannot be empty");
                   return true;
               }
            }

        }
        return false;

    }


}
