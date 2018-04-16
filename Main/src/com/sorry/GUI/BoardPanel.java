package com.sorry.GUI;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.util.*;

public class BoardPanel extends JPanel {

    private Image BoardImage;
    private ArrayList<JButton> Pawns;

    private final static String imagePath = "/Main/imgs/sorry_board.jpg";
    public BoardPanel() {
        int resaledWidth = this.getWidth();
        int resaledHeight = this.getHeight();
        this.setLayout(null);
        try {
            Image basicImage = ImageIO.read(new File(System.getProperty("user.dir")+imagePath));
            BoardImage = basicImage.getScaledInstance(Constants.boardWidth, Constants.boardHeight, Image.SCALE_SMOOTH);
        } catch (IOException ex) {
            // handle exception...
            System.out.println("loading image failed");
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(BoardImage, 0, 0, this); // see javadoc for more info on the parameters

    }
}
