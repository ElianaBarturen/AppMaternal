package com.example.appmaternal.model;

import java.io.Serializable;

public class Semanas implements Serializable {

    private int edad_gestacional_sem;

    public int getEdad_gestacional_sem() {
        return edad_gestacional_sem;
    }

    public void setEdad_gestacional_sem(int edad_gestacional_sem) {
        this.edad_gestacional_sem = edad_gestacional_sem;
    }

    public Semanas(int edad_gestacional_sem) {
        this.edad_gestacional_sem = edad_gestacional_sem;
    }

    public int getEdadGestacionalSem() {
        return edad_gestacional_sem;
    }
}
