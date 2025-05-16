package edu.uef.oti.mokkikodit.data;

import java.sql.Date;
public class Varaus {
    private int id;
    private int asiakasId;
    private int mokkiId;
    private Date AlkuPvm;
    private Date LoppuPvm;

    public Varaus(int id, int asiakasId, int mokkiId, Date alkuPvm, Date loppuPvm) {
        this.id = id;
        this.asiakasId = asiakasId;
        this.mokkiId = mokkiId;
        this.AlkuPvm = alkuPvm;
        this.LoppuPvm = loppuPvm;
    }

    public int getId() {
        return id;
    }

    public int getAsiakasId() {
        return asiakasId;
    }

    public int getMokkiId() {
        return mokkiId;
    }

    public Date getAlkuPvm() {
        return AlkuPvm;
    }

    public Date getLoppuPvm() {
        return LoppuPvm;
    }
}
