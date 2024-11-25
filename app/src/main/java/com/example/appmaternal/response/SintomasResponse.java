package com.example.appmaternal.response;
import com.example.appmaternal.model.Sintomas;
import com.example.appmaternal.model.TipoEmbarazo;

import com.example.appmaternal.model.Sintomas;

public class SintomasResponse {
    private Sintomas[] data;
    private String message;
    private boolean status;

    public Sintomas[] getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }

    public boolean isStatus() {
        return status;
    }
}


