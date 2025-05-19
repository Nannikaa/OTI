package edu.uef.oti.mokkikodit.data;

import java.sql.Date;

public class Lasku {
    private int id;
    private int varausId;
    private float summa;
    private Boolean maksettu;
    private Date maksuPvm;

    public Lasku(int id, int varausId, float summa, boolean maksettu, Date maksuPvm) {
        this.id = id;
        this.varausId = varausId;
        this.summa = summa;
        this.maksettu = maksettu;
        this.maksuPvm = maksuPvm;
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

    public Date getMaksuPvm() {
        return maksuPvm;
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

    public void setMaksuPvm(Date maksuPvm) {
        this.maksuPvm = maksuPvm;
    }

    public String toString() {
        return
                "ID: " + id +
                ", Varaus Id: " + varausId +
                ", summa: " + summa +
                ", maksettu: " + maksettu +
                ", maksuPvm: " + maksuPvm;
    }
}
