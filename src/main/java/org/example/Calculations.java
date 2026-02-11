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

    public void updateLetters(Letter letter){

    }

    public void updateWords(){

    }

    public void calculateBit(){

    }

    public void calculateProbability(){

    }
}
