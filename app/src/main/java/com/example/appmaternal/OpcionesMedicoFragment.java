package com.example.appmaternal;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.android.material.button.MaterialButton;


public class OpcionesMedicoFragment extends Fragment implements View.OnClickListener{
    MaterialButton btnGestantesAlerta,btnListaGestantes;
    private LinearLayout loginOpcionesObstetra;
    String nombreObstetra;

    public static OpcionesMedicoFragment newInstance(String param1, String param2) {
        OpcionesMedicoFragment fragment = new OpcionesMedicoFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_opciones_medico, container, false);
        btnGestantesAlerta = view.findViewById(R.id.btnGestantesAlerta);
        btnGestantesAlerta.setOnClickListener(this);
        btnListaGestantes = view.findViewById(R.id.btnListaGestantes);
        btnListaGestantes.setOnClickListener(this);
        loginOpcionesObstetra = view.findViewById(R.id.loginOpcionesObstetra);

        Bundle args = getArguments();
        if (args != null) {
            nombreObstetra = args.getString("NOMBRE_OBSTETRA");
            Log.e("nannananaa",nombreObstetra);
            Intent intent = new Intent(getActivity(), ReporteObsSintomasActivity.class);
            // Pasar el dato nombreObstetra a la actividad
            intent.putExtra("NOMBRE_OBSTETRA", nombreObstetra);
        }



        return view;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btnGestantesAlerta) {
            gestanteAlerta();
        } else if (view.getId() == R.id.btnListaGestantes){
            listaGestante();
        }
    }

    private void gestanteAlerta() {

    }

    private void listaGestante() {
        Fragment nuevoFragmento = new CardviewGestanteListaObstetraFragment();
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container_lista_gestantes, nuevoFragmento);
        transaction.addToBackStack(null); // Opcional si deseas que el usuario pueda volver con el botón de retroceso
        transaction.commit();
        loginOpcionesObstetra.setVisibility(View.GONE);

    }
}