package com.example.wordscloud.models;

import javafx.beans.property.SimpleBooleanProperty;

public class WordTableModel {
    private final SimpleBooleanProperty checkedProperty = new SimpleBooleanProperty(false);
    private Word word = new Word("");

    public WordTableModel(Word word) {
        this.word = word;
    }

    public boolean isChecked() {
        return checkedProperty.get();
    }

    public void setCheckedProperty(boolean c) {
        checkedProperty.set(c);
    }

    public SimpleBooleanProperty getCheckedProperty() {
        return checkedProperty;
    }

    public Word getWord() {
        return word;
    }

    public void setWord(Word word) {
        this.word = word;
    }
}
