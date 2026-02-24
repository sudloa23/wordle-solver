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
    private List<String> allPatterns = new ArrayList<>(243);

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
        char[] states = {'B', 'Y', 'G'};

        for (int n = 0; n < Math.pow(3, 5); n++) { // 3^5
            int x = n;
            char[] pat = new char[5];

            for (int i = 4; i >= 0; i--) {
                pat[i] = states[x % 3];
                x /= 3;
            }
            allPatterns.add(new String(pat));
        }
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
        for(int i = 0; i < words.size(); i++){
            calculateAllBits(words.get(i));
            System.out.println("entropy of " + words.get(i) + ": ");
        }
    }

    public Float calculateAllBits(String guess) {
        if (words == null || words.isEmpty()) return 0.0f;

        HashMap<String, Integer> patternCounts = new HashMap<>();

        for (String candidate : words) {
            String pattern = feedbackPattern(guess, candidate);
            patternCounts.put(pattern, patternCounts.getOrDefault(pattern, 0) + 1);
        }

        double entropy = 0.0;
        int total = words.size();

        for (int count : patternCounts.values()) {
            double p = (double) count / total;
            entropy += -p * (Math.log(p) / Math.log(2));
        }

        return (float) entropy;
    }

    private String feedbackPattern(String guess, String answer) {
        char[] g = guess.toCharArray();
        char[] a = answer.toCharArray();
        char[] res = {'B', 'B', 'B', 'B', 'B'};

        int[] freq = new int[26];
        for (int i = 0; i < 5; i++) {
            freq[a[i] - 'A']++;
        }

        // Greens
        for (int i = 0; i < 5; i++) {
            if (g[i] == a[i]) {
                res[i] = 'G';
                freq[g[i] - 'A']--;
            }
        }

        // Yellows
        for (int i = 0; i < 5; i++) {
            if (res[i] == 'G') continue;
            int idx = g[i] - 'A';
            if (idx >= 0 && idx < 26 && freq[idx] > 0) {
                res[i] = 'Y';
                freq[idx]--;
            }
        }

        return new String(res);
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
