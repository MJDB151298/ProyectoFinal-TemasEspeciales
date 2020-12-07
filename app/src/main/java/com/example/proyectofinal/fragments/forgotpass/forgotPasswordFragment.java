package com.example.proyectofinal.fragments.forgotpass;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.proyectofinal.R;
import com.example.proyectofinal.connection.Manager;
import com.example.proyectofinal.fragments.login.loginFragment;
import com.example.proyectofinal.fragments.register.registerFragment;
import com.example.proyectofinal.models.User;

public class forgotPasswordFragment extends Fragment {
    TextView txtRegister,txtLogin,mail;
    Button getPass;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_forgot_password, container, false);

        txtLogin = root.findViewById(R.id.txtloginF);
        txtRegister = root.findViewById(R.id.txtregisterF);
        mail = root.findViewById(R.id.txtmailF);

        txtRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment newFragment;
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                newFragment = new registerFragment();
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

        getPass = root.findViewById(R.id.btngetpass);
        getPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mail.getText().toString().isEmpty()){
                    mail.setError("Debe introducir su correo");
                }
                else{
                    User auth = Manager.getInstance(getActivity()).findUserByEmail(mail.getText().toString());
                    if(auth == null){
                        new AlertDialog.Builder(getActivity())
                                .setTitle("No existe ningun usuario con este correo")
                                .setMessage("Por favor verifique")
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                }).show();
                    }
                    else {
                        new AlertDialog.Builder(getActivity())
                                .setTitle("Su contraseña es")
                                .setMessage(auth.getPassword())
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                }).show();
                    }
                }

            }
        });

        return root;
    }
}
