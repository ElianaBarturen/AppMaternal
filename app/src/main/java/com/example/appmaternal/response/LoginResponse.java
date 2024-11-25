package com.example.appmaternal.response;

import com.example.appmaternal.model.Sesion;

public class LoginResponse {
    private Sesion data;
    private String message;
    private boolean status;

    public Sesion getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }

    public boolean isStatus() {
        return status;
    }
}
