package com.example.appmaternal.response;

public class GestanteDatosControlAgregarResponse {
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
