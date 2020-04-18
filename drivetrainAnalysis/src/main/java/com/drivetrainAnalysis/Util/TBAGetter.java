package com.drivetrainAnalysis.Util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import org.json.simple.parser.JSONParser;

public class TBAGetter {

    HttpsURLConnection connection;

    URL tbaUrl;

    BufferedReader in;

    String inputLine;
    StringBuffer content;

    FileWriter file;

    JSONParser parser;

    public TBAGetter() {
    }

    public String[] getTeamsInDistrict() throws IOException {

        // create a new url
        tbaUrl = new URL("https://www.thebluealliance.com/api/v3/district/2019fim/teams/keys");

        // open url connection
        connection = (HttpsURLConnection) tbaUrl.openConnection();

        // TBA requirements
        connection.setRequestMethod("GET");
        connection.setRequestProperty("X-TBA-Auth-key",
                "bkzTakyM23JMHhf4Gb8hcIlo5MomsWeE0wT7NoCyCcH2yngFSlV7Lu77ottyTzwG");

        // create string readers
        in = new BufferedReader(new InputStreamReader(connection.getInputStream()));

        content = new StringBuffer();

        // while there is data to read
        while ((inputLine = in.readLine()) != null) {

            // read data
            content.append(inputLine);
        }

        // close buffer
        in.close();

        String[] dataList = content.toString().split(",", 0);

        for (int i = 0; i < dataList.length; i++) {

            dataList[i] = formatForLogic(dataList[i]);
        }

        // return the data
        return dataList;
    }

    public String[] getMatchKeys(String teamNumber) throws IOException {

        // create a new url
        tbaUrl = new URL("https://www.thebluealliance.com/api/v3/team/" + teamNumber + "/matches/2019/keys");

        // open url connection
        connection = (HttpsURLConnection) tbaUrl.openConnection();

        // TBA requirements
        connection.setRequestMethod("GET");
        connection.setRequestProperty("X-TBA-Auth-key",
                "bkzTakyM23JMHhf4Gb8hcIlo5MomsWeE0wT7NoCyCcH2yngFSlV7Lu77ottyTzwG");

        // create string readers
        in = new BufferedReader(new InputStreamReader(connection.getInputStream()));

        content = new StringBuffer();

        // while there is data to read
        while ((inputLine = in.readLine()) != null) {

            // read data
            content.append(inputLine);
        }

        // close buffer
        in.close();

        String[] dataList = content.toString().split(",", 0);

        for (int i = 0; i < dataList.length; i++) {

            dataList[i] = formatForLogic(dataList[i]);
        }

        // return the data
        return dataList;
    }

    public double getTeamWinrate(String[] matchUrls, String teamNumber) throws IOException {

        double win = 0;
        double totalMatches = 0;

        double winRate = 5;

        for(int matchIndex = 0; matchIndex < matchUrls.length; matchIndex++) {

        // create a new url
        tbaUrl = new URL("https://www.thebluealliance.com/api/v3/match/" + matchUrls[matchIndex] + "/simple");

        // open url connection
        connection = (HttpsURLConnection) tbaUrl.openConnection();

        // TBA requirements
        connection.setRequestMethod("GET");
        connection.setRequestProperty("X-TBA-Auth-key",
                "bkzTakyM23JMHhf4Gb8hcIlo5MomsWeE0wT7NoCyCcH2yngFSlV7Lu77ottyTzwG");

        // create string readers
        try {

            in = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            content = new StringBuffer();

            // while there is data to read
            while ((inputLine = in.readLine()) != null) {

                content.append(inputLine);
            }

            // close buffer
            in.close();

            String[] delimitedData = content.toString().split(" ", 0);



        //delimitedData[51 60 69] indexes for blue side robots
        //delimitedData[186] index for winning alliance

        try {
            

        String blueRobots[] = {formatForLogic(delimitedData[51]), formatForLogic(delimitedData[60]), formatForLogic(delimitedData[69])};//frc5675

        String redRobots[] = {formatForLogic(delimitedData[124]), formatForLogic(delimitedData[133]), formatForLogic(delimitedData[142])};//frc5675

        String winningAlliance = formatForLogic(delimitedData[186]);//blue or red

        for(int i = 0; i < blueRobots.length; i++) {

            //If blue team won...
            if(winningAlliance.equals("blue")) {
                
                //And the current bot was on blue...
                if(teamNumber.equals(blueRobots[i])) {

                    //Add one to win
                    win += 1;

                    break;
                }
            }
        }

        for(int j = 0; j < redRobots.length; j++) {

            //Like blue, but opposite lol
            if(winningAlliance.equals("red")) {

                if(teamNumber.equals(redRobots[j])) {

                    win += 1;

                    break;
                }
            }
        }

        totalMatches += 1;

        winRate = win / totalMatches;

    }catch (Exception e) {
            
        e.printStackTrace();
        }

    } catch (FileNotFoundException e) {
            
        e.printStackTrace();
    }  
    }
        return winRate;
    }

    public String formatForLogic(String jsonValue) {

        if(jsonValue != null) {

            String[] splitString = jsonValue.split("\"", 0);

            if(splitString.length > 1) {

                return splitString[1];
            } else {

                return null;
            }
            
        }

        else{

            return null;
        }
    }


    public void disconnect() {

        //disconnect from url
        connection.disconnect();
    }
}