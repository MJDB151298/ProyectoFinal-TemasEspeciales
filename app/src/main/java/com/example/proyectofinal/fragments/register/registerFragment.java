package com.example.proyectofinal.fragments.register;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.proyectofinal.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.proyectofinal.MainActivity;
import com.example.proyectofinal.connection.Manager;
import com.example.proyectofinal.fragments.forgotpass.forgotPasswordFragment;
import com.example.proyectofinal.fragments.login.loginFragment;

import org.w3c.dom.Text;

import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

public class registerFragment extends Fragment {
    Button btnRegister;
    TextView txtPass,txtLogin,name,user,mail,pass,cPass;
    ImageView view;
    String url;
    ArrayList<TextView> views = new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_register, container, false);
        btnRegister = root.findViewById(R.id.btnregister);
        txtPass = root.findViewById(R.id.txtforgotpassR);
        txtLogin = root.findViewById(R.id.txtloginR);
        view = root.findViewById(R.id.imageRegister);
        name = root.findViewById(R.id.txtname);
        views.add(name);
        user = root.findViewById(R.id.txtuser);
        views.add(user);
        mail = root.findViewById(R.id.txtmailR);
        views.add(mail);
        pass = root.findViewById(R.id.txtpassR);
        views.add(pass);
        cPass = root.findViewById(R.id.txtconfim);
        views.add(cPass);
        Manager.getInstance(getActivity()).open();

        /*name.setText(Manager.getInstance(getActivity()).getAuth().getName());
        user.setText(Manager.getInstance(getActivity()).getAuth().getUsername());
        pass.setText(Manager.getInstance(getActivity()).getAuth().getPassword());
        mail.setText(Manager.getInstance(getActivity()).getAuth().getMail());

        view.setImageBitmap(Manager.getInstance(getActivity()).getAuth().getPp());*/


        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!(isEmpty()) && pass.getText().toString().equals(cPass.getText().toString())){
                    view.buildDrawingCache();
                    Bitmap bmap = view.getDrawingCache();
                    Manager.getInstance(getActivity()).createUser(bmap,name.getText().toString(),user.getText().toString(),mail.getText().toString(),pass.getText().toString());
                    Manager.getInstance(getActivity()).setAuth(Manager.getInstance(getActivity()).findUserByUsername(user.getText().toString()));
                    System.out.println(Manager.getInstance(getActivity()).getAuth().getName());
                    /*Intent intent = new Intent(getActivity(), MainActivity.class);
                    intent.putExtra("IMAGEN",url );
                    startActivity(intent);*/
                    /*name.setText(Manager.getInstance(getActivity()).getAuth().getName());
                    user.setText(Manager.getInstance(getActivity()).getAuth().getUsername());
                    pass.setText(Manager.getInstance(getActivity()).getAuth().getPassword());
                    mail.setText(Manager.getInstance(getActivity()).getAuth().getMail());

                    view.setImageBitmap(Manager.getInstance(getActivity()).getAuth().getPp());*/
                    Manager.getInstance(getActivity()).close();
                }
                else if( !(pass.getText().toString().equals(cPass.getText().toString()))){
                    pass.setError("Las contraseñas deben ser iguales");

                }

            }
        });

        txtPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment newFragment;
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                newFragment = new forgotPasswordFragment();
                transaction.replace(R.id.nav_host_outside_fragment, newFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        txtLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment newFragment;
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                newFragment = new loginFragment();
                transaction.replace(R.id.nav_host_outside_fragment, newFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });



        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getGallery();
            }
        });

        return root;
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
            url = patch.toString();
            view.setImageURI(patch);
        }
    }

    private Boolean isEmpty(){
        int num = 0;
        for (TextView view:views){
            if(view.getText().toString().isEmpty()){
                view.setError("Este campo es obligatorio");
                num++;
            }
        }
        return num == 0?false:true;
    }



}
