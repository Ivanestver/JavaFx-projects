package com.example.wordscloud.viewwords;

import com.example.wordscloud.db.DBCreator;
import com.example.wordscloud.db.IDB;
import com.example.wordscloud.models.Word;
import com.example.wordscloud.models.WordTableModel;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class AddNewWordController {
    @FXML
    private TextField wordTextField;
    @FXML
    private VBox translationsBox;
    @FXML
    private VBox examplesBox;
    @FXML
    private Button addWordBtn;
    @FXML
    private Button cancelBtn;

    private ObservableList<WordTableModel> rows;

    public void setRows(ObservableList<WordTableModel> rows) {
        this.rows = rows;
    }

    @FXML
    public void onAddWordBtnClicked(ActionEvent event) {
        Word word = new Word(wordTextField.getText());

        ObservableList<Node> children = translationsBox.getChildren();
        for (Node child : children) {
            TextField translationField = (TextField) child;
            if (translationField != null) {
                String text = translationField.getText();
                if (!text.isEmpty())
                    word.addTranslation(text);
            }
        }

        children = examplesBox.getChildren();
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
        if (!db.addNewWord(word)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Could not save the word");
            return;
        }

        rows.add(new WordTableModel(word));
        onCancelClicked(event);
    }

    @FXML
    public void onCancelClicked(ActionEvent event) {
        var stage = (Stage) addWordBtn.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void onAddTranslationBtnClicked(ActionEvent event) {
        translationsBox.getChildren().add(new TextField());
    }

    @FXML
    public void onRemoveTranslationBtnClicked(ActionEvent event) {
        var children = translationsBox.getChildren();
        if (children.isEmpty())
            return;

        children.remove(children.size() - 1);
    }

    @FXML
    public void onAddExampleBtnClicked(ActionEvent event) {
        examplesBox.getChildren().add(new TextField());
    }

    @FXML
    public void onRemoveExampleBtnClicked(ActionEvent event) {
        var children = examplesBox.getChildren();
        if (children.isEmpty())
            return;

        children.remove(children.size() - 1);
    }
}
