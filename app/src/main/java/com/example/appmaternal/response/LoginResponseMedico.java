package com.example.appmaternal.response;

import com.example.appmaternal.model.Sesion;
import com.example.appmaternal.model.Sesionmedico;

public class LoginResponseMedico {
    private Sesionmedico data;
    private String message;
    private boolean status;

    public Sesionmedico getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }

    public boolean isStatus() {
        return status;
    }
}
