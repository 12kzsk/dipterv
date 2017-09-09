package tech.artisanhub.ShapeletTrainerMD;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class CSVReader {

    public static ArrayList<ArrayList<String>> read(String csvFile, String csvSplitBy) {
        BufferedReader br = null;
        String line = "";

        ArrayList<ArrayList<String>> listStrList = new ArrayList<ArrayList<String>>();
        
        try {

            br = new BufferedReader(new FileReader(csvFile));
            while ((line = br.readLine()) != null) {
            	ArrayList<String> strList = new ArrayList<String>();
                
            	// separator: csvSplitBy
                String[] str = line.split(csvSplitBy);
                for (int i = 0; i < str.length; i++){
                	strList.add(str[i]);
                }
                
                listStrList.add(strList);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        
        return listStrList;

    }

}