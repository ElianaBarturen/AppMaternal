package com.example.appmaternal;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.example.appmaternal.model.AntFamiliar;
import com.example.appmaternal.model.AntPersonal;
import com.example.appmaternal.model.DatosControlGestante;
import com.example.appmaternal.model.Gestante;
import com.example.appmaternal.model.Gestas;
import com.example.appmaternal.model.TipoEmbarazo;
import com.example.appmaternal.response.AntFamiliarListadoResponse;
import com.example.appmaternal.response.AntPersonalListadoResponse;
import com.example.appmaternal.response.GestanteActualizarResponse;
import com.example.appmaternal.response.GestanteAgregarResponse;
import com.example.appmaternal.response.GestanteConsultarResponse;
import com.example.appmaternal.response.TipoEmbarazoListadoResponse;
import com.example.appmaternal.retrofit.ApiService;
import com.example.appmaternal.retrofit.RetrofitClient;
import com.example.appmaternal.util.Helper;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GestanteAgregarFragment extends Fragment  implements View.OnClickListener, AdapterView.OnItemClickListener {

    TextInputEditText txtIdUsuaria,txtNombres,txtDni, txtEdad, txtEmail, txtEdadGestacional;
    MaterialButton btnAgregar, btnActualizarDatos,btnHabilitarbotones;
    AutoCompleteTextView autoCompleteTextViewTipoEmbarazo,autoCompleteTextViewNumGestas,autoCompleteTextViewAntFamiliar,autoCompleteTextViewAntPersonal;

    int antPersonalId =0;
    int antFamiliarId =0;
    int numGestas =0;
    int tipoEmbarazoId=0;
    List<Gestas> gestasList = new ArrayList<>();
    List<AntFamiliar> listaAntFamiliar = new ArrayList<>();
    List<AntPersonal> listaAntPersonal = new ArrayList<>();
    List<TipoEmbarazo> listaTipoEmbarazo = new ArrayList<>();

    private int usuariaId;
    private String nombre;
    private String email;
    int idGestante =0;
    private String antFamiliarActual,tipoEmbarazoActual,numGestasActual,antPersonalActual;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Recuperar los datos del Bundle
        if (getArguments() != null) {
            usuariaId = getArguments().getInt("USER_ID");
            nombre = getArguments().getString("USER_NAME");
            email = getArguments().getString("USER_EMAIL");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gestante_agregar, container, false);

        txtNombres= view.findViewById(R.id.txtNombres);
        txtIdUsuaria= view.findViewById(R.id.txtIdUsuaria);
        txtDni= view.findViewById(R.id.txtDni);
        txtEdad= view.findViewById(R.id.txtEdad);
        txtEmail = view.findViewById(R.id.txtEmail);
        txtEdadGestacional= view.findViewById(R.id.txtEdadGestacional);
        btnAgregar = view.findViewById(R.id.btnAgregar);
        btnAgregar.setOnClickListener(this);
        btnActualizarDatos = view.findViewById(R.id.btnActualizarDatos);
        btnActualizarDatos.setOnClickListener(this);
        btnHabilitarbotones = view.findViewById(R.id.btnHabilitarbotones);
        btnHabilitarbotones.setOnClickListener(this);

        // Asignar los datos a los TextView
        txtIdUsuaria.setText(String.valueOf(usuariaId));
        txtNombres.setText(nombre);
        txtEmail.setText(email);


        autoCompleteTextViewAntFamiliar = view.findViewById(R.id.autoCompleteTextViewAntFamiliar);
        autoCompleteTextViewAntFamiliar.setOnItemClickListener(this);

        autoCompleteTextViewAntPersonal = view.findViewById(R.id.autoCompleteTextViewAntPersonal);
        autoCompleteTextViewAntPersonal.setOnItemClickListener(this);

        autoCompleteTextViewTipoEmbarazo = view.findViewById(R.id.autoCompleteTextViewTipoEmbarazo);
        autoCompleteTextViewTipoEmbarazo.setOnItemClickListener(this);

        autoCompleteTextViewNumGestas = view.findViewById(R.id.autoCompleteTextViewNumGestas);
        autoCompleteTextViewNumGestas.setOnItemClickListener(this);

        cargarAntFamiliar();
        cargarAntPersonal();
        cargarTipoEmbarazo();
        cargarNumGestas();


        // Obtener el ID de usuario desde el Bundle
        Bundle args = getArguments();
        if (args != null) {
            int usuariaId = args.getInt("USER_ID", -1);
            if (usuariaId != -1) {
                consultarDatos(usuariaId);
            }
        }

        return view;
    }

    private void consultarDatos(int id) {
        // Consultar los datos de gestante mediante su id
        ApiService apiService = RetrofitClient.createService();
        Call<GestanteConsultarResponse> call = apiService.consultarGestante(id);

        call.enqueue(new Callback<GestanteConsultarResponse>() {
            @Override
            public void onResponse(final Call<GestanteConsultarResponse> call, final Response<GestanteConsultarResponse> response) {
                if(response.code() == 200){
                    if(response.body().isStatus()){
                        // Gestante encontrado
                        Gestante gestante = response.body().getData();
                        txtNombres.setText(gestante.getNombres());
                        txtEmail.setText(gestante.getEmail());
                        txtDni.setText(gestante.getDni());
                        txtEdad.setText(String.valueOf(gestante.getEdad_gestante())); // Convertir a String antes de establecerlo
                        txtEdadGestacional.setText(String.valueOf(gestante.getEdad_gestacional()));
                        autoCompleteTextViewAntFamiliar.setText(gestante.getAnt_familiar_id());
                        autoCompleteTextViewTipoEmbarazo.setText(gestante.getTipo_embarazo_id());
                        autoCompleteTextViewNumGestas.setText(String.valueOf(gestante.getGestas()));
                        autoCompleteTextViewAntPersonal.setText(gestante.getAnt_personal_id());
                        txtDni.setEnabled(false);
                        txtEdad.setEnabled(false);
                        txtEdadGestacional.setEnabled(false);
                        autoCompleteTextViewAntFamiliar.setEnabled(false);
                        autoCompleteTextViewTipoEmbarazo.setEnabled(false);
                        autoCompleteTextViewNumGestas.setEnabled(false);
                        autoCompleteTextViewAntPersonal.setEnabled(false);

                        antFamiliarActual = autoCompleteTextViewAntFamiliar.getText().toString();
                        tipoEmbarazoActual = autoCompleteTextViewTipoEmbarazo.getText().toString();
                        numGestasActual = autoCompleteTextViewNumGestas.getText().toString();
                        antPersonalActual = autoCompleteTextViewAntPersonal.getText().toString();

                        idGestante = gestante.getId();
                        Log.e("idgestanteagreg", String.valueOf(idGestante));

                        // Mostrar botón "Actualizar" y ocultar botón "Registrar"
                        btnActualizarDatos.setVisibility(View.GONE);
                        btnHabilitarbotones.setVisibility(View.VISIBLE);
                        btnAgregar.setVisibility(View.GONE);

                    } else {
                        Helper.mensajeInformacion(GestanteAgregarFragment.this.getActivity(), "Verifique", "Gestante no existe");
                    }
                } else {
                    // status: 500,401,400
                    try {
                        Helper.mensajeError(GestanteAgregarFragment.this.getActivity(), "Error", response.errorBody().string());
                    } catch (final IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            @Override
            public void onFailure(final Call<GestanteConsultarResponse> call, final Throwable t) {
                Helper.mensajeError(GestanteAgregarFragment.this.getActivity(), "No existen registros previos", "Por favor, registre sus datos generales");
            }
        });
    }



    private void cargarNumGestas() {
        gestasList.add(new Gestas(0));
        gestasList.add(new Gestas(1));
        gestasList.add(new Gestas(2));
        gestasList.add(new Gestas(3));
        gestasList.add(new Gestas(4));
        gestasList.add(new Gestas(5));
        gestasList.add(new Gestas(6));

        ArrayAdapter<Gestas> adapter = new ArrayAdapter<>(this.getActivity(), android.R.layout.simple_list_item_1, gestasList);
        autoCompleteTextViewNumGestas.setAdapter(adapter);
    }
    private void cargarAntFamiliar() {

        ApiService apiService = RetrofitClient.createService();
        Call<AntFamiliarListadoResponse> call = apiService.listarAntFamiliar();
        call.enqueue((new Callback<AntFamiliarListadoResponse>() {
            @Override
            public void onResponse(final Call<AntFamiliarListadoResponse> call, final Response<AntFamiliarListadoResponse> response) {
                if(response.code()==200){
                    boolean status = response.body().isStatus();
                    if(status){ //true
                        listaAntFamiliar.clear();
                        listaAntFamiliar.addAll(Arrays.asList(response.body().getData()));
                        String[] nombreAntFamiliar = new String[listaAntFamiliar.size()];
                        Log.e("Nombre ===>", Arrays.toString(nombreAntFamiliar));


                        for (AntFamiliar antFamiliar : listaAntFamiliar) {
                            nombreAntFamiliar[listaAntFamiliar.indexOf(antFamiliar)] = antFamiliar.getNombre();
                        }

                        final ArrayAdapter<String> adapter = new ArrayAdapter<>
                                (
                                        GestanteAgregarFragment.this.getActivity(),
                                        android.R.layout.simple_list_item_1,
                                        nombreAntFamiliar
                                ) ;
                        //autoCompleteTextViewAntFamiliar.setAdapter(adapter);
                        if (autoCompleteTextViewAntFamiliar != null) {
                            //Log.e("AAAAAAAAAAA ===>", String.valueOf(autoCompleteTextViewAntFamiliar));
                            autoCompleteTextViewAntFamiliar.setAdapter(adapter);
                        }

                    }
                }else{
                    //status 500,401,400, etc
                    try {
                        Log.e("Error al ejecutar el servicio web de listar antecedente familiar",response.errorBody().string());
                    } catch (final IOException e) {
                        throw new RuntimeException(e);
                    }

                }
            }

            @Override
            public void onFailure(final Call<AntFamiliarListadoResponse> call, final Throwable t) {
                Log.e("Error al ejecutar el servicio web de listar antecedente familiar",t.getMessage());
            }
        }));
    }
    private void cargarAntPersonal() {
        ApiService apiService = RetrofitClient.createService();
        Call<AntPersonalListadoResponse> call = apiService.listarAntPersonal();
        call.enqueue((new Callback<AntPersonalListadoResponse>() {
            @Override
            public void onResponse(final Call<AntPersonalListadoResponse> call, final Response<AntPersonalListadoResponse> response) {
                if(response.code()==200){
                    boolean status = response.body().isStatus();
                    if(status){ //true
                        listaAntPersonal.clear();
                        listaAntPersonal.addAll(Arrays.asList(response.body().getData()));
                        String[] nombreAntPersonal = new String[listaAntPersonal.size()];
                        for (AntPersonal antPersonal : listaAntPersonal) {
                            nombreAntPersonal[listaAntPersonal.indexOf(antPersonal)] = antPersonal.getNombre();

                        }
                        final ArrayAdapter<String> adapter = new ArrayAdapter<>
                                (
                                        GestanteAgregarFragment.this.getActivity(),
                                        android.R.layout.simple_list_item_1,
                                        nombreAntPersonal
                                ) ;
                        //autoCompleteTextViewAntPersonal.setAdapter(adapter);
                        if (autoCompleteTextViewAntPersonal != null) {
                            autoCompleteTextViewAntPersonal.setAdapter(adapter);
                        }

                    }
                }else{
                    //status 500,401,400, etc
                    try {
                        Log.e("Error al ejecutar el servicio web de listar antecedente personal",response.errorBody().string());
                    } catch (final IOException e) {
                        throw new RuntimeException(e);
                    }

                }
            }

            @Override
            public void onFailure(final Call<AntPersonalListadoResponse> call, final Throwable t) {
                Log.e("Error al ejecutar el servicio web de listar antecedente personal",t.getMessage());
            }
        }));
    }

    private void cargarTipoEmbarazo() {
        ApiService apiService = RetrofitClient.createService();
        Call<TipoEmbarazoListadoResponse> call = apiService.listarTipoEmbarazo();
        call.enqueue((new Callback<TipoEmbarazoListadoResponse>() {
            @Override
            public void onResponse(final Call<TipoEmbarazoListadoResponse> call, final Response<TipoEmbarazoListadoResponse> response) {
                if(response.code()==200){
                    boolean status = response.body().isStatus();
                    if(status){ //true
                        listaTipoEmbarazo.clear();
                        listaTipoEmbarazo.addAll(Arrays.asList(response.body().getData()));
                        String[] nombreTipoEmbarazo = new String[listaTipoEmbarazo.size()];
                        for (TipoEmbarazo tipoEmbarazo: listaTipoEmbarazo) {
                            nombreTipoEmbarazo[listaTipoEmbarazo.indexOf(tipoEmbarazo)] = tipoEmbarazo.getNombre();

                        }
                        final ArrayAdapter<String> adapter = new ArrayAdapter<>
                                (
                                        GestanteAgregarFragment.this.getActivity(),
                                        android.R.layout.simple_list_item_1,
                                        nombreTipoEmbarazo
                                ) ;
                        //autoCompleteTextViewAntPersonal.setAdapter(adapter);
                        if (autoCompleteTextViewTipoEmbarazo != null) {
                            autoCompleteTextViewTipoEmbarazo.setAdapter(adapter);
                        }

                    }
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
            public void onFailure(final Call<TipoEmbarazoListadoResponse> call, final Throwable t) {
                Log.e("Error al ejecutar el servicio web de listar tipo embarazo",t.getMessage());
            }
        }));
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btnAgregar) {
            registrar();
        }else if(view.getId() == R.id.btnHabilitarbotones){
            habilitarbotones();
            Log.e("habi","dfgvb") ;

            btnHabilitarbotones.setVisibility(View.GONE);
            btnActualizarDatos.setVisibility(View.VISIBLE);
        }else{
            actualizar();
            Log.e("dfgsdfgvh","dfgvb") ;

        }
    }
    private void habilitarbotones() {

        Gestante datos = new Gestante();

        consultarActualizar(idGestante);
        Log.e("antFamact",antFamiliarActual);
        //consultarActualizar(idGestante);
        Log.e("abababa", String.valueOf(idGestante));
        //txtDni.setText("");
        txtDni.setEnabled(true);
        //txtEdad.setText("");
        txtEdad.setEnabled(true);
        //txtNombres.setText("");
        txtNombres.setEnabled(true);
        //txtEdadGestacional.setText("");
        txtEdadGestacional.setEnabled(true);


        autoCompleteTextViewAntFamiliar.setEnabled(true);
        cargarAntFamiliar();
        autoCompleteTextViewTipoEmbarazo.setEnabled(true);
        cargarTipoEmbarazo();
        autoCompleteTextViewNumGestas.setEnabled(true);
        cargarNumGestas();
        autoCompleteTextViewAntPersonal.setEnabled(true);
        cargarAntPersonal();


    }

    private void consultarActualizar(int id) {
        // Consultar los datos de gestante mediante su id
        ApiService apiService = RetrofitClient.createService();
        Call<GestanteConsultarResponse> call = apiService.consultarActalizarGestante(id);

        call.enqueue(new Callback<GestanteConsultarResponse>() {
            @Override
            public void onResponse(final Call<GestanteConsultarResponse> call, final Response<GestanteConsultarResponse> response) {
                if(response.code() == 200){
                    if(response.body().isStatus()){
                        // Gestante encontrado
                        Gestante gestante = response.body().getData();
                        antFamiliarActual = gestante.getAnt_familiar_id();
                        tipoEmbarazoActual = gestante.getTipo_embarazo_id();
                        numGestasActual = String.valueOf(gestante.getGestas());
                        antPersonalActual = gestante.getAnt_personal_id();



                    } else {
                        Helper.mensajeInformacion(GestanteAgregarFragment.this.getActivity(), "Verifique", "Gestante no existe");
                    }
                } else {
                    // status: 500,401,400
                    try {
                        Helper.mensajeError(GestanteAgregarFragment.this.getActivity(), "Error", response.errorBody().string());
                    } catch (final IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            @Override
            public void onFailure(final Call<GestanteConsultarResponse> call, final Throwable t) {
                Helper.mensajeError(GestanteAgregarFragment.this.getActivity(), "No existen registros previos", "Por favor, registre sus datos generales");
            }
        });
    }

    /*private void habilitarbotones() {
        // Guardar los valores actuales de los campos
        String antFamiliarActual = autoCompleteTextViewAntFamiliar.getText().toString();
        String tipoEmbarazoActual = autoCompleteTextViewTipoEmbarazo.getText().toString();
        String numGestasActual = autoCompleteTextViewNumGestas.getText().toString();
        String antPersonalActual = autoCompleteTextViewAntPersonal.getText().toString();

        Log.e("antttttt", antFamiliarActual);

        consultarDatos(idGestante);

        // Habilitar los campos de texto
        txtDni.setEnabled(true);
        txtEdad.setEnabled(true);
        txtNombres.setEnabled(true);
        txtEdadGestacional.setEnabled(true);

        // Habilitar y cargar AutoCompleteTextViews
        autoCompleteTextViewAntFamiliar.setEnabled(true);
        cargarAntFamiliar();
        autoCompleteTextViewAntFamiliar.setText(antFamiliarActual); // Restaurar valor

        autoCompleteTextViewTipoEmbarazo.setEnabled(true);
        cargarTipoEmbarazo();
        autoCompleteTextViewTipoEmbarazo.setText(tipoEmbarazoActual); // Restaurar valor

        autoCompleteTextViewNumGestas.setEnabled(true);
        cargarNumGestas();
        autoCompleteTextViewNumGestas.setText(numGestasActual); // Restaurar valor

        autoCompleteTextViewAntPersonal.setEnabled(true);
        cargarAntPersonal();
        autoCompleteTextViewAntPersonal.setText(antPersonalActual); // Restaurar valor
    }*/

    private boolean validarDatos() {

        if (txtNombres.getText().toString().isEmpty()) {
            Helper.mensajeInformacion(getActivity(), "Verifique", "Falta ingresar nombres");
            //Toast.makeText(getActivity(), "Falta ingresar DNI", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (txtDni.getText().toString().isEmpty()) {
            Helper.mensajeInformacion(getActivity(), "Verifique", "Falta ingresar DNI");
            //Toast.makeText(getActivity(), "Falta ingresar DNI", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (txtDni.getText().toString().length() != 8) {
            Helper.mensajeInformacion(getActivity(), "Verifique", "Debe ingresar un DNI de 8 dígitos");
            return false;
        }
        if (txtEdad.getText().toString().isEmpty()) {
            Helper.mensajeInformacion(getActivity(), "Verifique", "Falta ingresar su edad");
            return false;
        }else{
            int edadgestante = Integer.parseInt(String.valueOf(txtEdad.getText()));
            if (edadgestante <14 || edadgestante > 50) {
                Helper.mensajeInformacion(getActivity(), "Lo sentimos" , "Sólo se admiten edades entre 14 y 50 años ");
                return false;
            }
        }
        if (txtEdadGestacional.getText().toString().isEmpty()) {
            Helper.mensajeInformacion(getActivity(), "Verifique", "Falta ingresar su edad gestacional en semanas");
            //Toast.makeText(getActivity(), "Falta ingresar su edad gestacional en semanas", Toast.LENGTH_SHORT).show();
            return false;
        }if (txtEdadGestacional.getText().toString().length() ==0) {
            Helper.mensajeInformacion(getActivity(), "Verifique", "Ingrese una eda gestacional correcta diferente a 0");
            return false;
        }if (autoCompleteTextViewNumGestas.getText().toString().length() ==0) {
            Helper.mensajeInformacion(getActivity(), "Verifique", "Seleccione gestas ");
            return false;
        }
        if (autoCompleteTextViewAntFamiliar.getText().toString().length() ==0) {
            Helper.mensajeInformacion(getActivity(), "Verifique", "seleccione ant familiar");
            return false;
        }
        if (autoCompleteTextViewTipoEmbarazo.getText().toString().length() ==0) {
            Helper.mensajeInformacion(getActivity(), "Verifique", "selecciones tipo embarazo");
            return false;
        }
        if (autoCompleteTextViewAntPersonal.getText().toString().length() ==0) {
            Helper.mensajeInformacion(getActivity(), "Verifique", "selecciones ant personal");
            return false;
        }

        else{
            int edadGestacional = Integer.parseInt(String.valueOf(txtEdadGestacional.getText()));

            // Validar que esté dentro del rango permitido
            if (edadGestacional <= 0 || edadGestacional >= 40) {
                Helper.mensajeInformacion(getActivity(), "Verifique", "La edad gestacional debe ser mayor a 0 y menor a 40 semanas");
               // Toast.makeText(getActivity(), "La edad gestacional debe ser mayor a 0 y menor a 40 semanas", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return true;
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int i, long id) {

        // Verificar qué AutoCompleteTextView ha sido seleccionado
        if (parent.getAdapter() == autoCompleteTextViewAntPersonal.getAdapter()) {
            // Si es autoCompleteTextViewAntPersonal, asignar el id correspondiente de antPersonal
            antPersonalId = listaAntPersonal.get(i).getId();
            Log.e("antPersonalId ===>", String.valueOf(antPersonalId));
        } else if (parent.getAdapter() == autoCompleteTextViewAntFamiliar.getAdapter()) {
            // Si es autoCompleteTextViewAntFamiliar, asignar el id correspondiente de antFamiliar
            antFamiliarId = listaAntFamiliar.get(i).getId();
            Log.e("antFamiliarId ===>", String.valueOf(antFamiliarId));
        } else if (parent.getAdapter() == autoCompleteTextViewTipoEmbarazo.getAdapter()) {
            // Si es autoCompleteTextViewTipoEmbarazo, asignar el id correspondiente de tipoEmbarazo
            tipoEmbarazoId = listaTipoEmbarazo.get(i).getId();
            Log.e("tipoEmbarazo ===>", String.valueOf(tipoEmbarazoId));
        } else if (parent.getAdapter() == autoCompleteTextViewNumGestas.getAdapter()) {
            // Si es autoCompleteTextViewNumGestas, asignar el número correspondiente de gestas
            numGestas = gestasList.get(i).getnumGestas();
            Log.e("numGestas ===>", String.valueOf(numGestas));
        } else {
            Log.e("Error:", "No se pudo determinar el AutoCompleteTextView seleccionado.");
        }
    }

   private void registrar() {
        if(validarDatos()){
            String email = txtEmail.getText().toString();
            String dni = txtDni.getText().toString();
            String nombres = txtNombres.getText().toString();
            Integer edad_gestante = Integer.valueOf((txtEdad.getText().toString()));
            Integer edad_gestacional = Integer.valueOf((txtEdadGestacional.getText().toString()));

            ApiService apiService = RetrofitClient.createService();
            Call<GestanteAgregarResponse> call = apiService.agregarGestante(nombres,dni,edad_gestante,email,antFamiliarId,antPersonalId,numGestas,tipoEmbarazoId,edad_gestacional,usuariaId);
            call.enqueue(new Callback<GestanteAgregarResponse>() {
                @Override
                public void onResponse(final Call<GestanteAgregarResponse> call, final Response<GestanteAgregarResponse> response) {
                    if(response.code()==200){
                        Log.e("hooaoaoa", String.valueOf(numGestas));
                        if(response.body().isStatus()){
                            Helper.mensajeInformacion(getActivity(), "Mensaje","Datos gestante registrados correctamente");

                            // Bloquear los campos
                            txtEmail.setEnabled(false);
                            txtDni.setEnabled(false);
                            txtNombres.setEnabled(false);
                            txtEdad.setEnabled(false);
                            txtEdadGestacional.setEnabled(false);
                            autoCompleteTextViewAntFamiliar.setEnabled(false);
                            autoCompleteTextViewAntPersonal.setEnabled(false);
                            autoCompleteTextViewTipoEmbarazo.setEnabled(false);
                            autoCompleteTextViewNumGestas.setEnabled(false);
                            btnAgregar.setVisibility(View.GONE);
                            btnActualizarDatos.setVisibility(View.GONE);
                            //btnActualizar.setVisibility(View.VISIBLE);
                            btnHabilitarbotones.setVisibility(View.VISIBLE);
                        }
                    }else{
                        try {
                            Helper.mensajeError(getActivity(), "Error al registrar los datos de la gestante", response.errorBody().string());

                        } catch (final IOException e) {
                            throw new RuntimeException(e);
                        }

                    }
                }

                @Override
                public void onFailure(final Call<GestanteAgregarResponse> call, final Throwable t) {
                    Log.e("Error al conectar con el servicio web de gestante para registro", t.getMessage());

                }
            });

        }
    }

   private void actualizar() {

           if(validarDatos()) {
               String email = txtEmail.getText().toString();
               String dni = txtDni.getText().toString();
               String nombres = txtNombres.getText().toString();
               Integer edad_gestante = Integer.valueOf((txtEdad.getText().toString()));
               Integer edad_gestacional = Integer.valueOf((txtEdadGestacional.getText().toString()));

               //Gestante datos = new Gestante();
               //Integer antActF= Integer.valueOf(datos.getAnt_familiar_id());
               //Integer antActP= Integer.valueOf(datos.getAnt_personal_id());
               //Integer numG= Integer.valueOf(datos.getGestas());
               //Integer tipoEmb= Integer.valueOf(datos.getTipo_embarazo_id());

               ApiService apiService = RetrofitClient.createService();
               Call<GestanteActualizarResponse> call = apiService.actualizarGestante(nombres, dni, edad_gestante, email, antFamiliarId, antPersonalId, numGestas, tipoEmbarazoId, edad_gestacional, idGestante);
               call.enqueue(new Callback<GestanteActualizarResponse>() {
                   @Override
                   public void onResponse(final Call<GestanteActualizarResponse> call, final Response<GestanteActualizarResponse> response) {
                       if (response.code() == 200) {
                           if (response.body().isStatus()) {
                               Helper.mensajeInformacion(getActivity(), "Mensaje", "Gestante actualizado correctamente");
                               final NavController navController = Navigation.findNavController(GestanteAgregarFragment.this.getView());
                               navController.navigateUp();
                           }
                       } else {
                           try {
                               Log.e("Error al actualizar al gestante", response.errorBody().string());
                               Helper.mensajeError(getActivity(), "Error...", response.errorBody().string());
                           } catch (final IOException e) {
                               throw new RuntimeException(e);
                           }

                       }
                   }

                   @Override
                   public void onFailure(final Call<GestanteActualizarResponse> call, final Throwable t) {
                       Helper.mensajeError(GestanteAgregarFragment.this.getActivity(), "Error al conectar con el servicio web", t.getMessage());
                   }
               });

           }




    }


}
