package com.example.appmaternal.response;

import com.example.appmaternal.model.DatosControlGestante;

public class HistorialGestanteResponse {
    private DatosControlGestante data[]; // [] va corchetes porque devuelven muchos datos
    private String message;
    private boolean status;

    public DatosControlGestante[] getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }

    public boolean isStatus() {
        return status;
    }
}
