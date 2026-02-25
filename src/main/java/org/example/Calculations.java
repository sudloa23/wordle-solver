package org.example;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class Calculations {
    private List<String> words = new ArrayList<>();
    private HashMap<Character, Letter> letters = new HashMap<>();
    private List<Character> blackLetters = new ArrayList<>();
    private HashMap<Character, Letter> greenLetters = new HashMap<>();
    private HashMap<Character, Letter> yellowLetters = new HashMap<>();
    private List<String> calcList = new ArrayList<>();
    private List<String> allPatterns = new ArrayList<>(243);
    private HashMap<String,Float> entropies = new HashMap<>();
    private HashMap<String, Float> top5 = new HashMap<>();
    private List<String> top5str = new ArrayList<>(5);
    private List<Float> top5flo = new ArrayList<>(5);

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
        System.out.println("romoved all invalid words due to black letters");
        for (int i = 0; i < words.size(); i++){
            String w = words.get(i);

            for (int j = 0; j < blackLetters.size(); j++) {
                if (w.contains(String.valueOf(blackLetters.get(j)))) {

                    words.remove(i);
                    i--;
                    break;
                }
            }
        }
    }

    public void removeYellow() {
        System.out.println("romoved all invalid words due to yellow letters");
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
                words.remove(i);
                i--;
            }
        }
    }

    public void removeGreen(){
        System.out.println("removed all invalid words due to green letters");
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
                words.remove(i);
            }
        }
    }

    public void calculateEntropy(){
        top5str.clear();
        top5flo.clear();
        top5.clear();
        entropies.clear();
        System.out.println("calculateEntropy called");
        float entropy;
        for(int i = 0; i < words.size(); i++){
            entropy = calculateAllBits(words.get(i));
            entropies.put(words.get(i), entropy);
        }
        Map.Entry<String, Float> maxEntry = Collections.max(entropies.entrySet(), Map.Entry.comparingByValue());

        top5 = entropies.entrySet().stream().sorted(Map.Entry.<String, Float>comparingByValue().reversed()).limit(5).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
        for(Map.Entry<String, Float> entry : top5.entrySet()){
            top5str.add(entry.getKey());
            top5flo.add(entry.getValue());
        }
        System.out.println("all entropies calculated");
        System.out.println("highest Entropy: " + maxEntry.getKey() + " - " + maxEntry.getValue());
    }

    public Float calculateAllBits(String guess){
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

    private int[] freq = new int[26];

    private String feedbackPattern(String guess, String answer) {
        char[] g = guess.toCharArray();
        char[] a = answer.toCharArray();
        char[] res = {'B', 'B', 'B', 'B', 'B'};

        for (int i = 0; i < freq.length; i++){
            freq[i] = 0;
        }

        for (int i = 0; i < 5; i++) {
            freq[a[i] - 'A']++;
        }

        for (int i = 0; i < 5; i++) {
            if (g[i] == a[i]) {
                res[i] = 'G';
                freq[g[i] - 'A']--;
            }
        }

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
        System.out.println("removeLetters called");
        removeBlack();
        removeYellow();
        removeGreen();
    }

    public void removeWord(String word){
        System.out.println("removed word: " + word);
        words.remove(word);
    }

    public void draw(Graphics2D g2d){
        g2d.setColor(Color.BLACK);
        g2d.drawString("Entropy:", 750, 150);
        for(int i = 0; i < top5.size(); i++){
            g2d.drawString(String.valueOf(top5str.get(i) + " -- " + top5flo.get(i)), 750, (i*50)+ 200);
        }
    }
}
