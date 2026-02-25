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
    private HashMap<Character, DisplayLetter> displayLetters = new HashMap<>();
    private Calculations calculations;
    private boolean win = false;

    public Game(){
        initGame();
    }

    public void initGame(){
        System.out.println("Resource URL: " + Game.class.getResource("/answers.txt"));
        InputStream is = Game.class.getResourceAsStream("/answers.txt");
        if (is == null) {
            throw new RuntimeException("words.txt not found (is null). Liegt sie wirklich in src/main/resources ?");
        }
        try (BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) {
                allWords.add(line.trim());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        InputStream is2 = Game.class.getResourceAsStream("/possible_guesses.txt");
        if (is2 == null) {
            throw new RuntimeException("possible_guesses.txt not found (is null). Liegt sie wirklich in src/main/resources ?");
        }
        try (BufferedReader br = new BufferedReader(new InputStreamReader(is2, StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) {
                possibleWords.add(line.trim().toUpperCase());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        word = allWords.get(ThreadLocalRandom.current().nextInt(allWords.size())).toCharArray();

        cells = new ArrayList<>();
        for(int i = 0; i < cellAmount; i++){
            cells.add(new Cell(word[i % 5], i));
        }
        int counter = 0;
        for(char ch = 'A'; ch <= 'Z'; ch++){
            int id = counter;
            displayLetters.put(ch, new DisplayLetter(ch, id));
            counter++;
        }

        System.out.println("word: " + Arrays.toString(word));
    }

    public void draw(Graphics2D g2d){

        for(int i = 0; i < cells.size(); i++){
            cells.get(i).draw(g2d);
        }
        for(char ch = 'A'; ch <= 'Z'; ch++){
            displayLetters.get(ch).draw(g2d);
        }
        calculations.draw(g2d);
        if(win){
            g2d.setColor(Color.GREEN);
            g2d.drawString("YOU WON", 350, 30);
        }
    }

    public void update(){

    }

    public void handleKey(KeyEvent e){
        if(!win) {
            if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
                int rowStart = ((currentCellIndex / 5) * 5);
                int rowEnd = (rowStart + 4);

                for (int i = rowEnd; i >= rowStart; i--){
                    System.out.println("trying to delete " + i);
                    if (cells.get(i).getInputLetter() != ' ') {
                        System.out.println(i + " deleted");
                        cells.get(i).setInputLetter(' ');
                        if (currentCellIndex != rowStart) {
                            currentCellIndex--;
                        }

                        return;
                    }
                }

            } else if (e.getKeyCode() == KeyEvent.VK_ENTER && cells.get(currentCellIndex).getInputLetter() != ' ') {
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

                    if (cells.get(i).getAnswerIdentifier() == 'b') {
                        letter.setBlack(true);
                        letters.put(i, letter);
                        System.out.println(letter.getLetter() + " black");
                        displayLetters.get(letter.getLetter()).update('b');
                    } else if (cells.get(i).getAnswerIdentifier() == 'g') {
                        letter.setBlack(false);
                        letter.addGreenPos(i % 5);
                        letters.put(i, letter);
                        displayLetters.get(letter.getLetter()).update('g');
                    } else if (cells.get(i).getAnswerIdentifier() == 'y') {
                        letter.setBlack(false);
                        letter.addYellowPos(i % 5);
                        letters.put(i, letter);
                        displayLetters.get(letter.getLetter()).update('y');
                    }
                }

                if (checkWin(rowStart, rowEnd)) {
                    win = true;
                }else{
                    calculations.removeWord(guess);
                }
                calculations.updateLetters(letters);
                calculations.removeLetters();
                calculations.calculateEntropy();

                currentCellIndex++;
                return;
            } else if (currentCellIndex % 5 == 4 && e.getKeyCode() != KeyEvent.VK_ENTER) {
                cells.get(currentCellIndex).update(e);
                return;
            } else if (currentCellIndex < 25) {
                cells.get(currentCellIndex).update(e);
                currentCellIndex++;
                return;
            }
        }
    }

    public List<Cell> getCells(){
        return cells;
    }

    public HashMap<Integer, Letter> getLetters(){
        return letters;
    }

    public void setCalc(Calculations calculations){
        this.calculations = calculations;
        this.calculations.calculateEntropy();
    }

    public boolean checkWin(int start, int end){
        if(cells.get(start).getAnswerIdentifier() == 'g'
        && cells.get(start+1).getAnswerIdentifier() == 'g'
        && cells.get(start+2).getAnswerIdentifier() == 'g'
        && cells.get(start+3).getAnswerIdentifier() == 'g'
        && cells.get(end-1).getAnswerIdentifier() == 'g'){
            win = true;
            System.out.println("you won");
            possibleWords.clear();
        }

        return false;
    }
}
