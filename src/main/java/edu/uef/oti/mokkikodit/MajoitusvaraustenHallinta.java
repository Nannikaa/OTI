package edu.uef.oti.mokkikodit;

import edu.uef.oti.mokkikodit.data.Varaus;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class MajoitusvaraustenHallinta {
    private List<Varaus> varaukset = new ArrayList<>();
    private int nextId = 1;

    public void lisaaVaraus(int asiakasId, int mokkiId, Date alkuPvm, Date loppuPvm) {
        Varaus varaus = new Varaus(nextId++, asiakasId, mokkiId, alkuPvm, loppuPvm);
        varaukset.add(varaus);
        System.out.println("Varaus lisätty: \n" + varaus);

    }

    public void listVaraus() {
        if (varaukset.isEmpty()) {
            System.out.println("Varauksia ei löydy");
            return;
        }
        for (Varaus v : varaukset) {
            System.out.println(v);
        }
    }

    public Varaus etsiVaraus(int id) {
        for (Varaus v : varaukset) {
            if (v.getVarausId() == id) return v;
        }
        return null;
    }

    public boolean poistaVaraus(int varausId) {
        Varaus v = etsiVaraus(varausId);
        if ( v != null) {
            varaukset.remove(v);
            System.out.println("Varaus poistettu listalta.");
            return true;
        }
        System.out.println("Varausta ei löydy listalta.");
        return false;
    }
    public boolean paivitaVaraus(int mokkiId, int asiakasId, Date alkuPvm, Date loppuPvm) {
        Varaus v = etsiVaraus(mokkiId);
        if ( v != null) {
            v.setMokkiId(mokkiId);
            v.setAsiakasId(asiakasId);
            v.setAlkuPvm(alkuPvm);
            v.setLoppuPvm(loppuPvm);
            System.out.println("Varauksen tiedot on päivitetty: " + v);
            return true;
        }
        System.out.println("Varausta ei löytynyt listalta.");
        return false;
    }
}
