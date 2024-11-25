package com.example.appmaternal;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.example.appmaternal.model.DatosControlGestante;
import com.example.appmaternal.model.Gestante;
import com.example.appmaternal.model.Semanas;
import com.example.appmaternal.response.DatosControlConsultarResponse;
import com.example.appmaternal.response.DatosControlConsultarResponsePrueba;
import com.example.appmaternal.response.GestanteConsultarResponse;
import com.example.appmaternal.response.SemanasResponse;
import com.example.appmaternal.retrofit.ApiService;
import com.example.appmaternal.retrofit.RetrofitClient;
import com.example.appmaternal.util.Helper;
import com.google.android.material.textview.MaterialTextView;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.LongSummaryStatistics;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BienvenidoFragment extends Fragment {
    ProgressBar progressBarSemana;
    MaterialTextView txtSemana;
    int numsem;

    public static BienvenidoFragment newInstance(String param1, String param2) {
        BienvenidoFragment fragment = new BienvenidoFragment();
        Bundle args = new Bundle();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bienvenido, container, false);
        progressBarSemana = view.findViewById(R.id.progressBarSemana);
        txtSemana = view.findViewById(R.id.txtSemanaNumber);


        // Recuperar el último valor mostrado desde SharedPreferences
        //SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);
        //String valorMostrado = sharedPreferences.getString("valor_semana", "");

        //if (!valorMostrado.isEmpty()) {
        //    txtSemana.setText(valorMostrado);
        //}

        if (getArguments() != null) {
            int gestanteId = getArguments().getInt("GEST_ID");
            int usuariaId = getArguments().getInt("USER_ID");
            Log.e("idididi", String.valueOf(gestanteId));
            Log.e("user", String.valueOf(usuariaId));
            consultarDatos(gestanteId);
        }
    return view;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            int gestanteId = getArguments().getInt("GEST_ID");
            Log.e("idididi", String.valueOf(gestanteId));
        }

    }

    private void consultarDatos(int usuaria_id) {
        // Consultar los datos de gestante para control datos mediante su id_usuaria
        final ApiService apiService = RetrofitClient.createService();
        final Call<GestanteConsultarResponse> call = apiService.consultarDatosGestante(usuaria_id);

        call.enqueue(new Callback<GestanteConsultarResponse>() {
            @Override
            public void onResponse(final Call<GestanteConsultarResponse> call, final Response<GestanteConsultarResponse> response) {
                if (response.code() == 200) {
                    if (response.body().isStatus()) {
                        // Cliente encontrado
                        Gestante gestante = response.body().getData();


                        int edadGestRegistro = gestante.getEdad_gestacional();
                        String fecha_registro = gestante.getFecha_registro();

                        // Formato de fecha esperado
                        SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-dd");

                        // Objeto Date para almacenar la fecha de registro
                        Date fechaRegistro = null;

                        try {
                            // Convertir el String a Date
                            fechaRegistro = formatoFecha.parse(fecha_registro);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        if (fechaRegistro != null) {
                            // Obtener el tiempo en milisegundos de la fecha de registro
                            long tiempoRegistroMillis = fechaRegistro.getTime();

                            // Obtener el tiempo en milisegundos de la fecha actual
                            long tiempoActualMillis = System.currentTimeMillis();

                            // Calcular la diferencia en milisegundos entre la fecha actual y la fecha de registro
                            long diferenciaMillis = tiempoActualMillis - tiempoRegistroMillis;

                            // Convertir la diferencia de milisegundos a semanas
                            int semanasTranscurridas = (int) (diferenciaMillis / (1000 * 60 * 60 * 24 * 7));

                            // Calcular las semanas de gestación actuales sumando las semanas en el momento del registro
                            int semanasDeGestacionActuales = semanasTranscurridas + edadGestRegistro;

                            // Mostrar en qué semana de gestación está la paciente actualmente
                            Log.e("SEMANAS DE GESTACIÓN", "La paciente está en la semana " + semanasDeGestacionActuales);
                            // Setear el número de semanas de gestación actuales en un TextView

                            // Guardar el número de semanas de gestación actuales en SharedPreferences
                            //SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);
                            //.Editor editor = sharedPreferences.edit();
                            //editor.putString("valor_semana", String.valueOf(semanasDeGestacionActuales));
                            //editor.apply();

                            txtSemana.setText(String.valueOf(semanasDeGestacionActuales));

                        } else {
                            // Manejar caso de conversión fallida
                            Log.e("ERROR", "No se pudo convertir la fecha de registro a Date.");
                        }

                    } else {
                        Helper.mensajeInformacion(BienvenidoFragment.this.getActivity(), "Verifique", "Gestante no existe");
                    }
                } else {
                    // status: 500,401,400
                    try {
                        Helper.mensajeError(BienvenidoFragment.this.getActivity(), "Error", response.errorBody().string());
                    } catch (final IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            @Override
            public void onFailure(final Call<GestanteConsultarResponse> call, final Throwable t) {
                Helper.mensajeInformacion(BienvenidoFragment.this.getActivity(), "No existen registros previos", "Ingrese sus datos en la opción 'Recordatorios' y actualice");
            }
        });
    }



}