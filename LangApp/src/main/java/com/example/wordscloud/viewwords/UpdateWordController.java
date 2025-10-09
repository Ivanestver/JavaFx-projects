package com.example.wordscloud.viewwords;

import com.example.wordscloud.db.DBCreator;
import com.example.wordscloud.db.IDB;
import com.example.wordscloud.models.Word;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class UpdateWordController {
    @FXML
    private TextField wordTextField1;
    @FXML
    private VBox translationsBox1;
    @FXML
    private VBox examplesBox1;
    @FXML
    private Button addWordBtn1;
    @FXML
    private Button cancelBtn1;

    private Word word;

    public void setWord(Word word) {
        this.word = word;
        wordTextField1.setText(word.getWord());

        var translations = word.getTranslations();
        for (var translation : translations) {
            translationsBox1.getChildren().add(new TextField(translation));
        }

        var examples = word.getExamples();
        for (var example : examples) {
            examplesBox1.getChildren().add(new TextField(example));
        }
    }

    @FXML
    public void onAddWordBtnClicked(ActionEvent event) {
        word.getTranslations().clear();
        ObservableList<Node> children = translationsBox1.getChildren();
        for (Node child : children) {
            TextField translationField = (TextField) child;
            if (translationField != null) {
                String text = translationField.getText();
                if (!text.isEmpty())
                    word.addTranslation(text);
            }
        }

        word.getExamples().clear();
        children = examplesBox1.getChildren();
        for (Node child : children) {
            TextField examplesField = (TextField) child;
            if (examplesField != null) {
                String text = examplesField.getText();
                if (!text.isEmpty())
                    word.getExamples().add(text);
            }
        }

        IDB db = DBCreator.getInstance().GetDBInstance();
        if (db == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("There is no connection to the chosen database. Check your config and try again");
            alert.setHeaderText("No connection");
            alert.showAndWait();
            return;
        }
        db.updateWord(word);
        onCancelClicked(event);
    }

    @FXML
    public void onCancelClicked(ActionEvent event) {
        var stage = (Stage) addWordBtn1.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void onAddTranslationBtnClicked(ActionEvent event) {
        translationsBox1.getChildren().add(new TextField());
    }

    @FXML
    public void onRemoveTranslationBtnClicked(ActionEvent event) {
        var children = translationsBox1.getChildren();
        if (children.isEmpty())
            return;

        children.remove(children.size() - 1);
    }

    @FXML
    public void onAddExampleBtnClicked(ActionEvent event) {
        examplesBox1.getChildren().add(new TextField());
    }

    @FXML
    public void onRemoveExampleBtnClicked(ActionEvent event) {
        var children = examplesBox1.getChildren();
        if (children.isEmpty())
            return;

        children.remove(children.size() - 1);
    }
}
