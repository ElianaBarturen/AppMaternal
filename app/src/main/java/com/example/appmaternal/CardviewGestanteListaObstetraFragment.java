package com.example.appmaternal;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appmaternal.adapter.HistorialGestanteAdapter;
import com.example.appmaternal.adapter.ListaGestanteObstetraAdapter;
import com.example.appmaternal.model.DatosControlGestante;
import com.example.appmaternal.model.ListaGestanteObstetra;
import com.example.appmaternal.response.HistorialGestanteResponse;
import com.example.appmaternal.response.ListaGestanteObstetraResponse;
import com.example.appmaternal.retrofit.ApiService;
import com.example.appmaternal.retrofit.RetrofitClient;
import com.example.appmaternal.util.Helper;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CardviewGestanteListaObstetraFragment extends Fragment  implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener, AdapterView.OnItemClickListener{

    List<ListaGestanteObstetra> listaGestanteObstetra = new ArrayList<>();
    ListaGestanteObstetraAdapter listaGestanteAdapter;
    SwipeRefreshLayout swipeRefreshLayoutControlesGestanteObs;

    RecyclerView recyclerViewListaGestanteObstetra;
    TextInputEditText txtDni;
    MaterialButton btnFiltrarDni;

    String dni="";


    public static CardviewGestanteListaObstetraFragment newInstance(String param1, String param2) {
        CardviewGestanteListaObstetraFragment fragment = new CardviewGestanteListaObstetraFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        listarGestanteObs();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_cardview_gestante_lista_obstetra, container, false);
        // Configurar el RecyclerView
        recyclerViewListaGestanteObstetra = view.findViewById(R.id.recyclerViewListaGestanteObstetra);
        recyclerViewListaGestanteObstetra.setLayoutManager(new LinearLayoutManager(getContext()));
        listaGestanteAdapter = new ListaGestanteObstetraAdapter(listaGestanteObstetra);
        recyclerViewListaGestanteObstetra.setAdapter(listaGestanteAdapter);

        txtDni= view.findViewById(R.id.txtDni);
        btnFiltrarDni= view.findViewById(R.id.btnFiltrarDni);
        btnFiltrarDni.setOnClickListener(this);

        // Configurar el SwipeRefreshLayout
        swipeRefreshLayoutControlesGestanteObs = view.findViewById(R.id.swipeRefreshLayoutControlesGestanteObs);
        swipeRefreshLayoutControlesGestanteObs.setOnRefreshListener(this);

        return view;
    }

    private void listarGestanteObs() { // Cambiar el nombre de la variable a gestanteId
        // Crear una instancia de ApiService para hacer una solicitud al servicio web
        ApiService apiService = RetrofitClient.createService();
        Call<ListaGestanteObstetraResponse> call = apiService.listarGestanteObstetra();
        call.enqueue(new Callback<ListaGestanteObstetraResponse>() {
            @Override
            public void onResponse(final Call<ListaGestanteObstetraResponse> call, final Response<ListaGestanteObstetraResponse> response) {
                if (response.code() == 200) {
                    final boolean status = response.body().isStatus();
                    if (status) { // true
                        listaGestanteObstetra.clear();
                        listaGestanteObstetra.addAll(Arrays.asList(response.body().getData()));
                        // Indicar que hay cambios en los registros para actualizar
                        listaGestanteAdapter.notifyDataSetChanged();

                    }
                } else {
                    // HTTP: 500, 400, 401, otros estados de error
                    Log.e("Error al acceder al servicio web", response.message());
                }
            }

            @Override
            public void onFailure(final Call<ListaGestanteObstetraResponse> call, final Throwable t) {
                Log.e("Falla al conectarse al servicio web", t.getMessage());
                Helper.mensajeError(getActivity(), "No existen registros previos", "Debe tener al menos un registro en la opción 'Recordatorios'");

            }
        });
    }
    //String dni = String.valueOf(txtDni.getText());
    @Override
    public void onClick(View view) {
        dni = txtDni.getText().toString(); // Obtén el texto como String

        if (view.getId() == R.id.btnFiltrarDni) {
            if (!dni.isEmpty()) {  // Verifica si el campo no está vacío
                consultarGestanteObsDni(dni);  // Llamada al método de consulta
            } else {
                Log.e("Error", "No hay DNI ingresado");
            }
        }
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onRefresh() {
        if (getArguments() != null) {
            int gestanteId = getArguments().getInt("GEST_ID");
            //Log.e("gestanteId/desde main actovoty hasta historial", String.valueOf(gestanteId));
            listarGestanteObs();
        }
        swipeRefreshLayoutControlesGestanteObs.setRefreshing(false);
    }

    private void consultarGestanteObsDni(String dni) {
        // Crear una instancia de ApiService para hacer una solicitud al servicio web
        ApiService apiService = RetrofitClient.createService();
        Call<ListaGestanteObstetraResponse> call = apiService.consultarGestanteObstetraDni(dni);
        call.enqueue(new Callback<ListaGestanteObstetraResponse>() {
            @Override
            public void onResponse(final Call<ListaGestanteObstetraResponse> call, final Response<ListaGestanteObstetraResponse> response) {
                if (response.code() == 200) {
                    final boolean status = response.body().isStatus();
                    if (status) { // true
                        listaGestanteObstetra.clear();
                        listaGestanteObstetra.addAll(Arrays.asList(response.body().getData()));
                        // Indicar que hay cambios en los registros para actualizar
                        listaGestanteAdapter.notifyDataSetChanged();

                    }
                } else {
                    // HTTP: 500, 400, 401, otros estados de error
                    Log.e("Error al acceder al servicio web", response.message());
                }
            }

            @Override
            public void onFailure(final Call<ListaGestanteObstetraResponse> call, final Throwable t) {
                Log.e("Falla al conectarse al servicio web", t.getMessage());
                Helper.mensajeError(getActivity(), "No existen registros previos", "Debe tener al menos un registro en la opción 'Recordatorios'");

            }
        });
    }


    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 1:
                Log.e("opcion", "Seleccionaste 'Ver reportes'");
                // Aquí agregar el código para ver reportes

                int position = listaGestanteAdapter.posicionItemSeleccionado;
                if (position != RecyclerView.NO_POSITION) {
                    ListaGestanteObstetra selectedItem = listaGestanteObstetra.get(position);
                    int id = selectedItem.getId();
                    String nombreGestante= selectedItem.getNombres();
                    // Abrir la nueva actividad
                    Intent intent = new Intent(getContext(), ReporteObsSintomasActivity.class);
                    intent.putExtra("ID_GESTANTE", id);
                    intent.putExtra("NOMBRE_GESTANTE", nombreGestante);
                    //intent.putExtra("NOMBRE_OBSTETRA", nombreObstetra);
                    getContext().startActivity(intent);

                    Log.e("idgestateeeereporte", String.valueOf(id));
                    Log.e("nombre gestante", nombreGestante);

                }
                return true;
            case 2:
                Log.e("opcion", "Seleccionaste 'Dar baja'"+ getId());
                // Aquí agregar el código para dar de baja
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }



}