package com.example.appmaternal.response;
import com.example.appmaternal.model.TipoEmbarazo;

public class TipoEmbarazoListadoResponse {
    private TipoEmbarazo data[];
    private String message;
    private boolean status;

    public TipoEmbarazo[] getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }

    public boolean isStatus() {
        return status;
    }
}
