package com.example.appmaternal.model;

import com.google.gson.annotations.SerializedName;

public class Sesion {
    @SerializedName("email")
    private String email;
    private String estado_usuaria;
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
    public static Sesion DATOS_SESION;

    public String getEstado_usuaria() {
        return estado_usuaria;
    }

    public void setEstado_usuaria(String estado_usuaria) {
        this.estado_usuaria = estado_usuaria;
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

    public static Sesion getDatosSesion() {
        return DATOS_SESION;
    }

    public static void setDatosSesion(final Sesion datosSesion) {
        DATOS_SESION = datosSesion;
    }

}
