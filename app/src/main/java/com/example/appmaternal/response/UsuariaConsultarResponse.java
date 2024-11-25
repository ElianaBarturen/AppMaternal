package com.example.appmaternal.response;

import com.example.appmaternal.model.Usuaria;

public class UsuariaConsultarResponse {
    private Usuaria data; // se recibe datos de un solo cliente, por eso sin []
    private String message;
    private boolean status;

    public Usuaria getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }

    public boolean isStatus() {
        return status;
    }
}
