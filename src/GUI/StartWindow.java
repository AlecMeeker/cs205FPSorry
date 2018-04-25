package GUI;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class StartWindow extends JFrame {

    //Singleton
    private static volatile StartWindow instance = null;
    private static Object mutex = new Object();

    //gui components
    private JButton newGameBtn;
    private JLabel gameLogo;

    //
    private String gameLogoImgPath= "/imgs/sorry_start.jpg";

    private StartWindow(){
        initWindow();
    }

    public static StartWindow getInstance(){ //Thread safe singleton model
        StartWindow result = instance;
        if(result == null){
            synchronized (mutex){
                result = instance;
                if(result == null){
                    instance = result = new StartWindow();
                }
            }
        }
        return result;
    }

    private void initWindow(){

        //GUI Components config
        initGuiComponents();
        setGuiComponents();
        addEventListenerToComponents();

        //Set layout
        this.setLayout(null);
        //Setting the window
        this.setSize(Constants.windowWidth,Constants.windowHeight);
        this.setTitle("Sorry! Sweet avenge board game.");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //this.pack(); // pack the window
        //this.setVisible(true);
    }

    private void initGuiComponents(){
        newGameBtn = new JButton("New Game");
        try {
            Image basicImage = ImageIO.read(new File(System.getProperty("user.dir")+gameLogoImgPath));
            basicImage = basicImage.getScaledInstance(Constants.gameLogoWidth, Constants.gameLogoHeight, Image.SCALE_SMOOTH);
            ImageIcon gameLogoimg = new ImageIcon(basicImage);
            gameLogo = new JLabel(gameLogoimg);
        } catch (Exception ex) {
            // handle exception...
            System.out.println("loadPawns failed \n" + ex.toString());
        }
    }

    private void setGuiComponents(){
        newGameBtn.setBounds(960, 200, 120, 40);
        this.add(newGameBtn);
        gameLogo.setBounds(Constants.gameLogoStartX,Constants.gameLogoStartY,Constants.gameLogoWidth,Constants.gameLogoHeight);
        this.add(gameLogo);
    }

    private void addEventListenerToComponents(){
        newGameBtn.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                newGameDialog();

            }
        });
    }

    private void newGameDialog(){

        String[] players = {
                "2","3","4"
        };
        JComboBox<String> numPlayers = new JComboBox<String>(players);

        String [] playerColors = {"blue","yellow","green","red"};

        JComboBox<String> playerColorSelect = new JComboBox<String>(playerColors);

        String [] aiBehaviors = {"Dumb & Nice","Dumb & Cruel","Smart & Nice","Smart & Cruel"};
        JComboBox<String> computerDifficulties1 = new JComboBox<String>(aiBehaviors);
        JComboBox<String> computerDifficulties2 = new JComboBox<String>(aiBehaviors);
        JComboBox<String> computerDifficulties3 = new JComboBox<String>(aiBehaviors);

        final JComponent[] inputs = new JComponent[] {
                new JLabel("Number of Players"),
                numPlayers,
                new JLabel("Player Color"),
                playerColorSelect,
                new JLabel("Computer 1"),
                computerDifficulties1,
                new JLabel("Computer 2"),
                computerDifficulties2,
                new JLabel("Computer 3"),
                computerDifficulties3,

        };
        Object[] possibilities = {"ham", "spam", "yam"};

        //JOptionPane.showInputDialog(null, inputs, "My custom dialog", JOptionPane.PLAIN_MESSAGE);
        int result = JOptionPane.showConfirmDialog(null, inputs, "My custom dialog", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            System.out.println("You entered " +
                    numPlayers.getSelectedItem().toString() + ", " +
                    playerColorSelect.getSelectedItem().toString() + ", " +
                    computerDifficulties1.getSelectedItem().toString() + ", " +
                    computerDifficulties2.getSelectedItem().toString() + ", " +
                    computerDifficulties3.getSelectedItem().toString());
                GameWindow gw = GameWindow.getInstance();
                gw.setVisible(true);
        } else {
            System.out.println("User canceled / closed the dialog, result = " + result);
        }
    }
}
