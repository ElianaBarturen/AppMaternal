package com.example.appmaternal;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;

import androidx.compose.ui.text.android.TextLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.appmaternal.model.ConsultarCefalea;
import com.example.appmaternal.model.ConsultarContraciones;
import com.example.appmaternal.model.ConsultarDolorEpigastrio;
import com.example.appmaternal.model.ConsultarHichazonPies;
import com.example.appmaternal.model.ConsultarNauseas;
import com.example.appmaternal.model.ConsultarVisionBorrosa;
import com.example.appmaternal.model.Gestante;
import com.example.appmaternal.model.Prediction;
import com.example.appmaternal.response.GestanteConsultarResponse;
import com.example.appmaternal.response.GestanteDatosControlAgregarResponse;
import com.example.appmaternal.response.PredictionResponse;
import com.example.appmaternal.retrofit.ApiService;
import com.example.appmaternal.retrofit.RetrofitClient;
import com.example.appmaternal.util.Helper;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import javax.annotation.Nullable;

public class RecordatoriosFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener {
    int dolorEpigastrioId, visionBorrosaId, nuaseasId, cefaleaId, hinchazonPiesId, edadGestacional, tipo_embarazo_id, ant_personal_id, ant_familiar_id, gestas, edad_gestante, gestante_id = 0;
    TextInputEditText txtEdadGestacional, txtFrecCardiaca, txtPeso, txtSistolica, txtDiastolica;
    ImageView icPresionArterial;
    TextInputLayout input_layout_frec_cardiaca;
    private int usuariaId;

    public int dx;

    public Integer contador = 0;
    public MaterialButton btnAgregarDatos, btnActualizarDatos;
    AutoCompleteTextView autoCompleteTextViewDolorEpigastrio, autoCompleteTextViewVisionBorrosa, autoCompleteTextViewNauseas, autoCompleteTextViewCefalea, autoCompleteTextViewContracciones, autoCompleteTextViewHinchPies;
    List<ConsultarDolorEpigastrio> listaDolorEpigastrio = new ArrayList<>();
    List<ConsultarVisionBorrosa> listaVisionBorrosa = new ArrayList<>();
    List<ConsultarNauseas> listaNauseas = new ArrayList<>();
    List<ConsultarContraciones> listaContracciones = new ArrayList<>();
    List<ConsultarCefalea> listaCefalea = new ArrayList<>();
    List<ConsultarHichazonPies> listaHinchazonPies = new ArrayList<>();
    int semanasDeGestacionActualesm =0;
    VideoView videoView;

    public static ExtendedFloatingActionButton pred;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Recuperar los datos del Bundle
        if (getArguments() != null) {
            usuariaId = getArguments().getInt("USER_ID");
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recordatorios, container, false);
        videoView = view.findViewById(R.id.videoView);
        autoCompleteTextViewDolorEpigastrio = view.findViewById(R.id.autoCompleteTextViewDolorEpigastrio);
        autoCompleteTextViewDolorEpigastrio.setOnItemClickListener(this);
        autoCompleteTextViewDolorEpigastrio.setEnabled(false);

        autoCompleteTextViewVisionBorrosa = view.findViewById(R.id.autoCompleteTextViewVisionBorrosa);
        autoCompleteTextViewVisionBorrosa.setOnItemClickListener(this);
        autoCompleteTextViewVisionBorrosa.setEnabled(false);

        autoCompleteTextViewNauseas = view.findViewById(R.id.autoCompleteTextViewNauseas);
        autoCompleteTextViewNauseas.setOnItemClickListener(this);
        autoCompleteTextViewNauseas.setEnabled(false);

        autoCompleteTextViewCefalea = view.findViewById(R.id.autoCompleteTextViewCefalea);
        autoCompleteTextViewCefalea.setOnItemClickListener(this);
        autoCompleteTextViewCefalea.setEnabled(false);

        autoCompleteTextViewHinchPies = view.findViewById(R.id.autoCompleteTextViewHinchPies);
        autoCompleteTextViewHinchPies.setOnItemClickListener(this);
        autoCompleteTextViewHinchPies.setEnabled(false);

        txtDiastolica = view.findViewById(R.id.txtDiastolica);
        txtPeso = view.findViewById(R.id.txtPeso);
        txtSistolica = view.findViewById(R.id.txtSistolica);
        txtFrecCardiaca = view.findViewById(R.id.txtFrecCardiaca);
        txtEdadGestacional = view.findViewById(R.id.txtEdadGestacional);

