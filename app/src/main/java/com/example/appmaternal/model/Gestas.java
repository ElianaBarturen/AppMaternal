package com.example.appmaternal.model;

public class Gestas {
    private int numGestas;

    public Gestas(int numGestas) {
        this.numGestas = numGestas;
    }


    public int getnumGestas() {
        return numGestas;
    }

    @Override
    public String toString() {
        return String.valueOf(numGestas);
    }
}
