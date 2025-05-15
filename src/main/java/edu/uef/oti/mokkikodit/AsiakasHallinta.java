package edu.uef.oti.mokkikodit;

import edu.uef.oti.mokkikodit.data.Asiakas;

import java.util.ArrayList;
import java.util.List;

public class AsiakasHallinta {
    private List<Asiakas> asiakkaat = new ArrayList<>();
    private int nextId = 1;

    public void lisaaAsiakas(String nimi, String puhelin, String sahkoposti, String osoite, String hlotunnus, boolean onkoYritys) {
        Asiakas asiakas = new Asiakas(nextId++, nimi, puhelin, sahkoposti, osoite, hlotunnus, onkoYritys);
        asiakkaat.add(asiakas);
        System.out.println("Asiakas lisätty: \n" + asiakas);
    }

    public void listAsiakas() {
        if (asiakkaat.isEmpty()) {
            System.out.println("Asiakkaita ei löydy");
            return;
        }
        for (Asiakas a : asiakkaat) {
            System.out.println(a);
        }
    }

    public Asiakas etsiAsiakas(int id) {
        for (Asiakas a : asiakkaat) {
            if (a.getAsiakasId() == id) return a;
        }
        return null;
    }

    public boolean poistaAsiakas(int id) {
        Asiakas a = etsiAsiakas(id);
        if ( a != null) {
            asiakkaat.remove(a);
            System.out.println("Asiakas poistettu listalta.");
            return true;
        }
        System.out.println("Asiakasta ei löydy listalta.");
        return false;
    }

    public boolean paivitaAsiakas(int id, String nimi, String puhelinro, String sahkoposti, String osoite, String hlotunnus, boolean onkoYritys) {
        Asiakas a = etsiAsiakas(id);
        if ( a != null) {
            a.setNimi(nimi);
            a.setPuhelin(puhelinro);
            a.setSahkoposti(sahkoposti);
            a.setOsoite(osoite);
            a.setHlotunnus(hlotunnus);
            a.setOnkoYritys(onkoYritys);
            System.out.println("Asiakkaan tiedot on päivitetty: " + a);
            return true;
        }
        System.out.println("Asiakasta ei löytynyt listalta.");
        return false;
    }
}


