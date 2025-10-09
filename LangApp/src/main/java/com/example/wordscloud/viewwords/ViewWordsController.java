package com.example.wordscloud.viewwords;

import com.example.wordscloud.db.DBCreator;
import com.example.wordscloud.db.IDB;
import com.example.wordscloud.models.Word;
import com.example.wordscloud.models.WordTableModel;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.BitSet;

public class ViewWordsController {
    @FXML
    private MenuBar menubar;
    @FXML
    private TableView<WordTableModel> wordsTable;

    private ObservableList<WordTableModel> rows;

    @FXML
    public void initialize() {
        rows = wordsTable.getItems();
        wordsTable.setEditable(true);

        addCheckColumn();
        addWordColumn();
        addTranslationColumn();
        loadWords();
    }

    @FXML
    public void onAddNewWordMenuItemClicked(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(ViewWordsController.class.getResource("add-new-word.fxml"));

        Scene scene = new Scene(loader.load(), 640, 480);
        AddNewWordController controller = loader.getController();
        controller.setRows(rows);
        Stage stage = new Stage();
        stage.setTitle("Add new word");
        stage.setScene(scene);
        stage.showAndWait();
    }

    @FXML
    public void onRemoveSelectedWordsMenuItemClicked(ActionEvent event) {
        IDB db = DBCreator.getInstance().GetDBInstance();
        if (db == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("There is no connection to the chosen database. Check your config and try again");
            alert.setTitle("No connection");
            alert.showAndWait();
            return;
        }

        ArrayList<Word> wordsToRemove = new ArrayList<>();
        for (var row : rows) {
            if (row.isChecked())
                wordsToRemove.add(row.getWord());
        }

        BitSet bitSet = db.removeWords(wordsToRemove);
        if (bitSet.nextClearBit(0) == wordsToRemove.size())
            rows.removeIf(WordTableModel::isChecked);
    }

    @FXML
    public void onViewClicked(ActionEvent event) {
        var selectedRows = wordsTable.getSelectionModel().getSelectedCells();
        if (selectedRows.isEmpty())
            return;

        int firstRow = selectedRows.get(0).getRow();
        WordTableModel model = rows.get(firstRow);
        showUpdate(model);
    }

    @FXML
    public void onFindWordBtnClicked(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(UpdateWordController.class.getResource("find-word.fxml"));
            Scene scene = new Scene(loader.load(), 640, 480);
            Stage stage = new Stage();
            stage.setTitle("Find");
            stage.setScene(scene);
            stage.showAndWait();
        } catch (IOException e) {
            System.out.println(e.toString());
        }
    }

    private void showUpdate(WordTableModel model) {
        try {
            FXMLLoader loader = new FXMLLoader(UpdateWordController.class.getResource("view-word.fxml"));
            Scene scene = new Scene(loader.load(), 640, 480);
            var controller = (UpdateWordController) loader.getController();
            controller.setWord(model.getWord());
            Stage stage = new Stage();
            stage.setTitle("View");
            stage.setScene(scene);
            stage.showAndWait();
        } catch (IOException e) {
            System.out.println(e.toString());
        }
    }

    private CheckBoxTableCell<WordTableModel, Boolean> createNewCell() {
        var cell = new CheckBoxTableCell<WordTableModel, Boolean>();
        cell.setSelectedStateCallback(i -> rows.get(i).getCheckedProperty());
        return cell;
    }

    private void addCheckColumn() {
        TableColumn<WordTableModel, Boolean> checkColumn = new TableColumn<>("Check");
        checkColumn.setCellFactory(tc -> createNewCell());
        checkColumn.setCellValueFactory(new PropertyValueFactory<>("checkedProperty"));
        checkColumn.setEditable(true);
        wordsTable.getColumns().add(checkColumn);
    }

    private void addWordColumn() {
        TableColumn<WordTableModel, String> wordsColumn = new TableColumn<>("Word");
        wordsColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getWord().getWord()));
        wordsTable.getColumns().add(wordsColumn);
    }

    private void addTranslationColumn() {
        TableColumn<WordTableModel, String> translationsColumn = new TableColumn<>("Translations");
        translationsColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getWord().getTranslations().get(0)));
        wordsTable.getColumns().add(translationsColumn);
    }

    private void loadWords() {
        IDB db = DBCreator.getInstance().GetDBInstance();
        if (db == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("There is no connection to the chosen database. Check your config and try again");
            alert.setTitle("No connection");
            alert.showAndWait();
            return;
        }

        var words = db.getAllWords();
        for (Word word : words)
            rows.add(new WordTableModel(word));
    }
}
