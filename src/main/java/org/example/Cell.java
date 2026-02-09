package org.example;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Cell{
    private char realLetter;
    private char inputLetter = 'x';
    private int id;
    private Color color = Color.GRAY;
    private Color textColor = Color.WHITE;
    private int width = 50, height = 50;
    private int x, y;

    public Cell(char realLetter, int id){
        this.realLetter = realLetter;
        this.id = id;
        x = ((id % 5) * 75) + 250;
        y = ((id / 5) * 75) + 50;
    }

    public void draw(Graphics2D g2d){
        g2d.setColor(color);
        g2d.fillRect(x, y, width, height);
        g2d.setColor(textColor);
        g2d.drawString(String.valueOf(inputLetter),x+((width-5)/2), y+((height+7)/2));
    }

    public void update(KeyEvent e){
        inputLetter = e.getKeyChar();
    }

    public void checkLetter(char[] word, char letter){
        List<Character> wordList = Arrays.toString(word).chars().mapToObj(c -> (char)c).toList();

        inputLetter = letter;
        if(letter == realLetter){
            color = Color.GREEN;
            // add return for information bit calculation
        }else if(word[id%5] != letter && wordList.contains(letter)){
            color = Color.YELLOW;
            // add return for information bit calculation
        }else if(word[id%5] != letter && !wordList.contains(letter)){
            color = Color.RED;
            // add return for information bit calculation
        }

    }

    public int getId(){
        return id;
    }

    public char getInputLetter(){
        return inputLetter;
    }
}
