package com.example.appmaternal.model;

public class Prediction {
    private int frecCardiaca;
    private int sistolica;
    private int diastolica;
    private float peso;
    private int edad_gestacional;
    private int visionBorrosaId;
    private int nauseasId;
    private int cefaleaId;
    private int hinchazonPiesId;
    private int tipo_embarazo_id;
    private int gestas;
    private int ant_familiar_id;
    private int ant_personal_id;
    private  int edad_gestante;
    private int dolorEpigastrioId;

    // Constructor, getters y setters

    public Prediction(int frecCardiaca, int sistolica, int diastolica, float peso, int edad_gestacional, int visionBorrosaId, int nauseasId, int cefaleaId, int hinchazonPiesId, int tipo_embarazo_id, int gestas, int ant_familiar_id, int ant_personal_id, int edad_gestante, int dolorEpigastrioId) {
        this.frecCardiaca = frecCardiaca;
        this.sistolica = sistolica;
        this.diastolica = diastolica;
        this.peso = peso;
        this.edad_gestacional = edad_gestacional;
        this.visionBorrosaId = visionBorrosaId;
        this.nauseasId = nauseasId;
        this.cefaleaId = cefaleaId;
        this.hinchazonPiesId = hinchazonPiesId;
        this.tipo_embarazo_id = tipo_embarazo_id;
        this.gestas = gestas;
        this.ant_familiar_id = ant_familiar_id;
        this.ant_personal_id = ant_personal_id;
        this.edad_gestante = edad_gestante;
        this.dolorEpigastrioId = dolorEpigastrioId;
    }


    // Getters and setters (omitidos por brevedad)
}
