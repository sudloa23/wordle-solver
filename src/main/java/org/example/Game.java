package org.example;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Game{
    private char[] word;
    private List<Cell> cells = new ArrayList<>();
    private List<String> allWords = new ArrayList<String>();
    private int cellAmount =25;
    private int currentCellIndex = 0;
    private List<String> possibleWords = new ArrayList<>();
    private int fourCounter = 1;
    private HashMap<Integer, Letter> letters = new HashMap<>();

    public Game(){
        initGame();
    }

    public void initGame(){
        InputStream is = Game.class.getResourceAsStream("/words.txt");
        Path path = Paths.get("C:\\Users\\loren\\OneDrive\\Dokumente\\wordle\\src\\main\\resources\\answers.txt");
        try {
            BufferedReader br = Files.newBufferedReader(path);
            String line;
            while((line = br.readLine()) != null){
                allWords.add(line);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        InputStream is2 = Game.class.getResourceAsStream("/possible_guesses.txt");
        Path path2 = Paths.get("C:\\Users\\loren\\OneDrive\\Dokumente\\wordle\\src\\main\\resources\\possible_guesses.txt");
        try{
            BufferedReader br = new BufferedReader(new InputStreamReader(is2));
            String line;
            while((line = br.readLine()) != null){
                possibleWords.add(line.toUpperCase());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        word = allWords.get(ThreadLocalRandom.current().nextInt(1, 1692)).toCharArray();

        cells = new ArrayList<>();
        for(int i = 0; i < cellAmount; i++){
            cells.add(new Cell(word[i % 5], i));
        }

        System.out.println("word: " + Arrays.toString(word));
    }

    public void draw(Graphics2D g2d){
        for(int i = 0; i < cells.size(); i++){
            cells.get(i).draw(g2d);
        }
    }

    public void update(){

    }

    public void handleKey(KeyEvent e){
        if(e.getKeyCode() == KeyEvent.VK_BACK_SPACE){

            if(currentCellIndex % 5 == 0){
                return;
            }

            System.out.println("delete slot -> " + currentCellIndex%5);
            if(currentCellIndex % 5 == 4 && fourCounter == 1){
                cells.get(currentCellIndex).update(e);
                fourCounter = 0;
                return;
            }
            if (currentCellIndex > 0){
                currentCellIndex--;
                cells.get(currentCellIndex).update(e);
                fourCounter = 1;
            } else if(currentCellIndex == 0){
                cells.get(0).update(e);
            }
            return;
        }else if (e.getKeyCode() == KeyEvent.VK_ENTER && cells.get(currentCellIndex).getInputLetter() != ' '){
            int rowStart = (currentCellIndex / 5) * 5;
            int rowEnd = rowStart + 5;

            StringBuilder sb = new StringBuilder();
            for (int i = rowStart; i < rowEnd; i++) {
                sb.append(cells.get(i).getInputLetter());
            }
            String guess = sb.toString();
            guess = guess.toUpperCase();

            if (!possibleWords.contains(guess)) {
                System.out.println("not a word: " + guess);
                return;
            }


            for (int i = rowStart; i < rowEnd; i++) {
                cells.get(i).checkLetter(word, cells.get(i).getInputLetter());
                Character upperChar = Character.toUpperCase(cells.get(i).getInputLetter());
                Letter letter = new Letter((upperChar));

                if(cells.get(i).getAnswerIdentifier() == 'b'){
                    letter.setBlack(true);
                    letters.put(i, letter);
                    System.out.println("added " + letter + " to letters this char is black");
                }else if(cells.get(i).getAnswerIdentifier() == 'g'){
                    letter.setBlack(false);
                    letter.addGreenPos(i%5);
                    letters.put(i, letter);
                    System.out.println("added " + letter + " to letters this char is green at position " + i%5);
                }else if(cells.get(i).getAnswerIdentifier() == 'y'){
                    letter.setBlack(false);
                    letter.addYellowPos(i%5);
                    letters.put(i, letter);
                    System.out.println("added " + letter + " to letters this char is yellow at position " + i%5);
                }
            }

            for(int i = 0; i < letters.size(); i++){
                System.out.println("field " + i + ": " + letters.get(i));
            }

            currentCellIndex++;
            return;
        }else if(currentCellIndex % 5 == 4 && e.getKeyCode() != KeyEvent.VK_ENTER){
            cells.get(currentCellIndex).update(e);
            return;
        }else if(currentCellIndex < 25){
            cells.get(currentCellIndex).update(e);
            currentCellIndex++;
            return;
        }
    }

    public List<Cell> getCells(){
        return cells;
    }

    public HashMap<Integer, Cell> getColors(){
        return colors;
    }
}
