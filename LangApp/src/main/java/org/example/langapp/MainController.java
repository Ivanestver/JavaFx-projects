package org.example.langapp;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainController {
    @FXML
    protected void onAddNewWordBtnClicked(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("add-new-word.fxml"));
        Scene scene = new Scene(loader.load(), 640, 480);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();
    }
}