package com.example.appmaternal.model;

import com.google.gson.annotations.SerializedName;

public class Gestante {
    private String nombres;
    private String dni;
    private String fecha_nac;
    private int edad_gestante;
    private String email;
    private int id;
    private String usuaria_id;
    private String tipo_embarazo_id;
    private int gestas;
    private String estado;

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public int getGestas() {
        return gestas;
    }

    public void setGestas(int gestas) {
        this.gestas = gestas;
    }

    private String ant_familiar_id;
    private String ant_personal_id;
    private int edad_gestacional;
    private String fecha_registro;

    public String getFecha_registro() {
        return fecha_registro;
    }

    public void setFecha_registro(String fecha_registro) {
        this.fecha_registro = fecha_registro;
    }

    public int getEdad_gestacional() {
        return edad_gestacional;
    }

    public void setEdad_gestacional(int edad_gestacional) {
        this.edad_gestacional = edad_gestacional;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getFecha_nac() {
        return fecha_nac;
    }

    public void setFecha_nac(String fecha_nac) {
        this.fecha_nac = fecha_nac;
    }

    public int getEdad_gestante() {
        return edad_gestante;
    }

    public void setEdad_gestante(int edad_gestante) {
        this.edad_gestante = edad_gestante;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsuaria_id() {
        return usuaria_id;
    }

    public void setUsuaria_id(String usuaria_id) {
        this.usuaria_id = usuaria_id;
    }


    public String getTipo_embarazo_id() {
        return tipo_embarazo_id;
    }

    public void setTipo_embarazo_id(String tipo_embarazo_id) {
        this.tipo_embarazo_id = tipo_embarazo_id;
    }

    public String getAnt_familiar_id() {
        return ant_familiar_id;
    }

    public void setAnt_familiar_id(String ant_familiar_id) {
        this.ant_familiar_id = ant_familiar_id;
    }

    public String getAnt_personal_id() {
        return ant_personal_id;
    }

    public void setAnt_personal_id(String ant_personal_id) {
        this.ant_personal_id = ant_personal_id;
    }
}
