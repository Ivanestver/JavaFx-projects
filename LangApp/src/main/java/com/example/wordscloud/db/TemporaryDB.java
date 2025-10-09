package com.example.wordscloud.db;

import com.example.wordscloud.models.User;
import com.example.wordscloud.models.Word;

import java.util.ArrayList;
import java.util.BitSet;

public class TemporaryDB implements IDB {
    private ArrayList<Word> words = new ArrayList<>();
    private ArrayList<User> users = new ArrayList<>();

    @Override
    public ArrayList<Word> getAllWords() {
        return words;
    }

    @Override
    public boolean addNewWord(Word newWord) {
        if (words.contains(newWord))
            return false;
        words.add(new Word(newWord));
        return true;
    }

    @Override
    public BitSet addNewWords(ArrayList<Word> newWords) {
        BitSet bitset = new BitSet(newWords.size());
        for (int i = 0; i < bitset.size(); ++i) {
            Word w = newWords.get(i);
            boolean notContains = !words.contains(w);
            if (notContains)
                words.add(new Word(w));
            bitset.set(i, notContains);
        }

        return bitset;
    }

    @Override
    public boolean removeWord(Word word) {
        return words.remove(word);
    }

    @Override
    public BitSet removeWords(ArrayList<Word> wordsToRemove) {
        BitSet bitset = new BitSet(wordsToRemove.size());
        for (int i = 0; i < wordsToRemove.size(); ++i)
            bitset.set(i, removeWord(wordsToRemove.get(i)));
        return bitset;
    }

    @Override
    public boolean updateWord(Word word) {
        int idx = words.indexOf(word);
        if (idx < 0)
            return false;

        words.set(idx, new Word(word));

        return true;
    }

    @Override
    public BitSet updateWords(ArrayList<Word> wordsToUpdate) {
        BitSet bitset = new BitSet(wordsToUpdate.size());
        for (int i = 0; i < wordsToUpdate.size(); ++i)
            bitset.set(i, updateWord(wordsToUpdate.get(i)));
        return bitset;
    }

    @Override
    public boolean wordExists(Word word) {
        return words.contains(word);
    }

    @Override
    public BitSet wordsExists(ArrayList<Word> wordsToCheck) {
        BitSet bitset = new BitSet(wordsToCheck.size());
        for (int i = 0; i < wordsToCheck.size(); ++i)
            bitset.set(i, wordExists(wordsToCheck.get(i)));
        return bitset;
    }

    @Override
    public boolean userExits(User user) {
        return users.contains(user);
    }

    @Override
    public boolean registerUser(User user) {
        if (userExits(user))
            return false;
        users.add(new User(user.getLogin(), user.getPassword()));
        return true;
    }
}
