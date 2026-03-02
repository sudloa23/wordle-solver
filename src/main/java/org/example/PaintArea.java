package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class PaintArea extends JPanel{
    Game game;
    Frame frame;

    public PaintArea(Game game){
        super();
        this.game = game;

        setFocusable(true);

        addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                //game.handleKey(e);
            }

            @Override
            public void keyPressed(KeyEvent e){
                int keyCode = e.getKeyCode();
                char keyChar = e.getKeyChar();

                if(keyCode == KeyEvent.VK_ENTER){
                    game.handleKey(e);
                }

                if(keyCode == KeyEvent.VK_BACK_SPACE){
                    game.handleKey(e);
                }

                if(Character.isLetter(keyChar)){
                    game.handleKey(e);
                }

                if(keyCode == KeyEvent.VK_ESCAPE){
                    frame.restartgame();

                }
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });
    }

    public void setFrame(Frame frame){
        this.frame = frame;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;
        int height = getHeight();
        int width = getWidth();
        game.update();
        game.draw(g2d);
    }

    public Game getGame(){
        return game;
    }

    public void setCalcForGame(Calculations calc){
        game.setCalc(calc);
    }
}
