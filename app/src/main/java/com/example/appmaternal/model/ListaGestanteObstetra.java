package com.example.appmaternal.model;

public class ListaGestanteObstetra {

    private int id;
    private String nombres;
    private String dni;
    private int edad_gestante;
    private String email;
    private String fecha_registro;
    private String estado_usuaria;

    public String getEstado_usuaria() {
        return estado_usuaria;
    }

    public void setEstado_usuaria(String estado_usuaria) {
        this.estado_usuaria = estado_usuaria;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getFecha_registro() {
        return fecha_registro;
    }

    public void setFecha_registro(String fecha_registro) {
        this.fecha_registro = fecha_registro;
    }

    public ListaGestanteObstetra(int id, String nombres, String dni, int edad_gestante, String email, String fecha_registro, String estado_usuaria) {
        this.id = id;
        this.nombres = nombres;
        this.dni = dni;
        this.edad_gestante = edad_gestante;
        this.email = email;
        this.fecha_registro = fecha_registro;
        this.estado_usuaria = estado_usuaria;
    }
}
