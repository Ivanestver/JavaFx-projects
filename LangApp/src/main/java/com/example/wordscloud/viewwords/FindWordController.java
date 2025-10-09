package com.example.wordscloud.viewwords;

import com.example.wordscloud.db.DBCreator;
import com.example.wordscloud.db.IDB;
import com.example.wordscloud.models.Word;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;

import java.util.ArrayList;

public class FindWordController {
    @FXML
    private TextField searchField;
    @FXML
    private ListView<String> wordsList;
    private ArrayList<Word> words = new ArrayList<>();
    private ObservableList<String> rows;

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

        words = db.getAllWords();
        rows = wordsList.getItems();
    }

    @FXML
    public void onKeyTyped(KeyEvent e) {
        rows.clear();
        String request = searchField.getText();
        if (request.isEmpty())
            return;

        for (Word word : words) {
            String w = word.getWord();
            if (w.contains(request))
                rows.add(w);
        }
    }
}
