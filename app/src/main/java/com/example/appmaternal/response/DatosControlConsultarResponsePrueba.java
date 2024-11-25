package com.example.appmaternal.response;

import com.example.appmaternal.model.Resultado;

import java.util.List;

public class DatosControlConsultarResponsePrueba {
    private Resultado data[];
    private String message;
    private boolean status;

    // Getters
    public Resultado[] getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }

    public boolean isStatus() {
        return status;
    }
}