        btnAgregarDatos = view.findViewById(R.id.btnAgregarDatos);
        btnAgregarDatos.setOnClickListener(this);
        btnActualizarDatos = view.findViewById(R.id.btnActualizarDatos);
        btnActualizarDatos.setOnClickListener(this);
        btnAgregarDatos.setVisibility(View.GONE);

        pred = view.findViewById(R.id.pred);
        pred.setOnClickListener(this);

        icPresionArterial =view.findViewById(R.id.icPresionArterial);
        dialogPresionArterial();

        input_layout_frec_cardiaca =view.findViewById(R.id.input_layout_frec_cardiaca);
        dialogFrecuenciaCardiaca();


        cargarDolorEpigastrio();
        cargarVisionBorrosa();
        cargarNauseas();
        cargarCefalea();
        cargarHinchazonPies();


        // Obtener el ID de usuario desde el Bundle
        Bundle args = getArguments();
        if (args != null) {
            int usuariaId = args.getInt("USER_ID", -1);
            if (usuariaId != -1) {
                consultarDatos(usuariaId);
            }
        }


        txtEdadGestacional = view.findViewById(R.id.txtEdadGestacional);
        txtEdadGestacional.setText(String.valueOf(edadGestacional));
        txtEdadGestacional.setEnabled(false);


