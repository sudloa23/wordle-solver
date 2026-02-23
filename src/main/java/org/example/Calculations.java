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
    private List<String> calcList = new ArrayList<>();

    public Calculations(){
        InputStream is = Calculations.class.getResourceAsStream("/possible_guesses.txt");
        Path path = Paths.get("C:\\Users\\loren\\OneDrive\\Dokumente\\wordle\\src\\main\\resources\\possible_guesses.txt");

        try{
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line;
            while((line = br.readLine()) != null){
                words.add(line.toUpperCase());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        for(char ch = 'A'; ch <= 'Z'; ch++){
            letters.put(ch, new Letter(ch));
        }
        calcList.addAll(words);
    }

    public void updateLetters(HashMap<Integer, Letter> gameLetters){
        // optional: Listen/Maps leeren, sonst sammelst du Müll aus alten Runden
        blackLetters.clear();
        greenLetters.clear();
        yellowLetters.clear();


        for (Letter gl : gameLetters.values()) {
            letters.put(gl.getLetter(), gl);

            if (gl.isBlack()) blackLetters.add(gl.getLetter());
            if (gl.getGreenPos() != null && !gl.getGreenPos().isEmpty()) greenLetters.put(gl.getLetter(), gl);
            if (gl.getYellowPos() != null && !gl.getYellowPos().isEmpty()) yellowLetters.put(gl.getLetter(), gl);
        }
    }


    public void removeBlack() {
        for (int i = 0; i < words.size(); i++){
            String w = words.get(i);

            for (int j = 0; j < blackLetters.size(); j++) {
                if (w.contains(String.valueOf(blackLetters.get(j)))) {
                    System.out.println("removed black: " + w);
                    words.remove(i);
                    i--;
                    break;
                }
            }
        }
    }

    public void removeYellow() {
        for (int i = 0; i < words.size(); i++) {
            String w = words.get(i);
            boolean remove = false;
            char letter = '#';

            for (Letter l : yellowLetters.values()) {
                if (yellowCheck(w.toCharArray(), l)) {
                    letter = l.getLetter();
                    remove = true;
                    break;
                }else if(!w.contains(String.valueOf(yellowLetters.get(l.getLetter()).getLetter()))){
                    letter = l.getLetter();
                    remove = true;
                    break;
                }
            }

            if(remove){
                System.out.println("removed yellow "+ letter + ": " + w);
                words.remove(i);
                i--;
            }
        }
    }

    public void removeGreen(){
        for (int i = 0; i < words.size(); i++) {
            String w = words.get(i);
            boolean remove = false;

            for (Letter l : greenLetters.values()) {
                if(!w.contains(String.valueOf(greenLetters.get(l.getLetter()).getLetter()))){
                    remove = true;
                    break;
                }
            }

            if(remove){
                System.out.println("remove green: " + w);
                words.remove(i);
            }
        }
    }


    public void calculateEntropy(){

        for(int i = 0; i < calcList.size(); i++){
            calcList.clear();
            calcList.addAll(words);


        }
    }

    public Float calculateAllBits(String word){
        char[] wordChars = word.toCharArray();
        List<Float> bitList = new ArrayList<>();
        float sum = 0.0f;
        float entropy;
        double probability;


        for(int i = 0; i < Math.pow(3, 5); i++){


            probability = (double) calcList.size() / words.size();
            bitList.add((float) (-1 * (Math.log(probability) / Math.log(2))));
        }

        for(float num : bitList){
            sum += num;
        }
        entropy = sum / (float) bitList.size();

        return entropy;
    }

    public void calculateProbability(){

    }

    public boolean yellowCheck(char[] word, Letter letter){
        for(int i = 0; i < letter.getYellowPos().size(); i++){
            if(word[letter.getYellowPos().get(i)] == letter.getLetter()){
                return true;
            }
        }

        return false;
    }

    public void removeLetters(){
        removeBlack();
        removeYellow();
        removeGreen();
    }
}
