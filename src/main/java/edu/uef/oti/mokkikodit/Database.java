package edu.uef.oti.mokkikodit;

import edu.uef.oti.mokkikodit.data.Asiakas;
import edu.uef.oti.mokkikodit.data.Lasku;
import edu.uef.oti.mokkikodit.data.Mokki;
import edu.uef.oti.mokkikodit.data.Varaus;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Database {
  private static final String DB_URL = "jdbc:sqlite:mokkikodit.db";

  private static Connection connect() {
    try {
      return DriverManager.getConnection(DB_URL);
    } catch (SQLException e) {
      System.out.println("Virhe yhdistettäessä tietokantaan: " + e.getMessage());
      return null;
    }
  }

  public static void createTables() {
    String createAsiakasTable = """
            CREATE TABLE IF NOT EXISTS Asiakas (
                asiakas_id INTEGER PRIMARY KEY AUTOINCREMENT,
                nimi TEXT NOT NULL,
                email TEXT,
                puhelin TEXT
            );
        """;

    String createMokkiTable = """
            CREATE TABLE IF NOT EXISTS Mokki (
                mokki_id INTEGER PRIMARY KEY AUTOINCREMENT,
                nimi TEXT NOT NULL,
                sijainti TEXT NOT NULL,
                hinta_per_yö REAL NOT NULL,
                kuvaus TEXT
            );
        """;

    String createVarausTable = """
            CREATE TABLE IF NOT EXISTS Varaus (
                varaus_id INTEGER PRIMARY KEY AUTOINCREMENT,
                asiakas_id INTEGER,
                mokki_id INTEGER,
                alku_pvm DATE,
                loppu_pvm DATE,
                FOREIGN KEY (asiakas_id) REFERENCES Asiakas(asiakas_id),
                FOREIGN KEY (mokki_id) REFERENCES Mokki(mokki_id)
            );
        """;

    String createLaskuTable = """
            CREATE TABLE IF NOT EXISTS Lasku (
                lasku_id INTEGER PRIMARY KEY AUTOINCREMENT,
                varaus_id INTEGER,
                summa REAL,
                maksettu BOOLEAN,
                maksuPvm DATE,
                FOREIGN KEY (varaus_id) REFERENCES Varaus(varaus_id)
            );
        """;

    try (Connection conn = connect(); Statement stmt = conn.createStatement()) {
      stmt.execute(createAsiakasTable);
      stmt.execute(createMokkiTable);
      stmt.execute(createVarausTable);
      stmt.execute(createLaskuTable);
      System.out.println("Tietokantataulut luotu tai olivat jo olemassa.");
    } catch (SQLException e) {
      System.out.println("Virhe tauluja luotaessa: " + e.getMessage());
    }
  }

  public static boolean addAsiakas(String nimi, String email, String puhelin) {
    String sql = "INSERT INTO Asiakas (nimi, email, puhelin) VALUES (?, ?, ?)";
    try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
      pstmt.setString(1, nimi);
      pstmt.setString(2, email);
      pstmt.setString(3, puhelin);
      pstmt.executeUpdate();
      return true;
    } catch (SQLException e) {
      System.out.println("Virhe asiakasta lisätessä: " + e.getMessage());
      return false;
    }
  }

  public static boolean addMokki(String nimi, String sijainti, double hinta_per_yo, String kuvaus) {
    String sql = "INSERT INTO Mokki (nimi, sijainti, hinta_per_yö, kuvaus) VALUES (?, ?, ?, ?)";
    try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
      pstmt.setString(1, nimi);
      pstmt.setString(2, sijainti);
      pstmt.setDouble(3, hinta_per_yo);
      pstmt.setString(4, kuvaus);
      pstmt.executeUpdate();
      return true;
    } catch (SQLException e) {
      System.out.println("Virhe mökkiä lisätessä: " + e.getMessage());
      return false;
    }
  }

  public static String addVaraus(int asiakas_id, int mokki_id, Date alku_pvm, Date loppu_pvm) {
    if (!isMokkiVapaa(mokki_id, alku_pvm, loppu_pvm)) {
      return "Mökki ei ole vapaa valitulla aikavälillä.";

    }

    if (!doesAsiakasExist(asiakas_id) || !doesMokkiExist(mokki_id)) {
      return ("Asiakasta tai mökkiä ei löydy.");
    }

    String sql =
        "INSERT INTO Varaus (asiakas_id, mokki_id, alku_pvm, loppu_pvm) VALUES (?, ?, ?, ?)";
    try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
      pstmt.setInt(1, asiakas_id);
      pstmt.setInt(2, mokki_id);
      pstmt.setDate(3, alku_pvm);
      pstmt.setDate(4, loppu_pvm);
      pstmt.executeUpdate();
      return null;
    } catch (SQLException e) {
      return "Virhe varausta lisätessä: " + e.getMessage();
    }
  }

  public static boolean doesAsiakasExist(int asiakas_id) {
    String sql = "SELECT COUNT(*) AS count FROM Asiakas WHERE asiakas_id = ?";
    try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
      pstmt.setInt(1, asiakas_id);
      ResultSet rs = pstmt.executeQuery();
      if (rs.next()) {
        return rs.getInt("count") > 0;
      }
    } catch (SQLException e) {
      System.out.println("Virhe tarkistettaessa asiakasta: " + e.getMessage());
    }
    return false;
  }

  public static boolean doesMokkiExist(int mokki_id) {
    String sql = "SELECT COUNT(*) AS count FROM Mokki WHERE mokki_id = ?";
    try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
      pstmt.setInt(1, mokki_id);
      ResultSet rs = pstmt.executeQuery();
      if (rs.next()) {
        return rs.getInt("count") > 0;
      }
    } catch (SQLException e) {
      System.out.println("Virhe tarkistettaessa mökkiä: " + e.getMessage());
    }
    return false;
  }

  public static boolean addLasku(int varaus_id, double summa, boolean maksettu, Date maksuPvm) {
    if (!doesVarausExist(varaus_id)) {
      System.out.println("Varausta ei löydy.");
      return false;
    }
    String sql = "INSERT INTO Lasku (varaus_id, summa, maksettu, maksuPvm) VALUES (?, ?, ?, ?)";
    try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
      pstmt.setInt(1, varaus_id);
      pstmt.setDouble(2, summa);
      pstmt.setBoolean(3, maksettu);
      pstmt.setDate(4, maksuPvm);
      pstmt.executeUpdate();
      return true;
    } catch (SQLException e) {
      System.out.println("Virhe laskua lisätessä: " + e.getMessage());
      return false;
    }
  }

  public static boolean doesVarausExist(int varaus_id) {
    String sql = "SELECT COUNT(*) AS count FROM Varaus WHERE varaus_id = ?";
    try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
      pstmt.setInt(1, varaus_id);
      ResultSet rs = pstmt.executeQuery();
      if (rs.next()) {
        return rs.getInt("count") > 0;
      }
    } catch (SQLException e) {
      System.out.println("Virhe tarkistettaessa varausta: " + e.getMessage());
    }
    return false;
  }

  public static Mokki getMokkiBynimi(String nimi) {
    String sql = "SELECT * FROM Mokki WHERE nimi = ?";
    try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
      pstmt.setString(1, nimi);
      ResultSet rs = pstmt.executeQuery();
      if (rs.next()) {
        return new Mokki(rs.getInt("mokki_id"), rs.getString("nimi"), rs.getString("sijainti"),
            rs.getFloat("hinta_per_yö"), rs.getString("kuvaus"));
      }
    } catch (SQLException e) {
      System.out.println("Virhe mökkiä haettaessa: " + e.getMessage());
    }
    return null;
  }

  public static Asiakas getAsiakasBynimi(String nimi) {
    String sql = "SELECT * FROM Asiakas WHERE nimi = ?";
    try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
      pstmt.setString(1, nimi);
      ResultSet rs = pstmt.executeQuery();
      if (rs.next()) {
        return new Asiakas(rs.getInt("asiakas_id"), rs.getString("nimi"), rs.getString("puhelin"),
            rs.getString("sahkoposti"), rs.getString("osoite"), rs.getString("hlotunnus"),
            rs.getBoolean(7));
      }
    } catch (SQLException e) {
      System.out.println("Virhe asiakasta haettaessa: " + e.getMessage());
    }
    return null;
  }


  public static List<Mokki> getVapaatMokit(Date alku_pvm, Date loppu_pvm) {
    String sql = """
        SELECT * FROM Mokki
        WHERE mokki_id NOT IN (
            SELECT mokki_id FROM Varaus
            WHERE NOT (loppu_pvm < ? OR alku_pvm > ?)
        )
        """;
    try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
      pstmt.setDate(1, alku_pvm);
      pstmt.setDate(2, loppu_pvm);
      ResultSet rs = pstmt.executeQuery();
      List<Mokki> vapaatMokit = new ArrayList<>();
      while (rs.next()) {
        vapaatMokit.add(
            new Mokki(rs.getInt("mokki_id"), rs.getString("nimi"), rs.getString("sijainti"),
                rs.getFloat("hinta_per_yö"), rs.getString("kuvaus")));
      }
      return vapaatMokit;
    } catch (SQLException e) {
      System.out.println("Virhe vapaita mökkejä haettaessa: " + e.getMessage());
    }
    return null;
  }

  public static boolean updateMokki(int mokki_id, String nimi, String sijainti, double hinta_per_yo,
      String kuvaus) {
    String sql =
        "UPDATE Mokki SET nimi = ?, sijainti = ?, hinta_per_yö = ?, kuvaus = ? WHERE mokki_id = ?";
    try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
      pstmt.setString(1, nimi);
      pstmt.setString(2, sijainti);
      pstmt.setDouble(3, hinta_per_yo);
      pstmt.setString(4, kuvaus);
      pstmt.setInt(5, mokki_id);
      pstmt.executeUpdate();
      return true;
    } catch (SQLException e) {
      System.out.println("Virhe mökkiä päivitettäessä: " + e.getMessage());
      return false;
    }
  }

  public static boolean deleteMokki(int mokki_id) {
    String sql = "DELETE FROM Mokki WHERE mokki_id = ?";
    try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
      pstmt.setInt(1, mokki_id);
      pstmt.executeUpdate();
      return true;
    } catch (SQLException e) {
      System.out.println("Virhe mökkiä poistettaessa: " + e.getMessage());
      return false;
    }
  }

  public static boolean deleteVaraus(int varaus_id) {
    String sql = "DELETE FROM Varaus WHERE varaus_id = ?";
    try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
      pstmt.setInt(1, varaus_id);
      pstmt.executeUpdate();
      return true;
    } catch (SQLException e) {
      System.out.println("Virhe varausta poistettaessa: " + e.getMessage());
      return false;
    }
  }

  public static boolean deleteAsiakas(int asiakas_id) {
    String sql = "DELETE FROM Asiakas WHERE asiakas_id = ?";
    try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
      pstmt.setInt(1, asiakas_id);
      pstmt.executeUpdate();
      return true;
    } catch (SQLException e) {
      System.out.println("Virhe asiakasta poistettaessa: " + e.getMessage());
      return false;
    }
  }

  public static boolean deleteLasku(int lasku_id) {
    String sql = "DELETE FROM Lasku WHERE lasku_id = ?";
    try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
      pstmt.setInt(1, lasku_id);
      pstmt.executeUpdate();
      return true;
    } catch (SQLException e) {
      System.out.println("Virhe laskua poistettaessa: " + e.getMessage());
      return false;
    }
  }

  public static boolean isMokkiVapaa(int mokki_id, Date alku_pvm, Date loppu_pvm) {
    String sql = """
            SELECT COUNT(*) AS count FROM Varaus
            WHERE mokki_id = ? AND NOT (loppu_pvm <= ? OR alku_pvm >= ?)
        """;
    try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
      pstmt.setInt(1, mokki_id);
      pstmt.setDate(2, alku_pvm);
      pstmt.setDate(3, loppu_pvm);
      ResultSet rs = pstmt.executeQuery();
      if (rs.next()) {
        return rs.getInt("count") == 0;
      }
    } catch (SQLException e) {
      System.out.println("Virhe tarkistettaessa mökin varaustilannetta: " + e.getMessage());
    }
    return false;
  }

  public static List<Varaus> getAllVaraukset() {
    String sql = "SELECT * FROM Varaus";
    try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
      ResultSet rs = pstmt.executeQuery();
      List<Varaus> varaukset = new ArrayList<>();
      while (rs.next()) {
        varaukset.add(
            new Varaus(rs.getInt("varaus_id"), rs.getInt("asiakas_id"), rs.getInt("mokki_id"),
                rs.getDate("alku_pvm"), rs.getDate("loppu_pvm")));
      }
      return varaukset;
    } catch (SQLException e) {
      System.out.println("Virhe varauksia haettaessa: " + e.getMessage());
    }
    return null;
  }

  public static List<Asiakas> getAllAsiakkaat() {
    String sql = "SELECT * FROM Asiakas";
    try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
      ResultSet rs = pstmt.executeQuery();
      List<Asiakas> asiakkaat = new ArrayList<>();
      while (rs.next()) {
        asiakkaat.add(
            new Asiakas(rs.getInt("asiakas_id"), rs.getString("nimi"), rs.getString("puhelin"),
                rs.getString("email"), null, null, false));
      }
      return asiakkaat;
    } catch (SQLException e) {
      System.out.println("Virhe asiakkaita haettaessa: " + e.getMessage());
    }
    return null;
  }

  public static List<Mokki> getAllMokit() {
    String sql = "SELECT * FROM Mokki";
    try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
      ResultSet rs = pstmt.executeQuery();
      List<Mokki> mokit = new ArrayList<>();
      while (rs.next()) {
        mokit.add(new Mokki(rs.getInt("mokki_id"), rs.getString("nimi"), rs.getString("sijainti"),
            rs.getFloat("hinta_per_yö"), rs.getString("kuvaus")));
      }
      return mokit;
    } catch (SQLException e) {
      System.out.println("Virhe mökkejä haettaessa: " + e.getMessage());
    }
    return null;
  }


  public static List<Varaus> getVarauksetAikavalilta(Date alku_pvm, Date loppu_pvm) {
    String sql = """
            SELECT * FROM Varaus
            WHERE NOT (loppu_pvm < ? OR alku_pvm > ?)
        """;
    try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
      pstmt.setDate(1, alku_pvm);
      pstmt.setDate(2, loppu_pvm);
      ResultSet rs = pstmt.executeQuery();
      List<Varaus> varaukset = new ArrayList<>();
      while (rs.next()) {
        varaukset.add(
            new Varaus(rs.getInt("varaus_id"), rs.getInt("asiakas_id"), rs.getInt("mokki_id"),
                rs.getDate("alku_pvm"), rs.getDate("loppu_pvm")));
      }
      return varaukset;
    } catch (SQLException e) {
      System.out.println("Virhe varauksia haettaessa: " + e.getMessage());
    }
    return null;
  }

  public static List<Lasku> getAllLaskut() {
    String sql = "SELECT * FROM Lasku";
    try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
      ResultSet rs = pstmt.executeQuery();
      List<Lasku> laskut = new ArrayList<>();
      while (rs.next()) {
        laskut.add(new Lasku(rs.getInt("lasku_id"), rs.getInt("varaus_id"), rs.getFloat("summa"),
            rs.getBoolean("maksettu"), rs.getDate("maksuPvm")));
      }
      return laskut;
    } catch (SQLException e) {
      System.out.println("Virhe laskuja haettaessa: " + e.getMessage());
    }
    return null;
  }

  public static boolean markLaskuAsPaid(int lasku_id) {
    String sql = "UPDATE Lasku SET maksettu = 1 WHERE lasku_id = ?";
    try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
      pstmt.setInt(1, lasku_id);
      pstmt.executeUpdate();
      return true;
    } catch (SQLException e) {
      System.out.println("Virhe laskua merkitessä maksetuksi: " + e.getMessage());
      return false;
    }
  }

  public static List<String> getVarauksetRaportti(Date alkuPvm, Date loppuPvm) {
    String sql = """
        SELECT Varaus.varaus_id, Varaus.asiakas_id, Varaus.mokki_id,
               Varaus.alku_pvm, Varaus.loppu_pvm,
               COALESCE(SUM(Lasku.summa), 0) AS total_revenue
        FROM Varaus
        LEFT JOIN Lasku ON Varaus.varaus_id = Lasku.varaus_id
        WHERE NOT (Varaus.loppu_pvm < ? OR Varaus.alku_pvm > ?)
        GROUP BY Varaus.varaus_id, Varaus.asiakas_id, Varaus.mokki_id, Varaus.alku_pvm, Varaus.loppu_pvm
        """;
    List<String> raportti = new ArrayList<>();
    try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
      pstmt.setDate(1, alkuPvm);
      pstmt.setDate(2, loppuPvm);

      ResultSet rs = pstmt.executeQuery();
      double totalRevenue = 0;
      while (rs.next()) {
        int varausId = rs.getInt("varaus_id");
        int asiakasId = rs.getInt("asiakas_id");
        int mokkiId = rs.getInt("mokki_id");
        Date varausAlku = rs.getDate("alku_pvm");
        Date varausLoppu = rs.getDate("loppu_pvm");
        double revenue = rs.getDouble("total_revenue");

        Date effectiveStart = varausAlku.after(alkuPvm) ? varausAlku : alkuPvm;
        Date effectiveEnd = varausLoppu.before(loppuPvm) ? varausLoppu : loppuPvm;

        long days = 0;
        if (!effectiveStart.after(effectiveEnd)) {
          days = (effectiveEnd.getTime() - effectiveStart.getTime()) / (1000 * 60 * 60 * 24);
        }

        totalRevenue += revenue;
        raportti.add(
            "Varaus ID: " + varausId + ", Asiakas ID: " + asiakasId + ", Mökki ID: " + mokkiId + ", Päivät: " + days + ", Tulo: " + revenue + "€");
      }
      raportti.add("Yhteensä tuloja: " + totalRevenue + "€");
    } catch (SQLException e) {
      System.out.println("Virhe raporttia haettaessa: " + e.getMessage());
    }
    return raportti;
  }
}
