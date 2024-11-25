package com.example.appmaternal.response;
import com.example.appmaternal.model.Semanas;
import com.example.appmaternal.model.Sintomas;

public class SemanasResponse {
    private Semanas data[];
    private String message;
    private boolean status;

    public Semanas[] getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }

    public boolean isStatus() {
        return status;
    }
}
