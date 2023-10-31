import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.swing.*;

public class Scrapper extends JPanel implements Runnable {

    int list_size = 0;
    int done_count;
    boolean button_pressed = false;
    boolean ran = false;
    Thread object;
    Elements companies;
    ArrayList<Company> companylist;
    JProgressBar bar;
    JScrollBar VerticalScroll;
    JTextField text_field;
    JTextArea log;
    JScrollPane scrollog;
    JButton button;

    Scrapper() {
        this.setPreferredSize(new Dimension(500, 250));
        this.setBackground(Color.lightGray);

        text_field = new JTextField();
        text_field.setVisible(true);
        text_field.setPreferredSize(new Dimension(380, 20));
        text_field.setName("Enter the Yellow Pages URL");
        this.add(text_field);
        button = new JButton();
        button.addActionListener(e -> {
            button_pressed = true;
            button.setVisible(false);
            try {
                logic();
            } catch (IOException ex) {
                System.out.println("Issue in writing to file");
                throw new RuntimeException(ex);
            }
        });
        button.setText("Scrap!");
        button.setPreferredSize(new Dimension(80, 20));
        button.setVisible(true);
        this.add(button);
        bar = new JProgressBar();
        bar.setVisible(false);
        bar.setIndeterminate(true);
        this.add(bar);
        log = new JTextArea(6, 50);
        log.setEditable(false);
        log.setVisible(true);
        scrollog = new JScrollPane(log, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollog.setVisible(false);
        scrollog.setWheelScrollingEnabled(true);
        scrollog.getVerticalScrollBar().setVisible(false);
        this.add(scrollog);

        VerticalScroll = scrollog.getVerticalScrollBar();
    }

    public void logic() throws IOException {

        if (!ran) {
            ran = true;
            String url = text_field.getText();
            try {
                new URL(url);
            } catch (MalformedURLException e) {
                JOptionPane.showMessageDialog(null, "Sorry incorrect Url entered", "Error occured", -1);
                System.exit(0);
            }

            Document doc = Jsoup.connect(url).userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/108.0.0.0 Safari/537.36").get();
            companylist = new ArrayList<>();
            companies = doc.select(".listing__content__wrapper");
            list_size = companies.size();
            bar.setVisible(true);
            if (object == null)
                object = new Thread(this);
            scrollog.setVisible(true);
        }
        object.start();
        repaint();
    }

    @Override
    public void run() {
        for (org.jsoup.nodes.Element company : companies) {
            companylist.add(new Company(company, ""));
            log.append("done: " + done_count + "/" + companies.size() + "\n");
            done_count++;
            VerticalScroll.setValue(VerticalScroll.getMaximum());
        }
        output_to_file();
    }

    public void output_to_file() {

        bar.setVisible(false);
        object.interrupt();
        File output_file = new File("data.csv");

        if (!output_file.exists()) {
            try {
                output_file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        PrintWriter writer;

        try {
            writer = new PrintWriter(output_file);
        } catch (IOException ex2) {
            System.out.println("error occured when writing to file, check if file was deleted or altered while adding.");
            System.exit(0);
            throw new RuntimeException(ex2);
        }

        for (Company company : companylist) {

            writer.print(company);
            writer.println();
        }
        writer.close();
        Desktop desktop = Desktop.getDesktop();
        try {
            desktop.open(output_file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.exit(0);
    }
}