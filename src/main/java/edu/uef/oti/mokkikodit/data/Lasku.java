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
}
