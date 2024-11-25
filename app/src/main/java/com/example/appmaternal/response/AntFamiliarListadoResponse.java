package com.example.appmaternal.response;

import com.example.appmaternal.model.AntFamiliar;

public class AntFamiliarListadoResponse {
    private AntFamiliar data[];
    private String message;
    private boolean status;

    public AntFamiliar[] getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }

    public boolean isStatus() {
        return status;
    }
}
