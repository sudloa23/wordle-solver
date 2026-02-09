package org.example;

import javax.swing.*;

public class Frame extends JFrame{
    private int height =1000;
    private int width =1000;
    private String Title = "wordle";

    private PaintArea paintArea;

    public Frame(PaintArea paintArea){
        this.paintArea = paintArea;

        setTitle(Title);
        setSize(width,height);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        add(paintArea);

        Timer timer = new Timer(20, (e -> paintArea.repaint()));

        timer.start();
    }
}
