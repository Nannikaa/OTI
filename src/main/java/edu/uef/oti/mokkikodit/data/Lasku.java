package edu.uef.oti.mokkikodit.data;

public class Lasku {
    private int id;
    private int varausId;
    private float summa;
    private Boolean maksettu;

    public Lasku(int id, int varausId, float summa, boolean maksettu) {
        this.id = id;
        this.varausId = varausId;
        this.summa = summa;
        this.maksettu = maksettu;
    }

    public int getId() {
        return id;
    }

    public int getVarausId() {
        return varausId;
    }

    public float getSumma() {
        return summa;
    }

    public boolean getMaksettu() {
        return maksettu;
    }
    public void setId(int id) {
        this.id = id;
    }

    public void setVarausId(int varausId) {
        this.varausId = varausId;
    }

    public void setSumma(float summa) {
        this.summa = summa;
    }

    public void setMaksettu(Boolean maksettu) {
        this.maksettu = maksettu;
    }
}
