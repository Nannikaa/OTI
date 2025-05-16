module edu.uef.oti.mokkikodit {
  requires javafx.controls;
  requires javafx.fxml;
  requires java.sql;
    requires org.xerial.sqlitejdbc;
    requires jdk.jdi;

    opens edu.uef.oti.mokkikodit to javafx.fxml;
  exports edu.uef.oti.mokkikodit;
}
