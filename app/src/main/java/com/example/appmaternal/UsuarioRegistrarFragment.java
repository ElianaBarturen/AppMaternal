package com.example.appmaternal;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.text.InputFilter;
import android.text.SpannableString;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appmaternal.response.UsuariaRegistrarResponse;
import com.example.appmaternal.retrofit.ApiService;
import com.example.appmaternal.retrofit.RetrofitClient;
import com.example.appmaternal.util.Helper;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;


import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UsuarioRegistrarFragment extends Fragment implements View.OnClickListener{

    TextInputEditText txtNombres,txtContraseña, txtEmail;
    MaterialButton btnRegistrar, btnIniciarSesion;
    CheckBox checkBoxPrivacyPolicy;

    String text = "Acepto las políticas de privacidad";

    // Crear un SpannableString para hacer clic en parte del texto
    SpannableString spannableString = new SpannableString(text);





    public static UsuarioRegistrarFragment newInstance(String param1, String param2) {
        UsuarioRegistrarFragment fragment = new UsuarioRegistrarFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_usuario_registrar, container, false);

        txtNombres= view.findViewById(R.id.txtNombres);
        txtNombres.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        txtContraseña= view.findViewById(R.id.txtClave);
        txtEmail = view.findViewById(R.id.txtEmail);
        btnRegistrar = view.findViewById(R.id.btnRegistrar);
        btnRegistrar.setOnClickListener(this);
        btnIniciarSesion = view.findViewById(R.id.btnIniciarSesion);
        btnIniciarSesion.setOnClickListener(this);
        checkBoxPrivacyPolicy= view.findViewById(R.id.checkbox_privacy_policy);
        checkBoxPrivacyPolicy.setOnClickListener(this);
        //Validar si tenemos parámetros de entrada

        //Encuentra el TextView
        TextView privacyPolicyTextView = view.findViewById(R.id.checkbox_privacy_policy);

        // Configura el clic en el TextView
        privacyPolicyTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPrivacyPolicyDialog();
            }
        });


        return view;
    }

    public void onClick(final View view) {

            if (view.getId() == R.id.btnRegistrar) {
                registrar();
            }else if (view.getId() == R.id.btnIniciarSesion){
                iniciarSesion();
            }


    }

    private void iniciarSesion() {
        // Obtener el contexto del fragmento
        Context context = getActivity();
        if (context != null) {
            // Crear un Intent para iniciar MainActivity
            Intent intent = new Intent(context, MainActivity.class);
            // Iniciar la actividad
            context.startActivity(intent);

        }
    }


    private boolean validarDatos() {
        if (txtNombres.getText().toString().isEmpty()) {
            Toast.makeText(getActivity(), "Falta ingresar nombres y apellidos de gestante", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (txtEmail.getText().toString().isEmpty()) {
            Toast.makeText(getActivity(), "Falta ingresar el email", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (txtContraseña.getText().toString().isEmpty()) {
            Toast.makeText(getActivity(), "Falta ingresar la contraseña", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(!checkBoxPrivacyPolicy.isChecked()){
            Toast.makeText(getActivity(), "Seleccione la casilla de políticas y privacidad", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

   private void registrar() {
        if(validarDatos()){
            String email = txtEmail.getText().toString();
            String contraseña = txtContraseña.getText().toString();
            String nombres = txtNombres.getText().toString();


            final ApiService apiService = RetrofitClient.createService();
            final Call<UsuariaRegistrarResponse> call = apiService.registrarUsuaria(nombres,email,contraseña);
            call.enqueue(new Callback<UsuariaRegistrarResponse>() {
                @Override
                public void onResponse(final Call<UsuariaRegistrarResponse> call, final Response<UsuariaRegistrarResponse> response) {
                    if(response.code()==200){
                        if(response.body().isStatus()){

                            Helper.mensajeInformacion(getActivity(), "Mensaje","Usuaria registrada correctamente");
                            txtNombres.setText("");
                            txtEmail.setText("");
                            txtContraseña.setText("");
                        }
                    }else{
                        try {
                            Log.e("Error al registrar al usuaria, existente", response.errorBody().string());
                            Helper.mensajeError(getActivity(), "El email ya ha sido registrado \nPor favor verificar", response.errorBody().string());
                        } catch (final IOException e) {
                            throw new RuntimeException(e);
                        }

                    }
                }

                @Override
                public void onFailure(final Call<UsuariaRegistrarResponse> call, final Throwable t) {
                    Log.e("Error al conectar con el servicio web de usuaria", t.getMessage());

                }
            });

        }
    }

    private void showPrivacyPolicyDialog() {
        // Infla el diseño del diálogo
        LayoutInflater inflater = LayoutInflater.from(requireContext());
        View dialogView = inflater.inflate(R.layout.politicas_privacidad, null);

        // Configura el diálogo
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setView(dialogView)
                .setTitle("Políticas de Privacidad")
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss(); // Solo cerrar el diálogo
                    }
                })
                .setCancelable(true); // Permite que el diálogo sea cancelable si es necesario

        // Muestra el diálogo
        AlertDialog dialog = builder.create();
        dialog.show();
    }




}

