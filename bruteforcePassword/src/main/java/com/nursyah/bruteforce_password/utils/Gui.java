package com.nursyah.bruteforce_password.utils;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

public class Gui extends JFrame implements ItemListener{

    JLabel osLabel = new JLabel("OS: " + System.getProperty("os.name"));
    JLabel coreLabel = new JLabel("Core: " + Runtime.getRuntime().availableProcessors());

    JCheckBox lowerAplhabetCheckbox = new JCheckBox("lower Alphabet");
    JCheckBox upperAplhabetCheckbox = new JCheckBox("upper Alphabet");
    JCheckBox digitCheckbox = new JCheckBox("digit");

    JTextField combinationTextfield = new JTextField(1000);
    JTextField passwordTextfield = new JTextField(10);

    JButton runBtn = new JButton("run");
    JButton stopBtn = new JButton("stop");

    JTextField firsLetterCombination = new JTextField(100);
    JTextArea guessTextArea = new JTextArea(4, 100);


    String[] combination = {"abcdefghijklmnpqrstuvwxyz","ABCDEFGHIJKLMNOPQRSTUVWXYZ","0123456789"};
    ArrayList<String> combs;

    int combinationRes = 0;
    boolean runBruteforceBool = false;


    public Gui(){
        SwingUtilities.invokeLater(this::main);
    }

    void main(){
        setSize(800, 400);
        setLocation(10, 10);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("bruteforce multithread");
        setVisible(true);

        // configuration Java swing component
        lowerAplhabetCheckbox.setFocusPainted(false);
        upperAplhabetCheckbox.setFocusPainted(false);
        digitCheckbox.setFocusPainted(false);

        combinationTextfield.setEnabled(false);
        combinationTextfield.setDisabledTextColor(Color.BLACK);
        firsLetterCombination.setEnabled(false);
        firsLetterCombination.setDisabledTextColor(Color.BLACK);

        runBtn.setFocusPainted(false);
        runBtn.setBackground(Color.white);
        stopBtn.setFocusPainted(false);
        stopBtn.setBackground(Color.white);
        stopBtn.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {
                if(runBruteforceBool) {
                    stopBtn.setBackground(Color.red);
                    stopBtn.setForeground(Color.white);
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                stopBtn.setBackground(Color.white);
                stopBtn.setForeground(Color.BLACK);
            }
        });

        guessTextArea.setEnabled(false);
        guessTextArea.setDisabledTextColor(Color.BLACK);

        add(buildDashboard());

        lowerAplhabetCheckbox.addItemListener(this);
        upperAplhabetCheckbox.addItemListener(this);
        digitCheckbox.addItemListener(this);

        lowerAplhabetCheckbox.setSelected(true);


        runBtn.addActionListener(v->{
            if(!runBruteforceBool){
                runBruteforceBool = true;
                runBtn.setBackground(Color.GREEN);
                setDisabled(true);
                runBruteforce();
            }
        });

