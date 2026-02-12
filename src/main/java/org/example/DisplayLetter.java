package org.example;

import java.awt.*;

public class DisplayLetter{
    private Character letter;
    private int x, y, length;
    private int id;
    private Color color;
    private Color textColor;

    public DisplayLetter(Character letter, int id){
        this.letter = letter;
        this.id = id;
        this.x = (id%4 *55) + 15;
        this.y = (id/4 *55) + 15;
        length = 50;
        textColor = Color.WHITE;
        color = Color.LIGHT_GRAY;
    }

    public void draw(Graphics2D g2d){
        g2d.setColor(color);
        g2d.fillRect(x, y, length, length);
        g2d.setColor(textColor);
        g2d.drawString(letter.toString(), x + (length/2) - 2, y + (length/2) + 2);
    }

    public void update(char state){
        if(state == 'b'){
            color = Color.BLACK;
        }else if(state == 'g'){
            color = Color.GREEN;
        }else if(state == 'y'){
            color = Color.ORANGE;
        }
    }
}
