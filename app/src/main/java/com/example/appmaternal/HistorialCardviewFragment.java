package com.example.appmaternal;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;

import com.example.appmaternal.adapter.HistorialGestanteAdapter;
import com.example.appmaternal.model.DatosControlGestante;
import com.example.appmaternal.model.Gestante;
import com.example.appmaternal.response.GestanteConsultarResponse;
import com.example.appmaternal.response.GestanteDatosControlAgregarResponse;
import com.example.appmaternal.response.HistorialGestanteResponse;
import com.example.appmaternal.retrofit.ApiService;
import com.example.appmaternal.retrofit.RetrofitClient;
import com.example.appmaternal.util.Helper;
import com.google.android.material.button.MaterialButton;

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

public class HistorialCardviewFragment extends Fragment implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener, AdapterView.OnItemClickListener{
    List<DatosControlGestante> listadatosControlGestante = new ArrayList<>();
    RecyclerView recyclerViewControlGestante;
    SwipeRefreshLayout swipeRefreshLayoutControles;
    HistorialGestanteAdapter historialGestanteAdapter;
    AutoCompleteTextView txtFechaInicial, txtFechaFin;
    MaterialButton btnFiltrar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Recuperar los datos del Bundle
        if (getArguments() != null) {
            int gestanteId = getArguments().getInt("GEST_ID");
            Log.e("gestanteId/desde main actovoty hasta historial", String.valueOf(gestanteId));
            listar(gestanteId);
        }
       // Cambiar gestante_id a gestanteId

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_historial_cardview, container, false);

        // Configurar el RecyclerView
        recyclerViewControlGestante = view.findViewById(R.id.recyclerViewControlGestante);
        recyclerViewControlGestante.setLayoutManager(new LinearLayoutManager(getContext()));
        historialGestanteAdapter = new HistorialGestanteAdapter(listadatosControlGestante);
        recyclerViewControlGestante.setAdapter(historialGestanteAdapter);

        // Configurar el SwipeRefreshLayout
        swipeRefreshLayoutControles = view.findViewById(R.id.swipeRefreshLayoutControles);
        swipeRefreshLayoutControles.setOnRefreshListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {

    }





    private void listar(int gestanteId) { // Cambiar el nombre de la variable a gestanteId
        // Crear una instancia de ApiService para hacer una solicitud al servicio web
        ApiService apiService = RetrofitClient.createService();
        Call<HistorialGestanteResponse> call = apiService.listarHistorial(gestanteId);
        call.enqueue(new Callback<HistorialGestanteResponse>() {
            @Override
            public void onResponse(final Call<HistorialGestanteResponse> call, final Response<HistorialGestanteResponse> response) {
                if (response.code() == 200) {
                    final boolean status = response.body().isStatus();
                    if (status) { // true
                        listadatosControlGestante.clear();
                        listadatosControlGestante.addAll(Arrays.asList(response.body().getData()));
                        // Indicar que hay cambios en los registros para actualizar
                        historialGestanteAdapter.notifyDataSetChanged();
                    }
                } else {
                    // HTTP: 500, 400, 401, otros estados de error
                    Log.e("Error al acceder al servicio web", response.message());
                }
            }

            @Override
            public void onFailure(final Call<HistorialGestanteResponse> call, final Throwable t) {
                Log.e("Falla al conectarse al servicio web", t.getMessage());
                Helper.mensajeError(getActivity(), "No existen registros previos", "Debe tener al menos un registro en la opción 'Recordatorios'");

            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onRefresh() {
        ///llamar al listar falta acaaaaaa
        if (getArguments() != null) {
            int gestanteId = getArguments().getInt("GEST_ID");
            Log.e("gestanteId/desde main actovoty hasta historial", String.valueOf(gestanteId));
            listar(gestanteId);
        }
        swipeRefreshLayoutControles.setRefreshing(false);
    }
}