package com.example.appmaternal.model;

import com.google.gson.annotations.SerializedName;

public class Sesionmedico {
    @SerializedName("email")
    private String email;
    private String estado_medico;
    @SerializedName("id")
    private Integer id;
    private String imagen;
    @SerializedName("nombres")
    private String nombres;
    private String token;
    private String clave;

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    /*Declarar un atributo para almacenar los datos de la sesion*/
    public static Sesionmedico DATOS_SESION;

    public String getEstado_medico() {
        return estado_medico;
    }

    public void setEstado_medico(String estado_medico) {
        this.estado_medico = estado_medico;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }


    public int getId() {
        return id;
    }

    public void setId(final int id) {
        this.id = id;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(final String imagen) {
        this.imagen = imagen;
    }


    public String getToken() {
        return token;
    }

    public void setToken(final String token) {
        this.token = token;
    }

    public static Sesionmedico getDatosSesion() {
        return DATOS_SESION;
    }

    public static void setDatosSesion(final Sesionmedico datosSesion) {
        DATOS_SESION = datosSesion;
    }

}