        stopBtn.addActionListener(v->{
            if(runBruteforceBool){
                runBruteforceBool = false;
                setDisabled(false);
                runBtn.setBackground(Color.white);
            }
        });
    }

    JPanel buildDashboard(){
        JPanel panel = new JPanel(new MigLayout("flowy, gapy 0"));

        /* Information System */
        JPanel panel1 = new JPanel(new MigLayout("flowy"));
        panel1.add(osLabel);
        panel1.add(coreLabel);

        /* Checkbox guess */
        JPanel panel2 = new JPanel(new MigLayout());
        panel2.add(lowerAplhabetCheckbox);
        panel2.add(upperAplhabetCheckbox);
        panel2.add(digitCheckbox);

        /* Combination */
        JPanel panel3 = new JPanel(new MigLayout());
        panel3.add(new JLabel("Combination:"));
        panel3.add(combinationTextfield);

        /* firstletter Combination */
        JPanel panel4 = new JPanel(new MigLayout());
        panel4.add(new JLabel("First letter Combination:"));
        panel4.add(firsLetterCombination);

        /* password */
        JPanel panel5 = new JPanel(new MigLayout());
        panel5.add(new JLabel("password:"));
        panel5.add(passwordTextfield);
        panel5.add(runBtn);
        panel5.add(stopBtn);

        /* guess password */
        JPanel panel6 = new JPanel(new MigLayout("flowx"));
        panel6.add(guessTextArea);


        panel.add(panel1);
        panel.add(panel2);
        panel.add(panel3);
        panel.add(panel4);
        panel.add(panel5);
        panel.add(panel6);

        return panel;
    }


    void runBruteforce(){

        new Thread(() -> {
            try {
                testTime();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();

    }

    // this is main function
    void testTime() throws InterruptedException {
        long start = System.currentTimeMillis();

        String password = passwordTextfield.getText();


        Bruteforce bruteforce = new Bruteforce(password, combinationTextfield.getText(), combs);
        Thread thread = new Thread(()-> {
            try {
                bruteforce.bruteforce();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        thread.start();

        while(runBruteforceBool){
            if(password.equals("") || bruteforce.found) runBruteforceBool = false;
            String result = bruteforce.guessProgress;

            result += "\n\nrun in: " + (System.currentTimeMillis() - start)/1000 + "s";

            guessTextArea.setText(result);
            Thread.sleep(50);
        }

        thread.stop();

        setDisabled(false);
        runBtn.setBackground(Color.white);
        guessTextArea.setText(guessTextArea.getText() + "\ntook time: " + (System.currentTimeMillis() - start) + "ms" + " | password: " + bruteforce.guess);
    }

    /* Checkbox action */
    @Override
    public void itemStateChanged(ItemEvent e) {
        if(e.getSource() == lowerAplhabetCheckbox){
            if(e.getStateChange() == ItemEvent.SELECTED)combinationRes += 1;
            else combinationRes -= 1;
        }

        if(e.getSource() == upperAplhabetCheckbox){
            if(e.getStateChange() == ItemEvent.SELECTED) combinationRes += 2;
            else combinationRes -= 2;
        }

        if(e.getSource() == digitCheckbox){
            if(e.getStateChange() == ItemEvent.SELECTED) combinationRes += 4;
            else combinationRes -= 4;
        }



        switch (combinationRes){
            case 0:
                combinationTextfield.setText("");break;
            case 1:
                combinationTextfield.setText(combination[0]);break;
            case 2:
                combinationTextfield.setText(combination[1]);break;
            case 3:
                combinationTextfield.setText(combination[0]+combination[1]);break;
            case 4:
                combinationTextfield.setText(combination[2]);break;
            case 5:
                combinationTextfield.setText(combination[0]+combination[2]);break;
            case 6:
                combinationTextfield.setText(combination[1]+combination[2]);break;
            case 7:
                combinationTextfield.setText(combination[0]+combination[1]+combination[2]);
        }

        updateFirstLetterCombination();
    }

    /* disabled textfield if bruteforce run */
    void setDisabled(Boolean enabled){
        lowerAplhabetCheckbox.setEnabled(!enabled);
        upperAplhabetCheckbox.setEnabled(!enabled);
        digitCheckbox.setEnabled(!enabled);
        passwordTextfield.setEnabled(!enabled);
    }


    private void updateFirstLetterCombination() {
        String combination = combinationTextfield.getText();

        int core=Runtime.getRuntime().availableProcessors();
        StringBuilder temp = new StringBuilder();

        combs = new ArrayList<>();

        int i=0;
        int n = core;
        int jtemp = combination.length() / core;
        int j= jtemp;

        while(n-- != 0){
            String str = combination.substring(i,j);
            if(n == 0) str = combination.substring(i, j + (combination.length() % core));
            combs.add(str);
            temp.append(str).append("   ");
            i = j; j += jtemp;
        }

        firsLetterCombination.setText(temp.toString());
    }

}
