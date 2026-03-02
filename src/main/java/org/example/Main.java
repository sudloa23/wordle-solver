package org.example;

import javax.swing.*;
import java.util.HashMap;

public class Main {
    public static void main(String[] args) {
        System.setProperty("sun.java2d.opengl", "true");
        Game game = new Game();
        PaintArea paintArea = new PaintArea(game);
        Frame frame = new Frame(paintArea);
        paintArea.setFrame(frame);
        frame.setVisible(true);
        paintArea.requestFocusInWindow();
    }
}