package edu.uef.oti.mokkikodit;

import edu.uef.oti.mokkikodit.data.Asiakas;
import edu.uef.oti.mokkikodit.data.YhdistysTietokantaan;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AsiakasHallinta {
    public List<Asiakas> getAllAsiakas() {
        List<Asiakas> asiakkaat = new ArrayList<>();
        String query = "SELECT * FROM Asiakas";

        try (Connection conn = YhdistysTietokantaan.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Asiakas asiakas = new Asiakas(
                        rs.getInt("asiakas_id"),
                        rs.getString("nimi"),
                        rs.getString("email"),
                        rs.getString("puhelin")
                );
                asiakkaat.add(asiakas);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return asiakkaat;
    }
    public int lisaaAsiakas(Asiakas asiakas) {
        String query = "INSERT INTO guests (first_name, last_name, email, phone, address) VALUES (?, ?, ?, ?, ?)";
        int generatedId = -1;

        try (Connection conn = YhdistysTietokantaan.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, asiakas.getFirstName());
            pstmt.setString(3, asiakas.getEmail());
            pstmt.setString(4, asiakas.getPhone());
            pstmt.executeUpdate();

            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    generatedId = rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return generatedId;
    }

}
