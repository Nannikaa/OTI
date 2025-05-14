package edu.uef.oti.mokkikodit.data;

public class Asiakas {
    private int asiakasId;
    private String nimi;
    private String sahkoposti;
    private String puhelin;

    public Asiakas(int asiakasId, String nimi, String sahkoposti, String puhelin) {
        this.asiakasId = asiakasId;
        this.nimi = nimi;
        this.sahkoposti = sahkoposti;
        this.puhelin = puhelin;
    }

    public int getAsiakasId() {
        return asiakasId;
    }

    public String getNimi() {
        return nimi;
    }

    public String getSahkoposti() {
        return sahkoposti;
    }

    public String getPuhelin() {
        return puhelin;
    }
}
