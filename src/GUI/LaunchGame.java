package GUI;

public class LaunchGame {

    public static void main(String[] args){

        System.out.println("Working Directory = " +
                System.getProperty("user.dir"));

        StartWindow startWindow = StartWindow.getInstance();
        startWindow.setVisible(true);

    }
}
