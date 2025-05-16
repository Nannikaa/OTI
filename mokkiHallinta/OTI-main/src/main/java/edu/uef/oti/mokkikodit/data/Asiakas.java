package edu.uef.oti.mokkikodit.data;

public class Asiakas {
    private int asiakasId;
    private String nimi;
    private String puhelin;
    private String sahkoposti;
    private String osoite;
    private String hlotunnus;
    private boolean onkoYritys;

    public Asiakas(int asiakasId, String nimi, String puhelin, String sahkoposti, String osoite, String hlotunnus, Boolean onkoYritys) {
        this.asiakasId = asiakasId;
        this.nimi = nimi;
        this.puhelin = puhelin;
        this.sahkoposti = sahkoposti;
        this.osoite = osoite;
        this.hlotunnus = hlotunnus;
        this.onkoYritys = onkoYritys;
    }

    public int getAsiakasId() {
        return asiakasId;
    }

    public void setAsiakasId(int asiakasId) {
        this.asiakasId = asiakasId;
    }

    public String getNimi() {
        return nimi;
    }

    public void setNimi(String nimi) {
        this.nimi = nimi;
    }

    public String getPuhelin() {
        return puhelin;
    }

    public void setPuhelin(String puhelin) {
        this.puhelin = puhelin;
    }

    public String getSahkoposti() {
        return sahkoposti;
    }

    public void setSahkoposti(String sahkoposti) {
        this.sahkoposti = sahkoposti;
    }

    public String getOsoite() {
        return osoite;
    }

    public void setOsoite(String osoite) {
        this.osoite = osoite;
    }

    public String getHlotunnus() {
        return hlotunnus;
    }

    public void setHlotunnus(String hlotunnus) {
        this.hlotunnus = hlotunnus;
    }

    public boolean isOnkoYritys() {
        return onkoYritys;
    }

    public void setOnkoYritys(boolean onkoYritys) {
        this.onkoYritys = onkoYritys;
    }

    @Override
    public String toString() {
        return "ID: " + asiakasId + ", nimi: " + nimi + ", puhelinnumero: " + puhelin + ", sähköposti: " + sahkoposti + ", osoite: " + osoite + ", henkilötunnus: " + hlotunnus + ", Onko yritys: " + (onkoYritys ? "Kyllä" : "Ei");
    }
}
