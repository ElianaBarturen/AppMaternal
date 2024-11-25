package com.example.appmaternal.response;

public class UsuariaRegistrarResponse {
    private String data; //el ws devuelve "NONE"
    private boolean status;
    private String message;

    public String getData() {
        return data;
    }

    public boolean isStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
