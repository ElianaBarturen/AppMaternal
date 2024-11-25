package com.example.appmaternal.response;
import com.example.appmaternal.model.ObsDatosControlGestanteReporte;

import java.util.List;

public class ObsDatosControlReporterResponse {
    // Cambiamos 'data' a ser una lista de 'ObsDatosControlGestanteReporte'
    private List<ObsDatosControlGestanteReporte> data;
    private String message;
    private boolean status;

    // Método para devolver la lista de datos
    public List<ObsDatosControlGestanteReporte> getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }

    public boolean isStatus() {
        return status;
    }
}
