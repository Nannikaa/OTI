package edu.uef.oti.mokkikodit;

import edu.uef.oti.mokkikodit.data.Lasku;

import java.util.ArrayList;
import java.util.List;

public class LaskujenHallinta {
    private List<Lasku> laskut = new ArrayList<>();
    private int nextId = 1;

    public void lisaaLasku(int varausId, float summa, boolean maksettu) {
        Lasku lasku = new Lasku(nextId++, varausId, summa, maksettu);
        laskut.add(lasku);
        System.out.println("Lasku lisätty: " + lasku);

    }
    public void listLasku() {
        if (laskut.isEmpty()){
            System.out.println("Laskuja ei löydy.");
            return;
        }
        for (Lasku l : laskut) {
            System.out.println(l);
        }

    }
    public Lasku etsiLasku(int id) {

        for (Lasku l : laskut) {
            if (l.getId() == id) return l;
        }
        return null;
    }

    public boolean poistaLasku(int id) {
        Lasku l = etsiLasku(id);
        if ( l != null) {
            laskut.remove(l);
            System.out.println("Lasku poistettu listalta.");
            return true;
        }
        System.out.println("Laskua ei löydy listalta.");
        return false;
    }

    public boolean paivitaLasku(int id, int summa, Boolean maksettu) {
        Lasku l = etsiLasku(id);
        if ( l != null) {
            l.setSumma(summa);
            l.setMaksettu(maksettu);
            System.out.println("Laskun tiedot on päivitetty: " + l);
            return true;
        }
        System.out.println("Laskua ei löytynyt listalta.");
        return false;
    }
}
