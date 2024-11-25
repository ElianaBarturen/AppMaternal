package com.example.appmaternal;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class SeccionInformativaFragment extends Fragment implements View.OnClickListener {

    Button btnContactar;

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
         View view= inflater.inflate(R.layout.fragment_seccion_informativa, container, false);

        btnContactar = view.findViewById(R.id.btn_contactar);
        

        btnContactar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Abre el marcador de teléfono con el número de emergencia
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:999999999"));
                startActivity(intent);
            }
        });
        return view;
    }

    @Override
    public void onClick(View view) {

    }
}