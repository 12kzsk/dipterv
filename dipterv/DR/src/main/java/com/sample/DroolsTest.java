package com.sample;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Properties;

import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

import tech.artisanhub.ShapeletTrainerMD.CSVReader;
import tech.artisanhub.ShapeletTrainerMD.DoubleVectorMD;
import tech.artisanhub.ShapeletTrainerMD.LearnShapeletsMD;
import tech.artisanhub.ShapeletTrainerMD.ShapeletFilterMD;

/**
 * This is a sample class to launch a rule.
 */
public class DroolsTest {
	
	public static DoubleVectorMD[] shapelet = null;
	public static double th = -1; //threshold
	public static String cls = "";
	public static Integer dimension;
	public static Integer drTsLength = 120; //TODO: param

    public static final void main(String[] args) {
        try {
            // load up the knowledge base
	        KieServices ks = KieServices.Factory.get();
    	    KieContainer kContainer = ks.getKieClasspathContainer();
        	KieSession kSession = kContainer.newKieSession("ksession-rules");

        	//get props
        	Properties prop = new Properties();
    		InputStream input = null;
    		try {
    			input = new FileInputStream("C:/Users/Zsuzsi/git/dipterv/SH/config.properties");
    			prop.load(input);
    		} catch (IOException e) {
    			e.printStackTrace();
    		}
    		
    		TimeSerieClassifier.vectors = null;
    				
        	String shapeletSource = prop.getProperty("bestShapeletsFileName");
        	LearnShapeletsMD.vectorSize = Integer.parseInt(prop.getProperty("dimension"));

    		String line;

    		try (InputStream fis = new FileInputStream("C:/Users/Zsuzsi/git/dipterv/SH/" + shapeletSource);//TODO csunya
    				InputStreamReader isr = new InputStreamReader(fis, Charset.forName("UTF-8"));
    				BufferedReader br = new BufferedReader(isr);) {
    			line = br.readLine();
    			String[] splittedLine = line.split("#");
    			th = Float.parseFloat(splittedLine[0]); // threshold
    			String shAndCl = splittedLine[1];
    			String[] splittedShAndCl = shAndCl.split(";");
    			cls = splittedShAndCl[splittedShAndCl.length - 1];
    			// -2 <-- ; & index(vectors of the shapelet)
    			String shStr[] = (shAndCl.substring(0, shAndCl.length() - cls.length() - 2)).split(";");
    			// shapelet in the right format
    			shapelet = new DoubleVectorMD[shStr.length];
    			// j: number of vectors in the shapelets
    			for (int j = 0; j < shStr.length; j++) {
    				DoubleVectorMD d = new DoubleVectorMD();
    				String[] membersOfVector = shStr[j].split(",");
    				// k: number of doubles in a vector
    				for (int k = 0; k < membersOfVector.length; k++) {
    					d.setElement(k, Double.parseDouble(membersOfVector[k]));
    				}
    				shapelet[j] = d;
    			}
    		} catch (Exception e) {}
    		
    		//normalize shapelet
    		//System.out.println(shapelet.length + "," + shapelet[0]);
    		shapelet = ShapeletFilterMD.zNorm(shapelet, false);
    		
        	//get input
        	ArrayList<ArrayList<String>> lines = CSVReader.read(prop.getProperty("droolsInput"), ",");
        	
            // go !
    		dimension = new Integer(prop.get("dimension").toString());
        	
    		//egy elem (vektor) besz�r�sa
        	for (int i = 0; i < lines.size(); i++){
        		LogItem logItem = new LogItem(dimension);
        		DoubleVectorMD inVec = new DoubleVectorMD(dimension);
        		for (int j = 0; j < lines.get(i).size(); j++){
        			inVec.setElement(j, Double.parseDouble(lines.get(i).get(j)));
        		}

            	logItem.setLogItem(inVec);
                kSession.insert(logItem);
            	
            	//szab�lyok ki�rt�kel�se
                kSession.fireAllRules();
   
        	}
            
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    public static class LogItem {

    	public LogItem(Integer dimension){
    		this.logItem = new DoubleVectorMD(dimension);
    	}
    	
        private DoubleVectorMD logItem;

        public DoubleVectorMD getLogItem() {
            return this.logItem;
        }

        public void setLogItem(DoubleVectorMD logItem) {
            this.logItem = logItem;
        }

    }

}