package com.example.wordscloud.auth;

import com.example.wordscloud.db.DBConnection;
import com.example.wordscloud.db.DBCreator;
import com.example.wordscloud.db.IDB;
import com.example.wordscloud.main.MainController;
import com.example.wordscloud.models.User;
import com.example.wordscloud.viewwords.ViewWordsController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AuthController {
    @FXML
    private TextField loginField;
    @FXML
    private TextField passwdTextField;
    @FXML
    private Button loginBtn;
    @FXML
    private Button registerBtn;
    private IDB db;

    @FXML
    public void initialize() {
        db = DBCreator.getInstance().GetDBInstance();
        if (db == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("There is no connection to the chosen database. Check your config and try again");
            alert.setTitle("No connection");
            alert.showAndWait();
            close();
        }
    }

    @FXML
    private void onLoginBtnClicked(ActionEvent event) {
        User user = getUser();

        if (db.userExits(user))
            logIn();
        else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Wrong credentials provided");
            alert.setTitle("No connection");
            alert.showAndWait();
        }
    }

    private void logIn() {
        try {
            FXMLLoader loader = new FXMLLoader(MainController.class.getResource("hello-view.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.show();
            close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void onRegisterBtnClicked(ActionEvent event) {
        User user = getUser();
        if (!db.registerUser(user)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Wrong credentials provided");
            alert.setTitle("No connection");
            alert.showAndWait();
        }
        else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Registration successful!");
            alert.setTitle("Message");
            alert.showAndWait();
        }
    }

    private User getUser() {
        return new User(
                loginField.getText(),
                passwdTextField.getText()
        );
    }

    private void close() {
        var stage = (Stage) loginField.getScene().getWindow();
        stage.close();
    }
}
