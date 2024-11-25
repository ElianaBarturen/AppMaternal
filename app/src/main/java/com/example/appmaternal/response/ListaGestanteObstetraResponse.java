package com.example.appmaternal.response;

import com.example.appmaternal.model.ListaGestanteObstetra;

public class ListaGestanteObstetraResponse {
    private ListaGestanteObstetra data[];
    private String message;
    private boolean status;

    public ListaGestanteObstetra[] getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }

    public boolean isStatus() {
        return status;
    }
}
