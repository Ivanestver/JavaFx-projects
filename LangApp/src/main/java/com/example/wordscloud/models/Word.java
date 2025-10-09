package com.example.wordscloud.models;

import java.util.ArrayList;

public class Word {
    private String word;
    private ArrayList<String> translations = new ArrayList<>();

    private ArrayList<String> examples = new ArrayList<>();

    public Word(String word) {
        this.word = word;
    }

    public Word(Word other) {
        this.word = other.word;
        this.translations.addAll(other.translations);
        this.examples.addAll(other.examples);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(this.word);
        builder.append("\n(translations): ");
        for (String translation : translations) {
            builder.append(translation);
            builder.append("; ");
        }
        builder.append("\n(examples): ");
        for (String example : examples) {
            builder.append(example);
            builder.append("; ");
        }
        return builder.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Word))
            return false;

        if (this == obj)
            return true;

        return this.word.equals(((Word)obj).word);
    }

    @Override
    public int hashCode() {
        return word.hashCode();
    }

    public String getWord() {
        return word;
    }

    public ArrayList<String> getTranslations() {
        return translations;
    }

    public String getTranslation(int idx) {
        return idx < translations.size() ? translations.get(idx) : "";
    }

    public void addTranslation(String translation) {
        translations.add(translation);
    }

    public ArrayList<String> getExamples() {
        return examples;
    }
}
