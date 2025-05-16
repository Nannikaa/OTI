package edu.uef.oti.mokkikodit;
/*
* */

import edu.uef.oti.mokkikodit.data.Lasku;
import edu.uef.oti.mokkikodit.data.Varaus;

import java.util.List;

import static edu.uef.oti.mokkikodit.Database.getAllLaskut;
import static edu.uef.oti.mokkikodit.Database.getAllVaraukset;

public class Raportti {

    float summa_total = 0;
    int varausten_maara_total = 0;

    public void getRaportti(){

        List<Lasku> laskut = getAllLaskut();

        for ( Lasku l : laskut ){

           summa_total += l.getSumma();

        }

        List<Varaus> varaukset = getAllVaraukset();

        if( varaukset.isEmpty() ){

            System.out.println("Ei dataa");

        }

        varausten_maara_total = varaukset.size();

        System.out.println("Raportti");
        System.out.println(" brutto tulot: "+ summa_total);
        System.out.println(" varauksien määrä: "+ varausten_maara_total);

    }

}
