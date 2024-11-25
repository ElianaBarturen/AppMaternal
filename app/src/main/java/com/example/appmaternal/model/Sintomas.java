package com.example.appmaternal.model;

import java.io.Serializable;

public class Sintomas implements Serializable {
    private String nombre;

    public Sintomas(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public String toString() {
        return String.valueOf(nombre);
    }
}
