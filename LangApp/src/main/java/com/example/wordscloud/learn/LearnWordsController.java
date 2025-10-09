package com.example.wordscloud.learn;

import com.example.wordscloud.db.DBCreator;
import com.example.wordscloud.db.IDB;
import com.example.wordscloud.models.Word;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.event.ActionEvent;
import javafx.scene.layout.Background;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class LearnWordsController {
    @FXML
    private Label wordLabel;
    @FXML
    private Button answer1Btn;
    @FXML
    private Button answer2Btn;
    @FXML
    private Button answer3Btn;
    @FXML
    private Button answer4Btn;

    private int[] wordsIdxs;
    private ArrayList<Word> allWords;
    private int rightBtnNumber = -1;
    private int currentTurn = 0;
    private Button[] answers;
    private int successful = 0;

    @FXML
    public void initialize() {
        IDB db = DBCreator.getInstance().GetDBInstance();
        if (db == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("There is no connection to the chosen database. Check your config and try again");
            alert.setHeaderText("No connection");
            alert.showAndWait();
            return;
        }

        answers = new Button[]{answer1Btn, answer2Btn, answer3Btn, answer4Btn};
        allWords = db.getAllWords();
        Random random = new Random();
        wordsIdxs = new int[(int) Math.ceil((double)allWords.size() / 10)];
        for (int i = 0; i < wordsIdxs.length; ++i)
            wordsIdxs[i] = random.nextInt(allWords.size());
        setTask(wordsIdxs[currentTurn]);
    }

    private void setTask(int wordIdx) {
        Word rightWord = allWords.get(wordIdx);
        wordLabel.setText(rightWord.getWord());

        var btnNumbers = new ArrayList<>(Arrays.asList(0, 1, 2, 3));
        // Set correct meaning
        Random random = new Random();
        rightBtnNumber = random.nextInt(btnNumbers.size());
        getBtn(rightBtnNumber).setText(rightWord.getTranslation(0));
        btnNumbers.remove(btnNumbers.get(rightBtnNumber));

        // Set incorrect meanings
        while (!btnNumbers.isEmpty()) {
            int btnNumberIdx = random.nextInt(btnNumbers.size());
            int wNumber = random.nextInt(allWords.size());
            getBtn(btnNumbers.get(btnNumberIdx)).setText(allWords.get(wNumber).getTranslation(0));
            btnNumbers.remove(btnNumberIdx);
        }
    }

    private Button getBtn(int buttonNumber) {
        return answers[buttonNumber];
    }

    public void onAnswerBtnClicked(ActionEvent event) throws InterruptedException {
        Button clicked = (Button) event.getTarget();
        String answer = clicked.getText();
        Word rightWord = allWords.get(wordsIdxs[currentTurn]);
        String old = clicked.getStyle();
        if (rightWord.getTranslations().contains(answer)) {
            for (Button button : answers)
                button.setStyle("\"-fx-background-color: #ff0000;\"");

            clicked.setStyle("\"-fx-background-color: #00ff00;\"");
            successful++;
        } else {
            answers[rightBtnNumber].setStyle("\"-fx-background-color: #00ff00;\"");
            clicked.setStyle("\"-fx-background-color: #ff0000;\"");
        }

        Thread.sleep(1000, 0);

        for (Button button : answers)
            button.setStyle(old);
        currentTurn++;

        if (currentTurn != wordsIdxs.length)
            setTask(wordsIdxs[currentTurn]);
        else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Done!");
            alert.setHeaderText("You finished! Your score: " + String.valueOf((double)successful / wordsIdxs.length));
            alert.showAndWait();

            var stage = (Stage) clicked.getScene().getWindow();
            stage.close();
        }
    }
}
