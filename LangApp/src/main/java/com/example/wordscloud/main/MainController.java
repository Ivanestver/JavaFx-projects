package com.example.wordscloud.main;

import com.example.wordscloud.learn.LearnWordsController;
import com.example.wordscloud.viewwords.ViewWordsController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class MainController {
    @FXML
    private Button viewWordsBtn;
    @FXML
    private Button learnBtn;
    @FXML
    public void onViewWordsBtnClicked(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(ViewWordsController.class.getResource("view-words.fxml"));
        Scene scene = new Scene(loader.load());
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    public void onLearnBtnClicked(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(LearnWordsController.class.getResource("learn-words.fxml"));
        Scene scene = new Scene(loader.load());
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();
    }
}
