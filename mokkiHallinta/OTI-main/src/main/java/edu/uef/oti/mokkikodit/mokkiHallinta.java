package edu.uef.oti.mokkikodit;

import edu.uef.oti.mokkikodit.data.Mokki;
import java.util.ArrayList;
import java.util.List;

public class mokkiHallinta {
    private List<Mokki> mokit = new ArrayList<>();
    private int nextID = 1;

    public void onkoVarattu(){
        if (mokit.isEmpty()){
            System.out.println("Ei ole mökkejä varattavissa");
        }
        for(Mokki m : mokit){
            System.out.println("Tässä mökit: " + m);
        }
    }

    public Mokki etsiMokki(int id){
        for (Mokki m : mokit){
            if(m.getMokkiId() == id){
                return m;
            }
        }
        return null;
    }

    public boolean poistaVaraus(int id){
        Mokki m = etsiMokki(id);
        if (m != null){
            mokit.remove(m);
            System.out.println("Mökki ei ole enään varattu");
            return true;

        }
        System.out.println("Mökki on vapaa");
        return false;
    }

}
