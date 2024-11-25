package com.example.appmaternal.model;

public class DatosControlGestante {
    private int edad_gestante;
    private int edad_gestacional_sem;
    private float peso;
    private String pres_sistolica;
    private String pres_diastolica;
    private int id;
    private int frec_card;
    private String tipo_embarazo_id;
    private int gestas;
    private String dolor_epigastrio;
    private String VISION_BORROSA;
    private String CEFALEA;
    private String nauseas;
    private String hinchazon_pies;
    private String ant_familiar_id;
    private String ant_personal_id;
    private String gestante_id;
    private String fecha_registro;
    private int diagnostico;

    public DatosControlGestante(int edad_gestante, int edad_gestacional_sem, float peso, String pres_sistolica, String pres_diastolica, int id, int frec_card, String tipo_embarazo_id, int gestas, String dolor_epigastrio, String VISION_BORROSA, String CEFALEA, String nauseas, String hinchazon_pies, String ant_familiar_id, String ant_personal_id, String gestante_id, String fecha_registro, int diagnostico) {
        this.edad_gestante = edad_gestante;
        this.edad_gestacional_sem = edad_gestacional_sem;
        this.peso = peso;
        this.pres_sistolica = pres_sistolica;
        this.pres_diastolica = pres_diastolica;
        this.id = id;
        this.frec_card = frec_card;
        this.tipo_embarazo_id = tipo_embarazo_id;
        this.gestas = gestas;
        this.dolor_epigastrio = dolor_epigastrio;
        this.VISION_BORROSA = VISION_BORROSA;
        this.CEFALEA = CEFALEA;
        this.nauseas = nauseas;
        this.hinchazon_pies = hinchazon_pies;
        this.ant_familiar_id = ant_familiar_id;
        this.ant_personal_id = ant_personal_id;
        this.gestante_id = gestante_id;
        this.fecha_registro = fecha_registro;
        this.diagnostico = diagnostico;
    }

    public int getEdad_gestante() {
        return edad_gestante;
    }

    public void setEdad_gestante(int edad_gestante) {
        this.edad_gestante = edad_gestante;
    }

    public int getEdad_gestacional_sem() {
        return edad_gestacional_sem;
    }

    public void setEdad_gestacional_sem(int edad_gestacional_sem) {
        this.edad_gestacional_sem = edad_gestacional_sem;
    }

    public float getPeso() {
        return peso;
    }

    public void setPeso(float peso) {
        this.peso = peso;
    }

    public String getPres_sistolica() {
        return pres_sistolica;
    }

    public void setPres_sistolica(String pres_sistolica) {
        this.pres_sistolica = pres_sistolica;
    }

    public String getPres_diastolica() {
        return pres_diastolica;
    }

    public void setPres_diastolica(String pres_diastolica) {
        this.pres_diastolica = pres_diastolica;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFrec_card() {
        return frec_card;
    }

    public void setFrec_card(int frec_card) {
        this.frec_card = frec_card;
    }

    public String getTipo_embarazo_id() {
        return tipo_embarazo_id;
    }

    public void setTipo_embarazo_id(String tipo_embarazo_id) {
        this.tipo_embarazo_id = tipo_embarazo_id;
    }

    public int getGestas() {
        return gestas;
    }

    public void setGestas(int gestas) {
        this.gestas = gestas;
    }

    public String getDolor_epigastrio() {
        return dolor_epigastrio;
    }

    public void setDolor_epigastrio(String dolor_epigastrio) {
        this.dolor_epigastrio = dolor_epigastrio;
    }

    public String getVISION_BORROSA() {
        return VISION_BORROSA;
    }

    public void setVISION_BORROSA(String VISION_BORROSA) {
        this.VISION_BORROSA = VISION_BORROSA;
    }

    public String getCEFALEA() {
        return CEFALEA;
    }

    public void setCEFALEA(String CEFALEA) {
        this.CEFALEA = CEFALEA;
    }

    public String getNauseas() {
        return nauseas;
    }

    public void setNauseas(String nauseas) {
        this.nauseas = nauseas;
    }

    public String getHinchazon_pies() {
        return hinchazon_pies;
    }

    public void setHinchazon_pies(String hinchazon_pies) {
        this.hinchazon_pies = hinchazon_pies;
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

    public String getGestante_id() {
        return gestante_id;
    }

    public void setGestante_id(String gestante_id) {
        this.gestante_id = gestante_id;
    }

    public String getFecha_registro() {
        return fecha_registro;
    }

    public void setFecha_registro(String fecha_registro) {
        this.fecha_registro = fecha_registro;
    }

    public int getDiagnostico() {
        return diagnostico;
    }

    public void setDiagnostico(int diagnostico) {
        this.diagnostico = diagnostico;
    }
}
