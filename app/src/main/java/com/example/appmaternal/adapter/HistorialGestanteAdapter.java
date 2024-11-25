package com.example.appmaternal.adapter;

import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appmaternal.R;
import com.example.appmaternal.model.DatosControlGestante;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class HistorialGestanteAdapter extends RecyclerView.Adapter<HistorialGestanteAdapter.DatosControlGestanteViewHolder> {
    private List<DatosControlGestante> listadatosControlGestante;
    public int posicionItemSeleccionado;


    public HistorialGestanteAdapter(final List<DatosControlGestante> listadatosControlGestante){
        this.listadatosControlGestante = listadatosControlGestante;
    }

    @NonNull
    @Override
    public DatosControlGestanteViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, final int viewType) {
        //Vincular el adapter a la vista (cardview cliente). Archivo: cliente_cardview.xml (layout)
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.historial_gestante_cardview, parent, false);
        return new DatosControlGestanteViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull DatosControlGestanteViewHolder holder, int position) {
        DatosControlGestante datosControlGestante = listadatosControlGestante.get(position);
        holder.bind(datosControlGestante);
    }

    @Override
    public int getItemCount() {
        //Determina la cantidad de registros a asignar al recyclerView
        return listadatosControlGestante.size();
    }

    public void listarTodosControles() {
    }

    public class DatosControlGestanteViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener, View.OnLongClickListener{
        //Declarar los controles que tiene el cardview de cliente
        private TextView fechaTextView,semGestacionalTextView,frecCardiacaTextView,pesoTextView,sistolicaTextView,diastolicaTextView,dolorEpigastrioTextView,visionBorrosaTextView,cefaleaTextView,nauseasTextView,diagnosticoTextView;

        public DatosControlGestanteViewHolder(@NonNull View itemView) {
            super(itemView);
            //relacionar los controles del cardview con los controles declarados en java
            fechaTextView= itemView.findViewById(R.id.fechaTextView);
            semGestacionalTextView= itemView.findViewById(R.id.semGestacionalTextView);
            Log.e("semana", String.valueOf(semGestacionalTextView));
            frecCardiacaTextView= itemView.findViewById(R.id.frecCardiacaTextView);
            pesoTextView= itemView.findViewById(R.id.pesoTextView);
            sistolicaTextView= itemView.findViewById(R.id.sistolicaTextView);
            diastolicaTextView= itemView.findViewById(R.id.diastolicaTextView);
            dolorEpigastrioTextView= itemView.findViewById(R.id.dolorEpigastrioTextView);
            visionBorrosaTextView= itemView.findViewById(R.id.visionBorrosaTextView);
            cefaleaTextView= itemView.findViewById(R.id.cefaleaTextView);
            nauseasTextView= itemView.findViewById(R.id.nauseasTextView);
            diagnosticoTextView= itemView.findViewById(R.id.diagnosticoTextView);

            //Reconocer los eventos onCreateCONTEXTmENUlISTENER
            itemView.setOnCreateContextMenuListener(this);
            itemView.setOnLongClickListener(this);


        }


        public void bind(DatosControlGestante datosControlGestante){
            fechaTextView.setText("Fecha: " + datosControlGestante.getFecha_registro());
            semGestacionalTextView.setText("Sem.Gestacional: " + datosControlGestante.getEdad_gestacional_sem());
            frecCardiacaTextView.setText("Frec.Card.: " + datosControlGestante.getFrec_card());
            pesoTextView.setText("Peso: " + datosControlGestante.getPeso());
            sistolicaTextView.setText("Pres.Sist.: " + datosControlGestante.getPres_sistolica());
            diastolicaTextView.setText("Pres.Diast.: " + datosControlGestante.getPres_diastolica());
            dolorEpigastrioTextView.setText("Dolor Epig.: " + datosControlGestante.getDolor_epigastrio());
            visionBorrosaTextView.setText("Visión Borr.: " + datosControlGestante.getVISION_BORROSA());
            cefaleaTextView.setText("Cefalea: " + datosControlGestante.getCEFALEA());
            nauseasTextView.setText("Náuseas: " + datosControlGestante.getNauseas());
            if(datosControlGestante.getDiagnostico() ==1){
                String diag = "Riesgo de preeclampsia";
                diagnosticoTextView.setText("Diagnóstico: " + diag);
            }else{
                String nodiag ="No existe riesgo de preeclapmsia";
                diagnosticoTextView.setText("Diagnóstico: " + nodiag);
            }




        }

        @Override
        public void onCreateContextMenu(final ContextMenu contextMenu, final View view, final ContextMenu.ContextMenuInfo contextMenuInfo) {
            //Crear las opciones del menú (Se visualizará cuando el usuario haga un click prolongado sobre el cardview
            contextMenu.setHeaderTitle("Elija una opción");
            contextMenu.add(0,1,0,"ver datos");
            contextMenu.add(0,2,0,"Eliminar");
        }

        @Override
        public boolean onLongClick(final View view) {
            posicionItemSeleccionado = this.getAdapterPosition();
            Log.e("Posicion del item seleccionado", String.valueOf(posicionItemSeleccionado));
            return false;
        }
    }

}
