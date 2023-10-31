import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class Runner extends JFrame {
    public static void main(String[] args) {

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (UnsupportedLookAndFeelException e) {
            throw new RuntimeException(e);
        }

        JFrame frame = new JFrame();
        frame.setTitle("Yellow Pages Scrapper");
        Scrapper scrapper = new Scrapper();
        frame.add(scrapper);
        frame.setResizable(false);
        frame.setVisible(true);
        frame.setFocusable(true);
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        frame.setSize(500, 250);
        frame.setBackground(Color.lightGray);
    }
}
