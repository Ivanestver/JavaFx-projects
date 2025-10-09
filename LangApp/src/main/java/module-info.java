module com.example.wordscloud {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires javafx.graphics;
    requires javafx.base;
    requires java.prefs;
    requires java.xml;
    requires java.sql;
    requires java.desktop;

    exports com.example.wordscloud.main;
    exports com.example.wordscloud.viewwords;
    exports com.example.wordscloud.models;
    exports com.example.wordscloud.learn;
    exports com.example.wordscloud.auth;
    opens com.example.wordscloud.main to javafx.fxml;
    opens com.example.wordscloud.viewwords to javafx.fxml;
    opens com.example.wordscloud.models to javafx.fxml;
    opens com.example.wordscloud.learn to javafx.fxml;
    opens com.example.wordscloud.auth to javafx.fxml;
}