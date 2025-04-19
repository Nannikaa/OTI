module edu.uef.oti.mokkikodit {
  requires javafx.controls;
  requires javafx.fxml;


  opens edu.uef.oti.mokkikodit to javafx.fxml;
  exports edu.uef.oti.mokkikodit;
}
