package com.example.wordscloud.db;

import com.example.wordscloud.models.User;
import com.example.wordscloud.models.Word;

import java.util.ArrayList;
import java.util.BitSet;

public interface IDB {
    ArrayList<Word> getAllWords();

    boolean addNewWord(Word newWord);
    BitSet addNewWords(ArrayList<Word> newWords);

    boolean removeWord(Word word);
    BitSet removeWords(ArrayList<Word> words);

    boolean updateWord(Word word);
    BitSet updateWords(ArrayList<Word> words);

    boolean wordExists(Word word);
    BitSet wordsExists(ArrayList<Word> word);

    boolean userExits(User user);
    boolean registerUser(User user);
}
