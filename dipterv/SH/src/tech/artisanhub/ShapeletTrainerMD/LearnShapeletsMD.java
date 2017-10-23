package tech.artisanhub.ShapeletTrainerMD;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Map;
import java.util.Properties;

import weka.core.Instances;

public class LearnShapeletsMD
{
	//public static String ARFFName = "";
	public static String sourceName = "";
	public static Integer vectorSize = 3;
	
    public static void main( String[] args ) throws IOException{
    	// propertyk beolvas�sa
    	Properties prop = new Properties();
    	InputStream input = null;
    	input = new FileInputStream("config.properties");
    	prop.load(input);
    	
    	//ARFFName = prop.get("trainingDataFileNameArff").toString();
		sourceName = prop.get("trainingDataFileNameCsv").toString();
    	vectorSize = Integer.parseInt(prop.get("dimension").toString());
    	
    	long startTime = System.currentTimeMillis();
        try {
        	TimeSeriesMD[] data = ShapeletFilterMD.loadData(sourceName);

            //shapeletek sz�ma
            int k = Integer.MAX_VALUE;
            int minLength = Integer.parseInt(prop.get("minShapeletLength").toString());
            //max hossz sz�m�tott �rt�k:
            //int maxLength = data.instance(1).numValues()-1;
            //max hossz param�terez�sb�l:
            int maxLength = Integer.parseInt(prop.get("maxShapeletLength").toString());
            
            String classOutPutFile = prop.get("bestShapeletsFileName").toString();

            String outPutFile = prop.get("allShapeletsFileName").toString();

            int dim = Integer.parseInt(prop.get("dimension").toString());
            
            ShapeletFilterMD sf = new ShapeletFilterMD(k, minLength, maxLength);
            // az oszt�lyoz�sn�l felhaszn�lt (legjobb) shapelete(ke)t tartalmazza
            sf.setClassOutputFile(classOutPutFile);
            // a log file az �sszes megtal�lt shapeletet tartalmazza (rangsorolva)
            sf.setLogOutputFile(outPutFile);
            ArrayList<ShapeletMD> generatedShapelets = sf.process(data, dim);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        long endTime   = System.currentTimeMillis();
        long totalTime = endTime - startTime;
        System.out.println("\nExecution time in milli seconds: "+totalTime);
    }
}