module edu.uef.oti.mokkikodit {
        requires java.sql;
        requires javafx.base;
        requires javafx.controls;
        requires javafx.fxml;
        requires javafx.graphics;
        requires mysql.connector.j;
        requires org.xerial.sqlitejdbc;
        // Other required modules...


        // Export the package to JavaFX modules
        exports edu.uef.oti.mokkikodit;
        opens edu.uef.oti.mokkikodit.data to javafx.base, javafx.fxml;
        }

