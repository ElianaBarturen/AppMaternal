package com.example.appmaternal.response;

import com.example.appmaternal.model.Gestante;

public class GestanteConsultarResponse {
    private Gestante data; // se recibe datos de un solo cliente, por eso sin []
    private String message;
    private boolean status;

    public Gestante getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }

    public boolean isStatus() {
        return status;
    }
}
