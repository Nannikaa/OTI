package edu.uef.oti.mokkikodit.data;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Asiakas {
    private final IntegerProperty asiakasId = new SimpleIntegerProperty();
    private final StringProperty nimi = new SimpleStringProperty();
    private final StringProperty sahkoposti = new SimpleStringProperty();
    private final StringProperty puhelin = new SimpleStringProperty();

    public Asiakas(int asiakasId, String nimi, String sahkoposti, String puhelin) {
        setGuestId(asiakasId);
        setFirstName(nimi);
        setEmail(sahkoposti);
        setPhone(puhelin);
    }

    public IntegerProperty asiakasProperty() {
        return asiakasId;
    }

    public StringProperty nimiProperty() {
        return nimi;
    }
    public StringProperty sahkopostiProperty() {
        return sahkoposti;
    }
    public StringProperty puhelinProperty() {
        return puhelin;
    }

    public int getGuestId() { return asiakasId.get(); }
    public String getFirstName() { return nimi.get(); }
    public String getEmail() { return sahkoposti.get(); }
    public String getPhone() { return puhelin.get(); }

    public void setGuestId(int id) { this.asiakasId.set(id); }
    public void setFirstName(String name) { this.nimi.set(name); }
    public void setEmail(String email) { this.sahkoposti.set(email); }
    public void setPhone(String phone) { this.sahkoposti.set(phone); }
}
