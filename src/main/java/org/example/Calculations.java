package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Calculations {
    private List<String> words = new ArrayList<>();
    private HashMap<Character, Letter> letters = new HashMap<>();
    private List<Character> blackLetters = new ArrayList<>();
    private HashMap<Character, Letter> greenLetters = new HashMap<>();
    private HashMap<Character, Letter> yellowLetters = new HashMap<>();

    public Calculations(){
        InputStream is = Calculations.class.getResourceAsStream("/possible_guesses.txt");
        Path path = Paths.get("C:\\Users\\loren\\OneDrive\\Dokumente\\wordle\\src\\main\\resources\\possible_guesses.txt");

        try{
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line;
            while((line = br.readLine()) != null){
                words.add(line);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        for(char ch = 'A'; ch <= 'Z'; ch++){
            letters.put(ch, new Letter(ch));
        }
    }

    public void updateLetters(HashMap<Integer, Letter> gameLetters){
        for(int i = 0; i < gameLetters.size(); i++){
            letters.put(gameLetters.get(i).getLetter(), gameLetters.get(i));

            if(letters.get(gameLetters.get(i).getLetter()).isBlack()){
                blackLetters.add(gameLetters.get(i).getLetter());
            }

            if(letters.get(gameLetters.get(i).getLetter()).getGreenPos() != null){
                greenLetters.put(gameLetters.get(i).getLetter(), gameLetters.get(i));
            }

            if(letters.get(gameLetters.get(i).getLetter()).getYellowPos() != null){
                yellowLetters.put(gameLetters.get(i).getLetter(), gameLetters.get(i));
            }
        }
    }

    public void removeBlack(){
        for(int i = 0; i < words.size(); i++){
            for(int j = i + 1; j < blackLetters.size(); j++){
                if(words.get(i).contains(String.valueOf(blackLetters.get(j)))){
                    words.remove(i);
                    System.out.println("word: " + words.get(i) + " removed");
                }
            }
        }
    }

    public void removeYellow(){

    }

    public void calculateBit(){

    }

    public void calculateProbability(){

    }

    public boolean yellowCheck(List<Integer> positions, char[] word){
        for(int i = 0; i < yellowLetters.size(); i++){
            if(word[positions.get(i)] == yellowLetters.get(i).getLetter()){
                return true;
            }
        }

        return false;
    }

    public void removeLetters(){
        removeBlack();
    }
}
