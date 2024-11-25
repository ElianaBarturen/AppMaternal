package com.example.appmaternal.model;

public class Resultado {
    private int CEFALEA;
    private int dolor_epigastrio;
    private int hinchazon_pies;
    private int nauseas;
    private int vision_borrosa;

    public void setCEFALEA(int CEFALEA) {
        this.CEFALEA = CEFALEA;
    }

    public void setDolor_epigastrio(int dolor_epigastrio) {
        this.dolor_epigastrio = dolor_epigastrio;
    }

    public void setHinchazon_pies(int hinchazon_pies) {
        this.hinchazon_pies = hinchazon_pies;
    }

    public void setNauseas(int nauseas) {
        this.nauseas = nauseas;
    }

    public void setVision_borrosa(int vision_borrosa) {
        this.vision_borrosa = vision_borrosa;
    }

    // Getters
    public int getCEFALEA() {
        return CEFALEA;
    }

    public int getDolor_epigastrio() {
        return dolor_epigastrio;
    }

    public int getHinchazon_pies() {
        return hinchazon_pies;
    }

    public int getNauseas() {
        return nauseas;
    }

    public int getVision_borrosa() {
        return vision_borrosa;
    }
}