        return view;
    }

    private void dialogFrecuenciaCardiaca() {
        input_layout_frec_cardiaca.setStartIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Inflar el diseño del diálogo
                LayoutInflater inflater = getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.activity_frecuencia_cardiaca_dialog, null);

                // Construir el AlertDialog usando el diseño inflado
                new AlertDialog.Builder(requireContext())
                        .setView(dialogView)
                        .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .create()
                        .show();
            }
        });
    }


    private void cargarDolorEpigastrio() {

        listaDolorEpigastrio.add(new ConsultarDolorEpigastrio(1, "SI"));
        listaDolorEpigastrio.add(new ConsultarDolorEpigastrio(0, "NO"));

        ArrayAdapter<ConsultarDolorEpigastrio> adapter = new ArrayAdapter<>(this.getActivity(), android.R.layout.simple_list_item_1, listaDolorEpigastrio);
        autoCompleteTextViewDolorEpigastrio.setAdapter(adapter);
    }

    private void cargarVisionBorrosa() {

        listaVisionBorrosa.add(new ConsultarVisionBorrosa(1, "SI"));
        listaVisionBorrosa.add(new ConsultarVisionBorrosa(0, "NO"));

        ArrayAdapter<ConsultarVisionBorrosa> adapter = new ArrayAdapter<>(this.getActivity(), android.R.layout.simple_list_item_1, listaVisionBorrosa);
        autoCompleteTextViewVisionBorrosa.setAdapter(adapter);
    }

    private void cargarNauseas() {

        listaNauseas.add(new ConsultarNauseas(1, "SI"));
        listaNauseas.add(new ConsultarNauseas(0, "NO"));

        ArrayAdapter<ConsultarNauseas> adapter = new ArrayAdapter<>(this.getActivity(), android.R.layout.simple_list_item_1, listaNauseas);
        autoCompleteTextViewNauseas.setAdapter(adapter);
    }

    private void cargarCefalea() {

        listaCefalea.add(new ConsultarCefalea(1, "SI"));
        listaCefalea.add(new ConsultarCefalea(0, "NO"));

        ArrayAdapter<ConsultarCefalea> adapter = new ArrayAdapter<>(this.getActivity(), android.R.layout.simple_list_item_1, listaCefalea);
        autoCompleteTextViewCefalea.setAdapter(adapter);
    }

    private void cargarHinchazonPies() {

        listaHinchazonPies.add(new ConsultarHichazonPies(1, "SI"));
        listaHinchazonPies.add(new ConsultarHichazonPies(0, "NO"));

        ArrayAdapter<ConsultarHichazonPies> adapter = new ArrayAdapter<>(this.getActivity(), android.R.layout.simple_list_item_1, listaHinchazonPies);
        autoCompleteTextViewHinchPies.setAdapter(adapter);
    }


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btnAgregarDatos) {
            registrar();
        } else if (view.getId() == R.id.btnActualizarDatos) {
            actualizar();
        } else if (view.getId() == R.id.pred) {

           Bundle datos = cargarDatosControl();
            // VerificaR si los datos no son nulos
            if (datos != null) {
                // Llamar a dialogoRealizarPrediccion y pasas los datos como argumento
                dialogoRealizarPrediccion(datos);
            }

        }

    }

    private void actualizar() {
    }
    private boolean validarDatos() {
        if (txtFrecCardiaca.getText().toString().isEmpty()) {
            Helper.mensajeInformacion(getActivity(), "Verifique", "Falta ingresar frecuencia cardiaca");
            //Toast.makeText(getActivity(), "Falta ingresar frecuencia cardiaca", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (txtPeso.getText().toString().isEmpty()) {
            Helper.mensajeInformacion(getActivity(), "Verifique", "Falta ingresar su peso");
            //Toast.makeText(getActivity(), "Falta ingresar su peso", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (txtDiastolica.getText().toString().isEmpty()) {
            Helper.mensajeInformacion(getActivity(), "Verifique", "Falta ingresar presión diastolica");
            //Toast.makeText(getActivity(), "Falta ingresar presión diastolica", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (txtSistolica.getText().toString().isEmpty()) {
            Helper.mensajeInformacion(getActivity(), "Verifique", "Falta ingresar persion sistolica");
            //Toast.makeText(getActivity(), "Falta ingresar persion sistolica", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (txtEdadGestacional.getText().toString().isEmpty()) {
            Helper.mensajeInformacion(getActivity(), "Verifique", "Falta ingresar su edad gestacional en semanas");
            //Toast.makeText(getActivity(), "Falta ingresar su edad gestacional en semanas", Toast.LENGTH_SHORT).show();
            return false;
        }if (autoCompleteTextViewDolorEpigastrio.getText().toString().isEmpty()) {
            Helper.mensajeInformacion(getActivity(), "Verifique", "Falta seleccionar si presenta dolor epigastrio");
            //Toast.makeText(getActivity(), "Falta seleccionar si presenta dolor epigastrio", Toast.LENGTH_SHORT).show();
            return false;
        }if (autoCompleteTextViewVisionBorrosa.getText().toString().isEmpty()) {
            Helper.mensajeInformacion(getActivity(), "Verifique", "Falta seleccionar si presenta visión borrosa");
            //Toast.makeText(getActivity(), "Falta seleccionar si presenta visión borrosa", Toast.LENGTH_SHORT).show();
            return false;
        }if (autoCompleteTextViewNauseas.getText().toString().isEmpty()) {
            Helper.mensajeInformacion(getActivity(), "Verifique", "Falta seleccionar si presenta nauseas");
            //Toast.makeText(getActivity(), "Falta seleccionar si presenta nauseas", Toast.LENGTH_SHORT).show();
            return false;
        }if (autoCompleteTextViewCefalea.getText().toString().isEmpty()) {
            Helper.mensajeInformacion(getActivity(), "Verifique", "Falta seleccionar si presenta cefalea");
            //Toast.makeText(getActivity(), "Falta seleccionar si presenta cefalea", Toast.LENGTH_SHORT).show();
            return false;
        }if (autoCompleteTextViewHinchPies.getText().toString().isEmpty()) {
            Helper.mensajeInformacion(getActivity(), "Verifique", "Falta seleccionar si presenta hinchazón de pies");
            //Toast.makeText(getActivity(), "Falta seleccionar si presenta hinchazón de pies", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void registrar() {
        if (validarDatos()) {
            Integer edad_gestacional = Integer.valueOf(txtEdadGestacional.getText().toString());
            Integer frec_cardiaca = Integer.valueOf(txtFrecCardiaca.getText().toString());
            Float peso = Float.valueOf(txtPeso.getText().toString());
            Integer pres_sistolica = Integer.valueOf((txtSistolica.getText().toString()));
            Integer pres_diastolica = Integer.valueOf((txtDiastolica.getText().toString()));

            final ApiService apiService = RetrofitClient.createService();
            final Call<GestanteDatosControlAgregarResponse> call = apiService.agregarDatosGestante(edad_gestante, edad_gestacional, peso, pres_sistolica, pres_diastolica, frec_cardiaca, tipo_embarazo_id, gestas, dolorEpigastrioId, visionBorrosaId, cefaleaId, nuaseasId, hinchazonPiesId, ant_personal_id, ant_familiar_id ,dx,gestante_id);
            call.enqueue(new Callback<GestanteDatosControlAgregarResponse>() {
                @Override
                public void onResponse(final Call<GestanteDatosControlAgregarResponse> call, final Response<GestanteDatosControlAgregarResponse> response) {
                    if (response.code() == 200) {
                        if (response.body().isStatus()) {
                            Helper.mensajeInformacion(getActivity(), "Mensaje", "Datos gestante registrados correctamente");

                            // Bloquear los campos
                            txtDiastolica.setEnabled(false);
                            txtSistolica.setEnabled(false);
                            txtEdadGestacional.setEnabled(false);
                            txtPeso.setEnabled(false);
                            txtFrecCardiaca.setEnabled(false);
                            autoCompleteTextViewDolorEpigastrio.setEnabled(false);
                            autoCompleteTextViewVisionBorrosa.setEnabled(false);
                            autoCompleteTextViewNauseas.setEnabled(false);
                            autoCompleteTextViewCefalea.setEnabled(false);

                            //btnActualizarDatos.setVisibility(View.VISIBLE);
                            btnAgregarDatos.setVisibility(View.GONE);


                        }
                    } else {
                        try {
                            Helper.mensajeError(getActivity(), "Error al registrar los datos de la gestante", response.errorBody().string());

                        } catch (final IOException e) {
                            throw new RuntimeException(e);
                        }

                    }
                }

                @Override
                public void onFailure(final Call<GestanteDatosControlAgregarResponse> call, final Throwable t) {
                    Log.e("Error al conectar con el servicio web de usuaria", t.getMessage());

                }
            });

        }
    }

    private void dialogPresionArterial() {
        icPresionArterial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Inflar el diseño
                LayoutInflater inflater = getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.activity_presion_arterial_dialog, null);

                // Construir el AlertDialog usando el diseño inflado
                new AlertDialog.Builder(requireContext())
                        .setView(dialogView)
                        .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .create()
                        .show();
            }
        });
    }

    /////////CARGAR DATOS//////
   private Bundle cargarDatosControl() {

        Bundle datos = null;
        if (validarDatos()) {
            Integer frecCardiaca = Integer.valueOf(txtFrecCardiaca.getText().toString());
            Integer sistolica = Integer.valueOf(txtSistolica.getText().toString());
            Integer diastólica =Integer.valueOf(txtDiastolica.getText().toString());
            Float peso = Float.valueOf(txtPeso.getText().toString());
            Integer edad_gestacional = Integer.valueOf((txtEdadGestacional.getText().toString()));


            // Crear el Bundle y añadir los datos
            datos = new Bundle();
            datos.putInt("frecCardiaca", frecCardiaca);
            datos.putInt("sistolica", sistolica);
            datos.putFloat("peso", peso);
            datos.putInt("edad_gestacional", edad_gestacional);
            datos.putInt("dolorEpigastrioId", dolorEpigastrioId);
            datos.putInt("tipo_embarazo_id", tipo_embarazo_id);
            datos.putInt("gestas", gestas);
            datos.putInt("ant_familiar_id", ant_familiar_id);
            datos.putInt("ant_personal_id", ant_personal_id);
            datos.putInt("edad_gestante", edad_gestante);
            datos.putInt("diastolica", diastólica);
            datos.putInt("visionBorrosaId", visionBorrosaId);
            datos.putInt("cefaleaId", cefaleaId);
            datos.putInt("nauseasId", nuaseasId);
            datos.putInt("hinchazonPiesId", hinchazonPiesId);

            // Pasar los datos al método para mostrar el diálogo
            dialogoRealizarPrediccion(datos);
        }
        return datos;
    }

    /*ESTE ES ANTERIOR AL FINAL_ANTES DE AJUSTES dialogoRealizarPrediccionAc*/
    private void dialogoRealizarPrediccionAc(Bundle datos) {

        // Obtener datos del Bundle
        int frecCardiaca = datos.getInt("frecCardiaca");
        int sistolica = datos.getInt("sistolica");
        float peso = datos.getFloat("peso");
        int edad_gestacional = datos.getInt("edad_gestacional");
        int visionBorrosaId = datos.getInt("visionBorrosaId");
        int nauseasId = datos.getInt("nauseasId");
        int cefaleaId = datos.getInt("cefaleaId");
        int hinchazonPiesId = datos.getInt("hinchazonPiesId");
        int tipo_embarazo_id = datos.getInt("tipo_embarazo_id");
        int gestas = datos.getInt("gestas");
        int ant_familiar_id = datos.getInt("ant_familiar_id");
        int ant_personal_id = datos.getInt("ant_personal_id");
        int edad_gestante = datos.getInt("edad_gestante");
        int diastolica = datos.getInt("diastolica");
        int dolorEpigastrioId = datos.getInt("dolorEpigastrioId");

        Log.e("PrediccionDialog", "frecCardiaca: " + frecCardiaca);
        Log.e("PrediccionDialog", "sistolica: " + sistolica);
        Log.e("PrediccionDialog", "peso: " + peso);
        Log.e("PrediccionDialog", "edad_gestacional: " + edad_gestacional);
        Log.e("PrediccionDialog", "visionBorrosaId: " + visionBorrosaId);
        Log.e("PrediccionDialog", "nauseasId: " + nauseasId);
        Log.e("PrediccionDialog", "cefalea: " + cefaleaId);
        Log.e("PrediccionDialog", "hinchazon_pies: " + hinchazonPiesId);
        Log.e("PrediccionDialog", "tipo_embarazo_id: " + tipo_embarazo_id);
        Log.e("PrediccionDialog", "gestas: " + gestas);
        Log.e("PrediccionDialog", "ant_familiar_id: " + ant_familiar_id);
        Log.e("PrediccionDialog", "ant_personal_id: " + ant_personal_id);
        Log.e("PrediccionDialog", "edad_gestante: " + edad_gestante);
        Log.e("PrediccionDialog", "diastolica: " + diastolica);
        Log.e("PrediccionDialog", "dolorEpigastrioId: " + dolorEpigastrioId);

        // Crear instancia de Prediction
        Prediction prediction = new Prediction(
                frecCardiaca, sistolica, diastolica, peso, edad_gestacional, visionBorrosaId,
                nauseasId, cefaleaId, hinchazonPiesId, tipo_embarazo_id, gestas,
                ant_familiar_id, ant_personal_id, edad_gestante, dolorEpigastrioId
        );

        // Obtener instancia de ApiService
        ApiService apiService = RetrofitClient.createService();

        // Llamar al método predict para enviar los datos de predicción al servidor
        Call<PredictionResponse> call = apiService.predict(prediction);
        call.enqueue(new Callback<PredictionResponse>() {
            @Override
            public void onResponse(Call<PredictionResponse> call, Response<PredictionResponse> response) {
                if (response.isSuccessful()) {
                    // Manejar la respuesta exitosa
                    PredictionResponse predictionResponse = response.body();
                    int prediction = predictionResponse.getPrediction();
                    // Mostrar la predicción en el diálogo
                    //String predictionMessage = "La predicción es: " + prediction;
                    if(prediction==0){
                        String predictionMessage = "No existe riesgo de Preeclampsia";
                        // Crear y mostrar un nuevo AlertDialog con el mensaje de predicción
                        AlertDialog.Builder resultDialogBuilder = new AlertDialog.Builder(requireContext());
                        resultDialogBuilder.setMessage(predictionMessage);
                        resultDialogBuilder.setCancelable(true);
                        resultDialogBuilder.setTitle("Resultado de la Predicción");
                        resultDialogBuilder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }

                        });
                        AlertDialog resultDialog = resultDialogBuilder.create();
                        resultDialog.show();
                    }else{

                        contador++; // Incrementar el contador si se detecta riesgo

                        String predictionMessage = "Existe riesgo de Preeclampsia, si los resultados persisten, consulte a su médico";
                        AlertDialog.Builder resultDialogBuilder = new AlertDialog.Builder(requireContext());
                        resultDialogBuilder.setMessage(predictionMessage);
                        resultDialogBuilder.setCancelable(true);
                        resultDialogBuilder.setTitle("Resultado de la Predicción");
                        resultDialogBuilder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        });
                        AlertDialog resultDialog = resultDialogBuilder.create();
                        resultDialog.show();
                    }
                    if (contador > 3) {
                        // Emitir un sonido de alerta
                        MediaPlayer mediaPlayer = MediaPlayer.create(requireContext(), R.raw.alarma_reporte); // Reemplaza "alert_sound" con tu archivo de sonido
                        mediaPlayer.start(); // Reproduce el sonido

                        // Mostrar un mensaje de alerta adicional
                        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(requireContext());
                        alertBuilder.setMessage("Se ha detectado un riesgo persistente de preeclampsia. Consulte a su médico de inmediato.");
                        alertBuilder.setCancelable(true);
                        alertBuilder.setTitle("Alerta de Riesgo");
                        alertBuilder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        });

                        // Mostrar el diálogo de alerta
                        alertBuilder.create().show();
                    }


                    Log.e("Diagnostico" , String.valueOf(prediction));
                    dx = prediction;
                    Log.e("dig", String.valueOf(dx));

                    btnAgregarDatos.setVisibility(View.VISIBLE);

                /*
                    // Crear y mostrar un nuevo AlertDialog con el mensaje de predicción
                    AlertDialog.Builder resultDialogBuilder = new AlertDialog.Builder(requireContext());
                    resultDialogBuilder.setMessage(predictionMessage);
                    resultDialogBuilder.setCancelable(true);
                    resultDialogBuilder.setTitle("Resultado de la Predicción");
                    resultDialogBuilder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                        }

                    });
                    AlertDialog resultDialog = resultDialogBuilder.create();
                    resultDialog.show();*/
                } else {
                    // Manejar error en la respuesta
                    Log.e("PredictionDialog", "Error en la solicitud: " + response.code());
                    try {
                        // Obtener el cuerpo de la respuesta de error
                        String errorBody = response.errorBody().string();
                        Log.e("PredictionDialog", "Error body: " + errorBody);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }



            @Override
            public void onFailure(Call<PredictionResponse> call, Throwable t) {
                // Manejar el fallo en la solicitud
                Log.e("PredictionDialog", "Error en la solicitud", t);
            }
        });
    }



    @Override
    public void onItemClick(AdapterView<?> parent, View view, int i, long id) {
        Bundle datos = new Bundle();
        if (parent.getAdapter() == autoCompleteTextViewDolorEpigastrio.getAdapter()) {
            dolorEpigastrioId = listaVisionBorrosa.get(i).getId();
            Log.e("dolorEpigastrioId ===>", String.valueOf(dolorEpigastrioId));
            datos.putInt("dolorEpigastrioId", dolorEpigastrioId);

        } else if (parent.getAdapter() == autoCompleteTextViewVisionBorrosa.getAdapter()) {
            visionBorrosaId = listaVisionBorrosa.get(i).getId();
            Log.e("visionBorrosaId ===>", String.valueOf(visionBorrosaId));
            datos.putInt("visionBorrosaId", visionBorrosaId);

        } else if (parent.getAdapter() == autoCompleteTextViewNauseas.getAdapter()) {
            nuaseasId = listaNauseas.get(i).getId();
            Log.e("nauseasId ===>", String.valueOf(nuaseasId));
            datos.putInt("nuaseasId", nuaseasId);

        } else if (parent.getAdapter() == autoCompleteTextViewCefalea.getAdapter()) {
            cefaleaId = listaCefalea.get(i).getId();
            Log.e("cefaleaId ===>", String.valueOf(cefaleaId));
            datos.putInt("cefaleaId", cefaleaId);

        } else if (parent.getAdapter() == autoCompleteTextViewHinchPies.getAdapter()) {
            hinchazonPiesId = listaHinchazonPies.get(i).getId();
            Log.e("hinchazonId ===>", String.valueOf(hinchazonPiesId));
            datos.putInt("hinchazonPiesId", hinchazonPiesId);
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
                        tipo_embarazo_id = Integer.parseInt(gestante.getTipo_embarazo_id());
                        gestas = gestante.getGestas();
                        ant_familiar_id = Integer.parseInt(gestante.getAnt_familiar_id());
                        ant_personal_id = Integer.parseInt(gestante.getAnt_personal_id());
                        edad_gestante = gestante.getEdad_gestante();
                        gestante_id = gestante.getId(); //ID GESTANTE PARA HISTORIAL

                        Log.e("antctctc", String.valueOf(tipo_embarazo_id));
                        Log.e("d", String.valueOf(gestas));
                        Log.e(" c", String.valueOf(ant_familiar_id));
                        Log.e("e", String.valueOf(ant_personal_id));
                        Log.e("edad", String.valueOf(edad_gestante));


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
                            txtEdadGestacional.setText(String.valueOf(semanasDeGestacionActuales));
                            Bundle bundle = new Bundle();
                            bundle.putInt("semanasDeGestacionActuales", semanasDeGestacionActuales);

                            if(semanasDeGestacionActuales ==0){
                                Helper.mensajeError(getActivity(), "ATENCIÓN", "Para hacer uso de esta patalla, debe tener registros previos en 'Mis datos' ");
                            }else{
                                txtEdadGestacional.setText(String.valueOf(semanasDeGestacionActuales));
                            }

                        } else {
                            // Manejar caso de conversión fallida
                            Log.e("ERROR", "No se pudo convertir la fecha de registro a Date.");
                        }

                    } else {
                        Helper.mensajeInformacion(RecordatoriosFragment.this.getActivity(), "Verifique", "Gestante no existe");
                    }
                } else {
                    // status: 500,401,400
                    try {
                        Helper.mensajeError(RecordatoriosFragment.this.getActivity(), "Error", response.errorBody().string());
                    } catch (final IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            @Override
            public void onFailure(final Call<GestanteConsultarResponse> call, final Throwable t) {
                Helper.mensajeError(RecordatoriosFragment.this.getActivity(), "No existen registros previos", "Por favor, registre datos en la opción 'Mis datos'");
               txtFrecCardiaca.setEnabled(false);
               txtDiastolica.setEnabled(false);
               txtSistolica.setEnabled(false);
               txtPeso.setEnabled(false);
               pred.setEnabled(false);
            }
        });
    }



        /***********FINAAAAAAAAAL**********/
    private void dialogoRealizarPrediccion(Bundle datos) {


// Obtener datos del Bundle
        int frecCardiaca = datos.getInt("frecCardiaca");
        int sistolica = datos.getInt("sistolica");
        float peso = datos.getFloat("peso");
        int edad_gestacional = datos.getInt("edad_gestacional");
        int visionBorrosaId = datos.getInt("visionBorrosaId");
        int nauseasId = datos.getInt("nauseasId");
        int cefaleaId = datos.getInt("cefaleaId");
        int hinchazonPiesId = datos.getInt("hinchazonPiesId");
        int tipo_embarazo_id = datos.getInt("tipo_embarazo_id");
        int gestas = datos.getInt("gestas");
        int ant_familiar_id = datos.getInt("ant_familiar_id");
        int ant_personal_id = datos.getInt("ant_personal_id");
        int edad_gestante = datos.getInt("edad_gestante");
        int diastolica = datos.getInt("diastolica");
        int dolorEpigastrioId = datos.getInt("dolorEpigastrioId");


    // Inicializar los pesos
        Map<String, Double> pesos = new HashMap<>();
        pesos.put("PRES_SISTOLICA_ALTA", 0.25);
        pesos.put("PRES_DIASTOLICA_ALTA", 0.25);
        pesos.put("CEFALEA", 0.1);
        pesos.put("HINCHAZON_PIES", 0.1);
        pesos.put("DOLOR_EPIGASTRIO", 0.1);
        pesos.put("VISION_BORROSA", 0.1);
        pesos.put("NAUSEAS", 0.1);
        pesos.put("ANT_FAMILIAR", 0.02);
        pesos.put("ANT_PERSONAL", 0.02);
        pesos.put("EDAD GEST. (sem)", 0.02);
        pesos.put("GESTAS", 0.02);
        pesos.put("TIPO EMBARAZO", 0.02);
        pesos.put("EDAD_GESTANTE", 0.02);
        pesos.put("FREC. CARD / min", 0.02);
        pesos.put("PESO (kg)", 0.02);


        // Definir un puntaje final usando AtomicReference
        AtomicReference<Double> puntaje = new AtomicReference<>(0.0);

        // Calcular el puntaje basado en las condiciones
        if (sistolica > 140) {
            puntaje.updateAndGet(v -> v + pesos.get("PRES_SISTOLICA_ALTA"));
        }
        if (diastolica > 90) {
            puntaje.updateAndGet(v -> v + pesos.get("PRES_DIASTOLICA_ALTA"));
        }
        if (frecCardiaca > 100) {
            puntaje.updateAndGet(v -> v + pesos.get("FREC. CARD / min"));
        }
        if (dolorEpigastrioId == 1) {
            puntaje.updateAndGet(v -> v + pesos.get("DOLOR_EPIGASTRIO"));
        }
        if (visionBorrosaId == 1) {
            puntaje.updateAndGet(v -> v + pesos.get("VISION_BORROSA"));
        }
        if (cefaleaId == 1) {
            puntaje.updateAndGet(v -> v + pesos.get("CEFALEA"));
        }
        if (nauseasId == 1) {
            puntaje.updateAndGet(v -> v + pesos.get("NAUSEAS"));
        }
        if (hinchazonPiesId == 1) {
            puntaje.updateAndGet(v -> v + pesos.get("HINCHAZON_PIES"));
        }
        if (edad_gestacional >20) {
            puntaje.updateAndGet(v -> v + pesos.get("EDAD GEST. (sem)"));
        }

    // Verificar el puntaje para determinar riesgo
        if (puntaje.get() >= 0.5) {
            // Existe riesgo de Preeclampsia
            Log.e("Diagnostico", "Existe riesgo de Preeclampsia");
            // Lógica para mostrar alerta o manejar riesgo
        } else {
            // No existe riesgo de Preeclampsia
            Log.e("Diagnostico", "No existe riesgo de Preeclampsia");
            // Lógica para mostrar alerta o manejar la no existencia de riesgo
        }


    // Log para verificar el puntaje calculado
        Log.d("PrediccionPuntaje", "Puntaje total: " + puntaje.get());

        int varPrediction;

        if(puntaje.get() >=0.5){
            varPrediction =1;
        }else{
            varPrediction =0;
        }

    // Crear instancia de Prediction
        Prediction prediction = new Prediction(
                frecCardiaca, sistolica, diastolica, peso, edad_gestacional, visionBorrosaId,
                nauseasId, cefaleaId, hinchazonPiesId, tipo_embarazo_id, gestas,
                ant_familiar_id, ant_personal_id, edad_gestante, dolorEpigastrioId
        );

    // Obtener instancia de ApiService
        ApiService apiService = RetrofitClient.createService();

    // Llamar al método predict para enviar los datos de predicción al servidor
        Call<PredictionResponse> call = apiService.predict(prediction);
        call.enqueue(new Callback<PredictionResponse>() {
            @Override
            public void onResponse(Call<PredictionResponse> call, Response<PredictionResponse> response) {
                if (response.isSuccessful()) {
                    // Manejar la respuesta exitosa
                    PredictionResponse predictionResponse = response.body();
                    int prediction = predictionResponse.getPrediction();

                    String predictionMessage;
                    if (varPrediction ==0) {
                        predictionMessage = "No existe riesgo de Preeclampsia";
                    } else {
                        predictionMessage = "Existe riesgo de Preeclampsia, si los resultados persisten, consulte a su médico \n" +
                                "            Vuelva a consultar en los próximos 10 minutos";


                        // Programa la notificación para que aparezca después de 10 minutos
                        WorkRequest notificationWork = new OneTimeWorkRequest.Builder(NotificationWorker.class)
                                .setInitialDelay(1, TimeUnit.MINUTES)
                                .build();

                        WorkManager.getInstance(getContext()).enqueue(notificationWork);

                        Log.e("mensaje confirmado","mensaje");
                    }

                    // Mostrar la predicción en un AlertDialog
                    AlertDialog.Builder resultDialogBuilder = new AlertDialog.Builder(requireContext());
                    resultDialogBuilder.setMessage(predictionMessage);
                    resultDialogBuilder.setCancelable(true);
                    resultDialogBuilder.setTitle("Resultado de la Predicción");
                    resultDialogBuilder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                        }

                    });

                    btnAgregarDatos.setVisibility(View.VISIBLE);
                    AlertDialog resultDialog = resultDialogBuilder.create();
                    resultDialog.show();

                } else {
                    // Manejar error en la respuesta
                    Log.e("PredictionDialog", "Error en la solicitud: " + response.code());
                    try {
                        // Obtener el cuerpo de la respuesta de error
                        String errorBody = response.errorBody().string();
                        Log.e("PredictionDialog", "Error body: " + errorBody);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<PredictionResponse> call, Throwable t) {
                // Manejar el fallo en la solicitud
                Log.e("PredictionDialog", "Error en la solicitud", t);
            }
        });

    }

    private void mostrarResultadoPrediccion(int prediction, double puntaje) {
        String predictionMessage;
        if (prediction == 0) {
            predictionMessage = "No existe riesgo de Preeclampsia";
        } else {
            predictionMessage = "Existe riesgo de Preeclampsia. Puntaje: " + puntaje;
            contador++;
        }

        AlertDialog.Builder resultDialogBuilder = new AlertDialog.Builder(requireContext());
        resultDialogBuilder.setMessage(predictionMessage);
        resultDialogBuilder.setCancelable(true);
        resultDialogBuilder.setTitle("Resultado de la Predicción");
        resultDialogBuilder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        resultDialogBuilder.create().show();

        if (contador > 3) {
            emitirAlertaRiesgo();
        }
    }

    private void emitirAlertaRiesgo() {
        MediaPlayer mediaPlayer = MediaPlayer.create(requireContext(), R.raw.alarma_reporte);
        mediaPlayer.start();

        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(requireContext());
        alertBuilder.setMessage("Se ha detectado un riesgo persistente de preeclampsia. Consulte a su médico de inmediato.");
        alertBuilder.setCancelable(true);
        alertBuilder.setTitle("Alerta de Riesgo");
        alertBuilder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        alertBuilder.create().show();


    }

    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_recordatorios, null);

        // Inicializar VideoView
        VideoView videoView = view.findViewById(R.id.videoView);

        // Ruta del video (asegúrate de que el archivo esté en res/raw)
        String videoPath = "android.resource://" + requireActivity().getPackageName() + "/" + R.raw.videoPulsera;
        videoView.setVideoURI(Uri.parse(videoPath));

        // Configurar MediaController
        MediaController mediaController = new MediaController(requireContext());
        mediaController.setAnchorView(videoView);
        videoView.setMediaController(mediaController);

        // Iniciar el video automáticamente
        videoView.start();

        // Botón para cerrar el diálogo
        //Button closeButton = view.findViewById(R.id.btnCloseDialog);
        //closeButton.setOnClickListener(v -> dismiss());


        builder.setView(view);
        return builder.create();
    }



}