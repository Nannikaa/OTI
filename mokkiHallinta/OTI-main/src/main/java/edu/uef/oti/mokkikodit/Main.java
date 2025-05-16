package edu.uef.oti.mokkikodit;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        AsiakasHallinta ahallinta = new AsiakasHallinta();
        LaskujenHallinta lhallinta = new LaskujenHallinta();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("-------------------");
            System.out.println("Päävalikko");
            System.out.println("Valitse numerolla tehtävä");
            System.out.println("\n1. Lisää asiakas\n2. Lista asiakkaista\n3. Päivitä asiakastietoja\n4. Poista asiakkaan tiedot\n5. Poistu");
            int valinta = Integer.parseInt(scanner.nextLine());

            switch (valinta) {
                case 1:
                    System.out.println("Nimi: ");
                    String nimi = scanner.nextLine();
                    System.out.println("Puhelinnumero: ");
                    String puhelinro = scanner.nextLine();
                    System.out.println("Sähköposti: ");
                    String sahkoposti = scanner.nextLine();
                    System.out.println("Osoite: ");
                    String osoite = scanner.nextLine();
                    System.out.println("Henkilötunnus: ");
                    String hlotunnus = scanner.nextLine();
                    System.out.println("Onko asiakas yritys? (true/false): ");
                    boolean onkoYritys = Boolean.parseBoolean(scanner.nextLine());
                    ahallinta.lisaaAsiakas(nimi, puhelinro, sahkoposti, osoite, hlotunnus, onkoYritys);
                    break;

                case 2:
                    ahallinta.listAsiakas();
                    break;

                case 3:
                    System.out.println("Asiakas ID jota haluat päivittää: ");
                    int paivitaId = Integer.parseInt(scanner.nextLine());
                    System.out.println("Uusi nimi: ");
                    String newNimi = scanner.nextLine();
                    System.out.println("Uusi puhelinnumero: ");
                    String newPuhelinro = scanner.nextLine();
                    System.out.println("Uusi sähköpostiosoite: ");
                    String newSahkoposti = scanner.nextLine();
                    System.out.println("Uusi osoite: ");
                    String newOsoite = scanner.nextLine();
                    System.out.println("Uusi henkilötunnus: ");
                    String newHlotunnus = scanner.nextLine();
                    System.out.println("Onko asiakas yritys (true/false): ");
                    boolean onkoYritysuusi = Boolean.parseBoolean(scanner.nextLine());
                    ahallinta.paivitaAsiakas(paivitaId, newNimi, newPuhelinro, newSahkoposti, newOsoite, newHlotunnus, onkoYritysuusi);
                    break;

                case 4:
                    System.out.println("Asiakas ID jonka haluat poistaa: ");
                    int poistaId = Integer.parseInt(scanner.nextLine());
                    ahallinta.poistaAsiakas(poistaId);
                    break;


                case 5:
                    System.out.println("Poistutaan...");
                    scanner.close();
                    return;

                default:
                    System.out.println("Väärä toiminto.");

            }
        }

    }
}
