package GUI;

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

    private BufferedImage image;
    private ArrayList<JButton> Pawns;

    public BoardPanel() {

        this.setLayout(null);
        try {
            image = ImageIO.read(new File(System.getProperty("user.dir")+"/Main/imgs/sorry_900.jpg"));
        } catch (IOException ex) {
            // handle exception...
            System.out.println("loading image failed");
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 0, 0, this); // see javadoc for more info on the parameters

    }
}
