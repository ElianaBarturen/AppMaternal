package com.example.appmaternal.model;

import com.google.gson.annotations.SerializedName;

public class Usuaria {
    @SerializedName("nombres")

    private String nombres;
    @SerializedName("email")
    private String email;
    @SerializedName("id")
    private int id;


    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
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
}
