package com.nursyah.bruteforce_password.utils;


import java.util.ArrayList;

public class Bruteforce {
    final String pass;
    final int core = Runtime.getRuntime().availableProcessors();
    final char[] combination;
    final ArrayList<String> combs;

    public boolean found = false;
    String[] allguess = new String[core];
    public String guessProgress = "";
    public String guess = "";


    public Bruteforce(String pass, String combination, ArrayList<String> combs){
        this.pass = pass;
        this.combination = combination.toCharArray();
        this.combs = combs;
    }

    public void bruteforce() throws RuntimeException, InterruptedException {
        Thread []t = new Thread[core];
        for(int i=0; i< core; i++){
            int finalI = i;
            t[i] = new Thread(()->bruteforceCombination(combs.get(finalI), finalI));
            t[i].start();
        }

        while(!found && !pass.equals("")){
            StringBuilder temp = new StringBuilder();
            for(int i=0; i < core; i++) temp.append("guess").append(i + 1).append(": ").append(allguess[i]).append(" |  ");
            guessProgress = temp.toString();

            Thread.sleep(50);
        }

        for(Thread ti: t){
            ti.stop();
        }
    }

    void bruteforceCombination(String combination, int num){
        int length = 1;

        while(!found){
            for(char c : combination.toCharArray()){
                String temp = ""+c;
                brute(temp, length, num);
            }
            length++;
        }
    }

    void brute(String temp,int length, int num){
        if(length == 0){
            allguess[num] = temp;
            if(temp.equals(pass)) {
                found=true;
                guess = temp;
            }
            return;
        }
        for(char c: combination){
            String newtemp = temp+c;
            if(!found)brute(newtemp, length-1, num);
        }
    }


}
