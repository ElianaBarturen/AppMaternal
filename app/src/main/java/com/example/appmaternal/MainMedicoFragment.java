package com.example.appmaternal;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.appmaternal.model.Sesion;
import com.example.appmaternal.model.Sesionmedico;
import com.example.appmaternal.response.LoginResponse;
import com.example.appmaternal.response.LoginResponseMedico;
import com.example.appmaternal.retrofit.ApiService;
import com.example.appmaternal.retrofit.RetrofitClient;
import com.example.appmaternal.util.Helper;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainMedicoFragment extends Fragment implements View.OnClickListener{

    MaterialButton btnIniciarSesionGestante,btnIngresarMedico;
    TextInputEditText txtClaveMedico,txtEmailMedico;
    private LinearLayout loginlayoutmedico;


    public static MainMedicoFragment newInstance(String param1, String param2) {
        MainMedicoFragment fragment = new MainMedicoFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



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
    public void onClick(final View view) {
        if (view.getId() == R.id.btnIniciarSesionGestante) {
            iniciarSesion();
        }else if (view.getId() == R.id.btnIngresarMedico) {
            Log.e("d","rt");
            loginmedico();
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_medico, container, false);

        btnIniciarSesionGestante = view.findViewById(R.id.btnIniciarSesionGestante);
        btnIniciarSesionGestante.setOnClickListener(this);
        btnIngresarMedico = view.findViewById(R.id.btnIngresarMedico);
        btnIngresarMedico.setOnClickListener(this);
        txtEmailMedico = view.findViewById(R.id.txtEmailMedico);
        txtClaveMedico = view.findViewById(R.id.txtClaveMedico);
        loginlayoutmedico = view.findViewById(R.id.loginlayoutmedico);

        return view;
    }
    private void loginmedico() {
        ApiService apiService = RetrofitClient.urlComercial.create(ApiService.class);

        // Obtener las credenciales
        String email = txtEmailMedico.getText().toString();
        String clave = Helper.convertPassMd5(txtClaveMedico.getText().toString());

        // Realizar el request (petición) al end point de login
        final Call<LoginResponseMedico> call = apiService.loginmedico(email, clave);
        call.enqueue(new Callback<LoginResponseMedico>() {
            @Override
            public void onResponse(final Call<LoginResponseMedico> call, final Response<LoginResponseMedico> response) {
                if (response.code() == 200) { // Login ha sido satisfactorio
                    // Almacenar la respuesta del end point en la variable loginResponse
                    LoginResponseMedico loginResponse = response.body();
                    boolean status = loginResponse.isStatus();

                    if (status) { // True
                        // *****************Asignar el token de la sesión del usuario
                        RetrofitClient.API_TOKEN = loginResponse.getData().getToken();
                        // *****************Asignar el token de la sesión del usuario

                        String nombreObs = loginResponse.getData().getNombres();
                        Log.e("Nombre ===>", nombreObs);


                        /* Almacenar los datos de la sesión */
                        Sesionmedico.DATOS_SESION = loginResponse.getData();

                        SharedPreferences sharedPreferences = getContext().getSharedPreferences("MedicoSession", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("NOMBRE_OBSTETRA", nombreObs);
                        editor.apply();


                        // Crear una nueva instancia del fragmento de destino
                        OpcionesMedicoFragment nuevoFragmento = new OpcionesMedicoFragment();
                        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                        transaction.replace(R.id.fragment_container_opciones_medico, nuevoFragmento);
                        transaction.addToBackStack(null); // Opcional si deseas que el usuario pueda volver con el botón de retroceso
                        transaction.commit();

                        // Ocultar el layout actual
                        loginlayoutmedico.setVisibility(View.GONE);
                    }
                } else {
                    // http status: 500, 400, 401, etc.
                    Log.e("Error ===>", response.message());
                }
            }

            @Override
            public void onFailure(final Call<LoginResponseMedico> call, final Throwable t) {
                // En caso ocurra un error a conectarse con el servicio web, mostrara
                Log.e("Fail ===>", t.getMessage());
            }
        });
    }




}