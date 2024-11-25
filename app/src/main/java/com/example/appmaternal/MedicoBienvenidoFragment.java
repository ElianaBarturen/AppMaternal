package com.example.appmaternal;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.button.MaterialButton;

public class MedicoBienvenidoFragment extends Fragment implements View.OnClickListener{



    public static MedicoBienvenidoFragment newInstance(String param1, String param2) {
        MedicoBienvenidoFragment fragment = new MedicoBienvenidoFragment();
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
        View view = inflater.inflate(R.layout.fragment_medico_bienvenido, container, false);

        return view;
    }




    @Override
    public void onClick(View v) {

    }
}