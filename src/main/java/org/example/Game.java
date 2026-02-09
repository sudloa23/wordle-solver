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
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Game{
    private char[] word;
    private List<Cell> cells = new ArrayList<>();
    private List<String> allWords = new ArrayList<String>();
    private int cellAmount =25;
    private int currentCellIndex = 0;
    private List<String> possibleWords = new ArrayList<>();

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
                possibleWords.add(line);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        word = allWords.get(ThreadLocalRandom.current().nextInt(1, 1692)).toCharArray();

        cells = new ArrayList<>();
        for(int i = 0; i < cellAmount; i++){
            cells.add(new Cell(word[i % 5], i));
        }
    }

    public void draw(Graphics2D g2d){
        for(int i = 0; i < cells.size(); i++){
            cells.get(i).draw(g2d);
        }
    }

    public void update(){

    }

    public void handleKey(KeyEvent e){
        if(e.getKeyCode() == KeyEvent.VK_BACK_SPACE && currentCellIndex % 5 != 0) {
            System.out.println("delete -> " + currentCellIndex);
            if(currentCellIndex % 5 == 4){
                cells.get(currentCellIndex).update(e);
                currentCellIndex--;
                return;
            }
            if (currentCellIndex > 0){
                currentCellIndex--;
                cells.get(currentCellIndex).update(e);
            } else if(currentCellIndex == 0){
                cells.get(0).update(e);
            }
            return;
        }else if(currentCellIndex % 5 == 4 && e.getKeyCode() != KeyEvent.VK_ENTER){
            cells.get(currentCellIndex).update(e);
            return;
        }else if (e.getKeyCode() == KeyEvent.VK_ENTER && cells.get(currentCellIndex).getInputLetter() != 'x'){
            int rowStart = (currentCellIndex / 5) * 5;
            int rowEnd = rowStart + 5;
            List<Character> wordAsList = new ArrayList<>();
            for(int i = rowStart; i < rowEnd; i++){
                wordAsList.add(cells.get(i).getInputLetter());
            }
            String wordChar = wordAsList.toString();
            if(!possibleWords.contains(wordChar)){
                System.out.println("not a word bucko");
                return;
            }

            for (int i = rowStart; i < rowEnd; i++) {
                cells.get(i).checkLetter(word, cells.get(i).getInputLetter());
            }

            currentCellIndex++;
            return;
        }else if(currentCellIndex < 25){
            cells.get(currentCellIndex).update(e);
            currentCellIndex++;
            return;
        }
    }
}
