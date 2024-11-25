package com.example.appmaternal.response;

import com.example.appmaternal.model.DatosControlGestante;

public class DatosControlConsultarResponse {
    private DatosControlGestante data; // se recibe datos de un solo cliente, por eso sin []
    private String message;
    private boolean status;

    public DatosControlGestante getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }

    public boolean isStatus() {
        return status;
    }
}
