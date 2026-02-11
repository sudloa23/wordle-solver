package org.example;

import java.util.ArrayList;
import java.util.List;

public class Letter{
    private Character letter;
    private List<Integer> greenPos = new ArrayList();
    private List<Integer> yellowPos = new ArrayList<>();
    private boolean black;

    public Letter(Character letter){
        this.letter = letter;
    }

    public Character getLetter(){
        return letter;
    }

    public List<Integer> getGreenPos(){
        return greenPos;
    }

    public List<Integer> getYellowPos(){
        return yellowPos;
    }

    public boolean isBlack(){
        return black;
    }

    public void addGreenPos(Integer greenPos) {
        this.greenPos.add(greenPos);
    }

    public void addYellowPos(Integer yellowPos) {
        this.yellowPos.add(yellowPos);
    }

    public void setBlack(boolean black) {
        this.black = black;
    }

    public void setLetter(Character letter) {
        this.letter = letter;
    }
}
