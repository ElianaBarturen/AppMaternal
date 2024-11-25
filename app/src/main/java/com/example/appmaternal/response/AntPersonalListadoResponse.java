package com.example.appmaternal.response;

import com.example.appmaternal.model.AntPersonal;

public class AntPersonalListadoResponse {
    private AntPersonal data[];
    private String message;
    private boolean status;

    public AntPersonal[] getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }

    public boolean isStatus() {
        return status;
    }
}
