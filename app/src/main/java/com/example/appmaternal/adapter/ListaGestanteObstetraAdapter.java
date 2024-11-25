package com.example.appmaternal.adapter;

import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appmaternal.R;
import com.example.appmaternal.model.ListaGestanteObstetra;

import java.util.List;

public class ListaGestanteObstetraAdapter extends RecyclerView.Adapter<ListaGestanteObstetraAdapter.ListaGestanteObstetraViewHolder> {
    private List<ListaGestanteObstetra> listaGestanteObstetra;
    public int posicionItemSeleccionado;



    public ListaGestanteObstetraAdapter(final List<ListaGestanteObstetra> listaGestanteObstetra){
        this.listaGestanteObstetra = listaGestanteObstetra;
    }

    @NonNull
    @Override
    public ListaGestanteObstetraViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, final int viewType) {
        //Vincular el adapter a la vista (cardview cliente). Archivo: cliente_cardview.xml (layout)
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.gestante_medico_cardview, parent, false);
        return new ListaGestanteObstetraViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ListaGestanteObstetraViewHolder holder, int position) {
        ListaGestanteObstetra listaGesObstetra = listaGestanteObstetra.get(position);
        holder.bind(listaGesObstetra);
    }

    @Override
    public int getItemCount() {
        //Determina la cantidad de registros a asignar al recyclerView
        return listaGestanteObstetra.size();
    }


    public class ListaGestanteObstetraViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener, View.OnLongClickListener{
        //Declarar los controles que tiene el cardview de cliente
        private TextView idTextView,dniTextView,nombreTextView,emailTextView,estadoTextView,edadgestanteTextView;

        public ListaGestanteObstetraViewHolder(@NonNull View itemView) {
            super(itemView);
            //relacionar los controles del cardview con los controles declarados en java
            idTextView= itemView.findViewById(R.id.idTextView);
            dniTextView= itemView.findViewById(R.id.dniTextView);
            nombreTextView= itemView.findViewById(R.id.nombreTextView);
            emailTextView= itemView.findViewById(R.id.emailTextView);
            estadoTextView= itemView.findViewById(R.id.estadoTextView);
            edadgestanteTextView= itemView.findViewById(R.id.edadgestanteTextView);


            //Reconocer los eventos onCreateCONTEXTmENUlISTENER
            itemView.setOnCreateContextMenuListener(this);
            itemView.setOnLongClickListener(this);


        }



        public void bind(ListaGestanteObstetra listaGestanteObstetra){
            idTextView.setText("Id gestante: " + listaGestanteObstetra.getId());
            dniTextView.setText("Dni: " + listaGestanteObstetra.getDni());
            nombreTextView.setText("Nombres: " + listaGestanteObstetra.getNombres());
            emailTextView.setText("Email: " + listaGestanteObstetra.getEmail());
            estadoTextView.setText("Estado: " + listaGestanteObstetra.getEstado_usuaria());
            edadgestanteTextView.setText("Edad gestante: " + listaGestanteObstetra.getEdad_gestante());



        }

        @Override
        public void onCreateContextMenu(final ContextMenu contextMenu, final View view, final ContextMenu.ContextMenuInfo contextMenuInfo) {
            //Crear las opciones del menú (Se visualizará cuando el usuario haga un click prolongado sobre el cardview
            contextMenu.setHeaderTitle("Elija una opción");
            contextMenu.add(0,1,0,"Ver reportes");
            contextMenu.add(0,2,0,"Eliminar gestante");

        }


        @Override
        public boolean onLongClick(final View view) {
            posicionItemSeleccionado = this.getAdapterPosition();
            Log.e("Posicion del item seleccionado", String.valueOf(posicionItemSeleccionado));

            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                ListaGestanteObstetra item = listaGestanteObstetra.get(position);
                int id = item.getId();
                // Maneja el clic normal aquí (puedes usar un Toast o cualquier otra acción)
                Log.e("ItemClicked", "ID: " + id);
            }

            return false;
        }



    }

}
