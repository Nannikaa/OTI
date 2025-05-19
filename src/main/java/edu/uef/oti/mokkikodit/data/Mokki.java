package edu.uef.oti.mokkikodit.data;

public class Mokki {
    private int mokkiId;
    private String nimi;
    private String sijainti;
    private float hintaPerYo;
    private String kuvaus;

    public Mokki(int mokkiId, String nimi, String sijainti, float hintaPerYo, String kuvaus) {
        this.mokkiId = mokkiId;
        this.nimi = nimi;
        this.sijainti = sijainti;
        this.hintaPerYo = hintaPerYo;
        this.kuvaus = kuvaus;
    }

    public int getMokkiId() {
        return mokkiId;
    }

    public String getNimi() {
        return nimi;
    }

    public String getSijainti() {
        return sijainti;
    }

    public float getHintaPerYo() {
        return hintaPerYo;
    }

    public String getKuvaus() {
        return kuvaus;
    }

    public String toString() {
        return
                "ID: " + mokkiId +
                ", nimi: " + nimi +
                ", sijainti: " + sijainti +
                ", hintaPerYo: " + hintaPerYo +
                ", kuvaus: " + kuvaus;
    }
}



