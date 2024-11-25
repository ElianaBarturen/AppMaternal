package com.example.appmaternal;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appmaternal.model.DatosControlGestante;
import com.example.appmaternal.model.Resultado;
import com.example.appmaternal.model.Semanas;
import com.example.appmaternal.model.Sintomas;
import com.example.appmaternal.model.TipoEmbarazo;
import com.example.appmaternal.response.DatosControlConsultarResponse;
import com.example.appmaternal.response.DatosControlConsultarResponsePrueba;
import com.example.appmaternal.response.SemanasResponse;
import com.example.appmaternal.response.TipoEmbarazoListadoResponse;
import com.example.appmaternal.retrofit.ApiService;
import com.example.appmaternal.retrofit.RetrofitClient;
import com.example.appmaternal.util.Helper;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReportesFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener{

    MaterialButton btnReportes;
    AutoCompleteTextView autoCompleteTextViewSemanas;
    List<Resultado> listaSintomasGestante = new ArrayList<>();
    List<Semanas> listaSemanas = new ArrayList<>();
    ProgressBar progressBarCefalea,progressHinchazonPies,progressBarDolorEpigastrio,progressBarNauseas,progressBarVisionBorrosa,progressBarHinchazonPies;
    int numSemanagest =0;

    ImageView icCefalea,icDolorEpigastrio,icNauseas,icHinchazonPies,icVisionBorrosa,imageViewCirculoVerde,imageViewCirculoRojo;

    MaterialTextView txtCefaleaRojo,txtCefaleaVerde,txtDolorEpigastrioRojo,txtDolorEpigastrioVerde,
            txtNauseasRojo,txtNauseasVerde,txtHinchazonRojo,txtHinchazonVerde,txtVisionBorrosaRojo,txtVisionBorrosaVerde;




    public static ReportesFragment newInstance(String param1, String param2) {
        ReportesFragment fragment = new ReportesFragment();
        Bundle args = new Bundle();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            int gestanteId = getArguments().getInt("GEST_ID");
            Log.e("gestanteId/desde main actovoty hasta reportes", String.valueOf(gestanteId));
            //cargarSintomas(gestanteId);
            cargarSemanas(gestanteId);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_reportes, container, false);

        btnReportes = view.findViewById(R.id.btnReportes);
        btnReportes.setOnClickListener(this);


        autoCompleteTextViewSemanas = view.findViewById(R.id.autoCompleteTextViewSemanas);
        autoCompleteTextViewSemanas.setOnItemClickListener(this);

        progressBarCefalea = view.findViewById(R.id.progressBarCefalea);
        progressBarDolorEpigastrio = view.findViewById(R.id.progressBarDolorEpigastrio);
        progressHinchazonPies = view.findViewById(R.id.progressBarHinchazonPies);
        progressBarNauseas = view.findViewById(R.id.progressBarNauseas);
        progressBarVisionBorrosa = view.findViewById(R.id.progressBarVisionBorrosa);
        progressBarHinchazonPies = view.findViewById(R.id.progressBarHinchazonPies);


        txtCefaleaRojo=view.findViewById(R.id.txtCefaleaRojo);
        txtCefaleaVerde =view.findViewById(R.id.txtCefaleaVerde);
        txtDolorEpigastrioRojo =view.findViewById(R.id.txtDolorEpigastrioRojo);
        txtDolorEpigastrioVerde =view.findViewById(R.id.txtDolorEpigastrioVerde);
        txtNauseasRojo =view.findViewById(R.id.txtNauseasRojo);
        txtNauseasVerde =view.findViewById(R.id.txtNauseasVerde);
        txtHinchazonRojo =view.findViewById(R.id.txtHinchazonRojo);
        txtHinchazonVerde =view.findViewById(R.id.txtHinchazonVerde);
        txtVisionBorrosaRojo =view.findViewById(R.id.txtVisionBorrosaRojo);
        txtVisionBorrosaVerde =view.findViewById(R.id.txtVisionBorrosaVerde);

        icCefalea =view.findViewById(R.id.icCefalea);
        icDolorEpigastrio =view.findViewById(R.id.icDolorEpigastrio);
        icNauseas =view.findViewById(R.id.icNauseas);
        icHinchazonPies =view.findViewById(R.id.icHinchazonPies);
        icVisionBorrosa =view.findViewById(R.id.icVisionBorrosa);

        imageViewCirculoVerde =view.findViewById(R.id.imageViewCirculoVerde);
        imageViewCirculoRojo =view.findViewById(R.id.imageViewCirculoRojo);

        dialogHinchazonPies();
        dialogCefalea();
        dialogDolorEpigastrio();
        dialogNauseas();
        dialogVisionBorrosa();

        dialogCirculoRojo();
        dialogCirculoVerde();

        return view;
    }

    private void dialogCirculoVerde() {
        imageViewCirculoVerde.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Inflar el diseño personalizado
                LayoutInflater inflater = getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.activity_verde_dialog, null);

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

    private void dialogCirculoRojo() {
        imageViewCirculoRojo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Inflar el diseño personalizado
                LayoutInflater inflater = getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.activity_rojo_dialog, null);

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

    private void dialogVisionBorrosa() {
        icVisionBorrosa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Inflar el diseño personalizado
                LayoutInflater inflater = getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.activity_vision_borrosa_dialog, null);

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

    private void dialogNauseas() {
        icNauseas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Inflar el diseño personalizado
                LayoutInflater inflater = getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.activity_nauseas_dialog, null);

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

    private void dialogDolorEpigastrio() {
        icDolorEpigastrio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Inflar el diseño personalizado
                LayoutInflater inflater = getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.activity_dolor_epigastrio_dialog, null);

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

    private void dialogCefalea() {
        icCefalea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Inflar el diseño personalizado
                LayoutInflater inflater = getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.activity_cefalea_dialog, null);

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

    private void dialogHinchazonPies() {
        icHinchazonPies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Inflar el diseño personalizado
                LayoutInflater inflater = getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.activity_hinchazon_pies_dialog, null);

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


    private boolean validarDatos() {
        String semanas = autoCompleteTextViewSemanas.getText().toString();
        if (semanas.isEmpty()) {
            Toast.makeText(getActivity(), "Falta seleccionar semana de gestacion", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }


    @Override
    public void onClick(View view) {

        if (getArguments() != null) {
            int gestanteId = getArguments().getInt("GEST_ID");
            Log.e("gestanteId/desde main actovoty hasta reportes", String.valueOf(gestanteId));
            //cargarSintomas(gestanteId);
            if (view.getId() == R.id.btnReportes) {
                //reporteSintomas(gestanteId,10);
                validarDatos();
                reporte(gestanteId,numSemanagest);
                Toast.makeText(getActivity(), "Reporte de síntomas en la semana " + numSemanagest, Toast.LENGTH_SHORT).show();
            }
        }


    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int i, long id) {
        if (parent.getAdapter() == autoCompleteTextViewSemanas.getAdapter()) {
            // Si es autoCompleteTextViewAntPersonal, asignar el id correspondiente de antPersonal
            numSemanagest = listaSemanas.get(i).getEdadGestacionalSem();
            Log.e("NUM SEMANA SELECCIONADA ===>", String.valueOf(numSemanagest));
        }

    }

    private void reporte(int gestanteId, int edad_gestacional_sem) {
        ApiService apiService = RetrofitClient.createService();
        Call<DatosControlConsultarResponsePrueba> call = apiService.reporteSintomas(gestanteId,edad_gestacional_sem);
        call.enqueue((new Callback<DatosControlConsultarResponsePrueba>() {
            @Override
            public void onResponse(final Call<DatosControlConsultarResponsePrueba> call, final Response<DatosControlConsultarResponsePrueba> response) {
                if(response.code()==200){
                    boolean status = response.body().isStatus();
                    if(status){ //true
                        listaSintomasGestante.clear();
                        listaSintomasGestante.addAll(Arrays.asList(response.body().getData()));
                        Integer[] res = new Integer[listaSintomasGestante.size()];
                        Log.e("fg", Arrays.toString(res));

                        int conteoCefalea =0;
                        int conteoCefaleaNO=0;
                        int conteoDolorEpigastrio =0;
                        int conteoDolorEpigastrioNO=0;
                        int conteoNauseas =0;
                        int conteoNauseasNO=0;
                        int conteoVisionBorrosa =0;
                        int conteoVisionBorrosaNO=0;
                        int conteoHinchazonPies=0;
                        int conteoHinchazonPiesNO=0;
                        double porcCefaleaSI=0.0;
                        double porcCefaleaNO=0.0;
                        double porcDolorEpigastrioSI=0.0;
                        double porcDolorEpigastrioNO=0.0;
                        double porcNauseasSI=0.0;
                        double porcNauseasNO=0.0;
                        double porcVisionBorrosaNO=0.0;
                        double porcVisionBorrosaSI=0.0;
                        double porcHinchazonSI=0.0;
                        double porcHinchazonNO=0.0;

                        for (int i = 0; i < listaSintomasGestante.size(); i++) {
                            Resultado resultado = listaSintomasGestante.get(i);

                            if (resultado.getCEFALEA() == 1) {
                                //ROJO
                                conteoCefalea++;
                            }else {
                                //VERDE
                                conteoCefaleaNO++;
                            }

                            if (resultado.getDolor_epigastrio() == 1) {
                                conteoDolorEpigastrio++;
                            }else {
                                conteoDolorEpigastrioNO++;
                            }

                            if (resultado.getNauseas() == 1) {
                                conteoNauseas++;
                            }else{
                                conteoNauseasNO++;
                            }

                            if (resultado.getVision_borrosa() == 1) {
                                conteoVisionBorrosa++;
                            }else{
                                conteoVisionBorrosaNO++;
                            }

                            if (resultado.getHinchazon_pies() == 1) {
                                conteoHinchazonPies++;
                            }else{
                                conteoHinchazonPiesNO++;
                            }
                        }



                        // Imprimir mensajes después de las iteraciones
                        Log.e("Conteo1CefaleaSI", String.valueOf(conteoCefalea));
                        Log.e("cefaleaNO", String.valueOf(conteoCefaleaNO));
                        porcCefaleaSI = ((float)conteoCefalea / listaSintomasGestante.size()) * 100;
                        Log.e("porcCefaleaSI", String.valueOf(porcCefaleaSI));
                        porcCefaleaNO = ((float)conteoCefaleaNO / listaSintomasGestante.size()) * 100;
                        Log.e("porcCefaleaNO", String.valueOf(porcCefaleaNO));

                        Log.e("conteoDolorEpigSI", String.valueOf(conteoDolorEpigastrio));
                        Log.e("dolorepiNO", String.valueOf(conteoDolorEpigastrioNO));
                        porcDolorEpigastrioSI = ((float)conteoDolorEpigastrio / listaSintomasGestante.size()) * 100;
                        Log.e("porcDolorEpigastrioSI", String.valueOf(porcDolorEpigastrioSI));
                        porcDolorEpigastrioNO = ((float)conteoDolorEpigastrioNO / listaSintomasGestante.size()) * 100;
                        Log.e("porcDolorEpigastrioNO", String.valueOf(porcDolorEpigastrioNO));


                        Log.e("conteoNauseasSI", String.valueOf(conteoNauseas));
                        Log.e("nauseaNO", String.valueOf(conteoNauseasNO));
                        porcNauseasSI= ((float)conteoNauseas / listaSintomasGestante.size()) * 100;
                        Log.e("porconteoNauseasSI", String.valueOf(porcNauseasSI));
                        porcNauseasNO = ((float)conteoNauseasNO / listaSintomasGestante.size()) * 100;
                        Log.e("porconteoNauseasNO", String.valueOf(porcNauseasNO));

                        Log.e("conteoVisionBorrosaSI", String.valueOf(conteoVisionBorrosa));
                        Log.e("visionNO", String.valueOf(conteoVisionBorrosaNO));
                        porcVisionBorrosaSI= ((float)conteoVisionBorrosa / listaSintomasGestante.size()) * 100;
                        Log.e("porcVisionBorrosaSI", String.valueOf(porcVisionBorrosaSI));
                        porcVisionBorrosaNO = ((float)conteoVisionBorrosaNO / listaSintomasGestante.size()) * 100;
                        Log.e("porcVisionBorrosaNO", String.valueOf(porcVisionBorrosaNO));

                        Log.e("conteoHinchSI", String.valueOf(conteoHinchazonPies));
                        Log.e("hinchNO", String.valueOf(conteoHinchazonPiesNO));
                        porcHinchazonSI= ((float)conteoHinchazonPies / listaSintomasGestante.size()) * 100;
                        Log.e("porcHinchazonSI", String.valueOf(porcHinchazonSI));
                        porcHinchazonNO = ((float)conteoHinchazonPiesNO / listaSintomasGestante.size()) * 100;
                        Log.e("porcHinchazonNO", String.valueOf(porcHinchazonNO));

                        //listaSintomasGestante.size();
                        Log.e("TOTAL FILAS", String.valueOf(listaSintomasGestante.size()));

                        progressBarCefalea.setProgress((int) porcCefaleaSI);
                        progressBarDolorEpigastrio.setProgress((int) porcDolorEpigastrioSI);
                        progressHinchazonPies.setProgress((int) porcHinchazonSI);
                        progressBarNauseas.setProgress((int) porcNauseasSI);
                        progressBarVisionBorrosa.setProgress((int) porcVisionBorrosaSI);

                        txtCefaleaRojo.setText(String.valueOf(porcCefaleaSI) + "%");
                        Log.e("rojooo", String.valueOf(porcNauseasSI));
                        txtCefaleaVerde.setText(String.valueOf(porcCefaleaNO) +"%");
                        txtDolorEpigastrioRojo.setText(String.valueOf(porcDolorEpigastrioSI) +"%");
                        txtDolorEpigastrioVerde.setText(String.valueOf(porcDolorEpigastrioNO) +"%");
                        txtHinchazonRojo.setText(String.valueOf(porcHinchazonSI) +"%");
                        txtHinchazonVerde.setText(String.valueOf(porcHinchazonNO)+"%");
                        txtNauseasRojo.setText(String.valueOf(porcNauseasSI) +"%");
                        txtNauseasVerde.setText(String.valueOf(porcNauseasNO) +"%");
                        txtVisionBorrosaRojo.setText(String.valueOf(porcVisionBorrosaSI) +"%");
                        txtVisionBorrosaVerde.setText(String.valueOf(porcVisionBorrosaNO) +"%");


                    }
                    //int valor = 70; // Tu valor entre 0 y 100

                }else{
                    //status 500,401,400, etc
                    try {
                        Log.e("Error al ejecutar el servicio web de listar tipo embarazo",response.errorBody().string());
                    } catch (final IOException e) {
                        throw new RuntimeException(e);
                    }

                }
            }

            @Override
            public void onFailure(final Call<DatosControlConsultarResponsePrueba> call, final Throwable t) {
                Log.e("Error al ejecutar el servicio web de listar tipo embarazo",t.getMessage());
            }
        }));
    }

    private void cargarSemanas(int gestanteId) {
        ApiService apiService = RetrofitClient.createService();
        Call<SemanasResponse> call = apiService.listarSemanas(gestanteId);
        call.enqueue(new Callback<SemanasResponse>() {
            @Override
            public void onResponse(Call<SemanasResponse> call, Response<SemanasResponse> response) {
                if (response.code() == 200) {
                    boolean status = response.body().isStatus();
                    if (status) {
                        listaSemanas.clear();
                        listaSemanas = Arrays.asList(response.body().getData());
                        Integer[] numSemanas = new Integer[listaSemanas.size()];

                        for (int i = 0; i < listaSemanas.size(); i++) {
                            numSemanas[i] = listaSemanas.get(i).getEdadGestacionalSem();
                        }

                        ArrayAdapter<Integer> adapter = new ArrayAdapter<>(
                                ReportesFragment.this.getActivity(),
                                android.R.layout.simple_list_item_1,
                                numSemanas
                        );
                        Log.e("semanaaaa", Arrays.toString(numSemanas));


                        if (autoCompleteTextViewSemanas != null) {
                            autoCompleteTextViewSemanas.setAdapter(adapter);
                        }
                    }
                } else {
                    try {
                        Log.e("Error al ejecutar el servicio web de listar semanas", response.errorBody().string());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            @Override
            public void onFailure(Call<SemanasResponse> call, Throwable t) {
                Log.e("Error al ejecutar el servicio web de listar semanas", t.getMessage());
                Helper.mensajeError(getActivity(), "No existen registros previos", "Debe tener al menos un registro en la opción 'Recordatorios'");
                btnReportes.setEnabled(false);
            }
        });
    }


}