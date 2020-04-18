package com.drivetrainAnalysis;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import com.drivetrainAnalysis.Util.TBAGetter;

public class Main {

    static TBAGetter getter;

    static String[] teamList;

    static String[] matchData;
    static String winLoss;

    static FileWriter csvWriter;

    public static void main(String[] args) {

        getter = new TBAGetter();

        double winRate;

        BufferedReader csvReader;

        try {

            csvReader = new BufferedReader(new FileReader("teams.csv"));

            String row;

            while ((row = csvReader.readLine()) != null) {

                teamList = row.split(",");
            }

            csvWriter = new FileWriter("winrates.csv");
            
            csvWriter.append("Team");
            csvWriter.append(",");
            csvWriter.append("Winrate");
            csvWriter.append("\n");

            for(int i = 19; i < teamList.length; i++) {

                winRate = getter.getTeamWinrate(getter.getMatchKeys(teamList[i]), teamList[i]);


                csvWriter.write(teamList[i]);
                csvWriter.append(",");
                csvWriter.write(Double.toString(winRate));
                csvWriter.append("\n");
    
                csvWriter.flush();
            }
        } catch (IOException e) {
                
            e.printStackTrace();
        }

        try {

            csvWriter.close();
        } catch (IOException e) {

            e.printStackTrace();
        }
    }
}
