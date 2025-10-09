package com.example.wordscloud.db;

import com.example.wordscloud.models.IDBPrefs;
import com.example.wordscloud.models.User;
import com.example.wordscloud.models.Word;
import javafx.util.Pair;

import java.sql.*;
import java.util.*;

public class SQLDB implements IDB {
    private final DBConnection connection;

    private final String WORD_TABLE = "Word";
    private final String WORD_COLUMN = "word";
    private final String TRANSLATION_TABLE = "Translation";
    private final String TRANSLATION_COLUMN = "translation";
    private final String EXAMPLE_TABLE = "Example";
    private final String EXAMPLE_COLUMN = "example";
    private final String USER_TABLE = "User";
    private final String LOGIN_COLUMN = "login";
    private final String PASSWORD_COLUMN = "passwd";

    public SQLDB(IDBPrefs prefs) {
        connection = new DBConnection(prefs);
    }

    @Override
    public ArrayList<Word> getAllWords() {
        var translations = getDataOfWord(getSelectWordsAndTranslations());
        if (translations == null)
            return null;
        var examples = getDataOfWord(getSelectWordsAndExamples());
        if (examples == null)
            return null;
        HashMap<String, Pair<ArrayList<String>, ArrayList<String>>> m = new HashMap<>();
        for (var word : translations.keySet()) {
            if (!m.containsKey(word))
                m.put(word, new Pair<>(new ArrayList<>(), new ArrayList<>()));
            var p = m.get(word);
            for (String translation : translations.get(word))
                p.getKey().add(translation);
        }
        for (var word : examples.keySet()) {
            if (!m.containsKey(word))
                m.put(word, new Pair<>(new ArrayList<>(), new ArrayList<>()));
            var p = m.get(word);
            for (String example : examples.get(word))
                p.getValue().add(example);
        }

        ArrayList<Word> words = new ArrayList<>();
        for (var w : m.keySet()) {
            Word word = new Word(w);
            var p = m.get(w);
            word.getTranslations().addAll(p.getKey());
            word.getExamples().addAll(p.getValue());
            words.add(word);
        }
        return words;
    }

    private HashMap<String, ArrayList<String>> getDataOfWord(String query) {
        try (Connection conn = connection.getConnection()) {
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            HashMap<String, ArrayList<String>> m = new HashMap<>();
            while (resultSet.next()) {
                String word = resultSet.getString(1);
                if (!m.containsKey(word))
                    m.put(word, new ArrayList<>());
                var translations = m.get(word);
                String translation = resultSet.getString(2);
                translations.add(translation);
            }
            return m;
        } catch (Exception e) {
            return null;
        }
    }

    private String getSelectWordsAndTranslations() {
        return "SELECT " + WORD_COLUMN + ", " + TRANSLATION_COLUMN +
                " FROM " + TRANSLATION_TABLE +
                " ORDER BY " + WORD_TABLE;
    }

    private String getSelectWordsAndExamples() {
        return "SELECT " + WORD_COLUMN + ", " + EXAMPLE_COLUMN +
                " FROM " + EXAMPLE_TABLE +
                " ORDER BY " + WORD_TABLE;
    }

    @Override
    public boolean addNewWord(Word newWord) {
        BitSet bitSet = addNewWords(new ArrayList<>(Collections.singletonList(newWord)));
        return !bitSet.isEmpty() && bitSet.get(0);
    }

    @Override
    public BitSet addNewWords(ArrayList<Word> newWords) {
        try (Connection conn = connection.getConnection()) {
            BitSet bitSet = addWords(conn, newWords);
            if (bitSet.nextClearBit(0) != newWords.size())
                return bitSet;

            bitSet = addWordsInfo(conn, TRANSLATION_TABLE, newWords);
            if (bitSet.nextClearBit(0) != newWords.size())
                return bitSet;

            return addWordsInfo(conn, EXAMPLE_TABLE, newWords);
        }
        catch (Exception e) {
            return new BitSet(newWords.size());
        }
    }

    private BitSet addWords(Connection conn, ArrayList<Word> newWords) throws SQLException {
        // firstly, add words
        PreparedStatement statement = conn.prepareStatement(getAddNewWordsStatement(newWords.size()));
        for (int i = 0; i < newWords.size(); ++i) {
            statement.setString(i+1, newWords.get(i).getWord());
        }
        int rowsAffected = statement.executeUpdate();
        BitSet bitSet = new BitSet(newWords.size());
        bitSet.set(0, rowsAffected, true);
        return bitSet;
    }

    private String getAddNewWordsStatement(int wordsCount) {
        return "INSERT INTO " + WORD_TABLE + " VALUES " +
                "(?), ".repeat(Math.max(0, wordsCount - 1)) +
                "(?);";
    }

