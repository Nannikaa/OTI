package edu.uef.oti.mokkikodit.data;

import java.sql.Date;
public class Varaus {
    private int varausId;
    private int asiakasId;
    private int mokkiId;
    private Date alkuPvm;
    private Date loppuPvm;

    public Varaus(int id, int asiakasId, int mokkiId, Date alkuPvm, Date loppuPvm) {
        this.varausId = varausId;
        this.asiakasId = asiakasId;
        this.mokkiId = mokkiId;
        this.alkuPvm = alkuPvm;
        this.loppuPvm = loppuPvm;
    }

    public int getVarausId() {
        return varausId;
    }

    public int getAsiakasId() {
        return asiakasId;
    }

    public int getMokkiId() {
        return mokkiId;
    }

    public Date getAlkuPvm() {
        return alkuPvm;
    }

    public Date getLoppuPvm() {
        return loppuPvm;
    }

    public void setVarausId(int varausId) {
        this.varausId = varausId;
    }

    public void setAsiakasId(int asiakasId) {
        this.asiakasId = asiakasId;
    }

    public void setMokkiId(int mokkiId) {
        this.mokkiId = mokkiId;
    }

    public void setAlkuPvm(Date alkuPvm) {
        this.alkuPvm = alkuPvm;
    }

    public void setLoppuPvm(Date loppuPvm) {
        this.loppuPvm = loppuPvm;
    }
}