    private BitSet addWordsInfo(Connection conn, String tableName, ArrayList<Word> newWords) throws SQLException {
        BitSet bitSet = new BitSet(newWords.size());
        for (int i = 0; i < newWords.size(); ++i) {
            Word w = newWords.get(i);
            PreparedStatement statement = conn.prepareStatement(getAddNewWordInfoStatement(tableName, newWords.size()));
            int n = 0;
            var translations = getWordInfo(w, tableName);
            for (String translation : translations) {
                statement.setString(++n, w.getWord());
                statement.setString(++n, translation);
            }
            int rowsAffected = statement.executeUpdate();
            bitSet.set(i, rowsAffected == translations.size());
        }
        return bitSet;
    }

    private String getAddNewWordInfoStatement(String tableName, int translationsCount) {
        return "INSERT INTO " + tableName + " VALUES " +
                "(?, ?), ".repeat(Math.max(0, translationsCount - 1)) +
                "(?, ?);";
    }

    private ArrayList<String> getWordInfo(Word w, String tableName) {
        if (tableName.equals(TRANSLATION_TABLE))
            return w.getTranslations();
        else if (tableName.equals(EXAMPLE_TABLE))
            return w.getExamples();
        else
            return new ArrayList<>();
    }

    @Override
    public boolean removeWord(Word word) {
        BitSet bitSet = removeWords(new ArrayList<>(Collections.singletonList(word)));
        return !bitSet.isEmpty() && bitSet.get(0);
    }

    @Override
    public BitSet removeWords(ArrayList<Word> words) {
        try (Connection conn = connection.getConnection()) {
            conn.setAutoCommit(false);
            BitSet bitSet = removeInfo(conn, TRANSLATION_TABLE, words);
            if (bitSet.nextClearBit(0) != words.size()) {
                conn.rollback();
                return bitSet;
            }

            bitSet = removeInfo(conn, EXAMPLE_TABLE, words);
            if (bitSet.nextClearBit(0) != words.size()) {
                conn.rollback();
                return bitSet;
            }

            bitSet = removeInfo(conn, WORD_TABLE, words);
            if (bitSet.nextClearBit(0) != words.size())
                conn.rollback();

            conn.commit();
            return bitSet;
        }
        catch (Exception e) {
            return new BitSet(words.size());
        }
    }

    private BitSet removeInfo(Connection conn, String tableName, ArrayList<Word> words) throws SQLException {
        PreparedStatement statement = conn.prepareStatement(getRemoveFromWordInfoTableStatement(tableName, words.size()));
        for (int i = 0; i < words.size(); ++i) {
            Word w = words.get(i);
            statement.setString(i+1, w.getWord());
        }
        int rowsAffected = statement.executeUpdate();
        BitSet bitSet = new BitSet(words.size());
        bitSet.set(0, rowsAffected, true);
        return bitSet;
    }

    private String getRemoveFromWordInfoTableStatement(String tableName, int wordsCount) {
        return "DELETE FROM " + tableName + " WHERE " + WORD_COLUMN + " IN (" +
                "?, ".repeat(wordsCount - 1) + "?);";
    }

    @Override
    public boolean updateWord(Word word) {
        if (!removeWord(word))
            return false;
        return addNewWord(word);
    }

    @Override
    public BitSet updateWords(ArrayList<Word> words) {
        BitSet bitSet = removeWords(words);
        if (bitSet.nextClearBit(0) != words.size())
            return bitSet;

        return addNewWords(words);
    }

    @Override
    public boolean wordExists(Word word) {
        ArrayList<Word> allWords = getAllWords();
        return allWords.contains(word);
    }

    @Override
    public BitSet wordsExists(ArrayList<Word> words) {
        ArrayList<Word> allWords = getAllWords();
        BitSet bitSet = new BitSet(words.size());
        for (int i = 0; i < bitSet.size(); ++i)
            bitSet.set(i, allWords.contains(words.get(i)));

        return bitSet;
    }

    @Override
    public boolean userExits(User user) {
        try (Connection conn = connection.getConnection()) {
            PreparedStatement statement = conn.prepareStatement(getSelectUser());
            statement.setString(1, user.getLogin());
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next();
        }
        catch (Exception e) {
            return false;
        }
    }

    private String getSelectUser() {
        return "SELECT * FROM " + USER_TABLE + " WHERE " + LOGIN_COLUMN + " = ?;";
    }

    @Override
    public boolean registerUser(User user) {
        try (Connection conn = connection.getConnection()) {
            PreparedStatement statement = conn.prepareStatement(createInsertUser());
            statement.setString(1, user.getLogin());
            statement.setString(2, user.getPassword());
            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        }
        catch (Exception e) {
            return false;
        }
    }

    private String createInsertUser() {
        return "INSERT INTO " + USER_TABLE + " VALUES (?, ?);";
    }
}
