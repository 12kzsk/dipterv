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

	public static String ARFFName = "";
	public static Integer vectorSize = 1;
	
    public static void main( String[] args ) throws IOException{
    	// propertyk beolvasása
    	Properties prop = new Properties();
    	InputStream input = null;
    	input = new FileInputStream("config.properties");
    	prop.load(input);
    	
    	ARFFName = prop.get("trainingDataFileNameArff").toString();
    	vectorSize = Integer.parseInt(prop.get("dimension").toString());
    	
    	long startTime = System.currentTimeMillis();
        try {
            Instances data = ShapeletFilterMD.loadData(ARFFName);

            int k = Integer.MAX_VALUE; // number of shapelets
            int minLength = 2;
//          int maxLength = data.get(1).numValues()-1;
            //int maxLength = data.instance(1).numValues()-1; //TODO
            int maxLength = 10;

            String classOutPutFile = "classGeneratedShapelets2.txt";

            String outPutFile = "generatedShapelets2.txt";

            int dim = vectorSize; //TODO
            
            ShapeletFilterMD sf = new ShapeletFilterMD(k, minLength, maxLength);
            sf.setClassOutputFile(classOutPutFile); // input for classifying
            sf.setLogOutputFile(outPutFile); // log file stores shapelet output
            ArrayList<ShapeletMD> generatedShapelets = sf.process(data, dim);
            
            Map<Double,Integer> classDist = ShapeletFilterMD.getClassDistributions(data);
            ArrayList<Integer> arr = new ArrayList<Integer>(); //arr = class ids
            for (double val: classDist.keySet()){
                arr.add((int)val);
            }
            
            //int shapeletClusterSize = (int) Math.sqrt(generatedShapelets.size()); //this defines the threshold. Put a larger number to detect all the events
            //ArrayList<Shapelet2> mergedShapelets = new MergeShapelets2().mergeShapelets(generatedShapelets,shapeletClusterSize); //meerging shapelets
            //ArrayList<Shapelet2> finalOutputShapelets = new ImportantShapelets2().GetImportantShapelets(mergedShapelets,data,arr); //find important shapelets
            //pl. 3 osztály esetén 3 shapelet lesz benne, amik üresek, kivéve a mergedblabla tulajdonságuk, amiben 12 shapelet lesz (iris adatsor esetén)
//          displayShapeletStats(finalOutputShapelets,data.get(1).numValues()-1);
            //displayShapeletStats(finalOutputShapelets,data.instance(1).numValues()-1); //TODO


        }
        catch (Exception e) {
            e.printStackTrace();
        }
        long endTime   = System.currentTimeMillis();
        long totalTime = endTime - startTime;
        System.out.println("\nExecution time in milli seconds: "+totalTime);
    }
/*
    private static void displayShapeletStats(ArrayList<Shapelet2> shapelets,int noOfColumns) throws IOException {
        int size = 0;
        int startPos =0;
        int shapeletVal = 0;
        int sizeOfTheShapelet = 0;
        int eventCount =0;
        BufferedWriter output = null;
        for(Shapelet2 val : shapelets){
            eventCount ++;
            File file = new File("dataset/importantShapeletsEvent"+eventCount+".txt");
            output = new BufferedWriter(new FileWriter(file));
            if(val != null) {
                sizeOfTheShapelet = val.contentInMergedShapelets.size();
                XYSeriesCollection dataset = new XYSeriesCollection( );
                for (int i = 0; i < sizeOfTheShapelet; i++) {
                    size = val.contentInMergedShapelets.get(i).size() - 4;
                    startPos = val.contentInMergedShapelets.get(i).get(size + 2).intValue();
                    shapeletVal = 0;
                    //XYSeries series = new XYSeries(i);
                    XYSeries series = new XYSeries(Integer.toString(i));
                    for (int j = 0; j < noOfColumns; j++) {
                        if (startPos > j) {
                            shapeletVal ++;
                        } else if (startPos + size < j) {
                            shapeletVal ++;
                        } else {
                            series.add(j,val.contentInMergedShapelets.get(i).get(j-shapeletVal));
                        }
                    }
                    int contentSize = val.contentInMergedShapelets.get(i).size();
                    output.write(val.contentInMergedShapelets.get(i).get(contentSize-2).intValue()+",");
                    output.write(contentSize-3+",");
                    output.write(val.contentInMergedShapelets.get(i).get(contentSize-3).intValue()+",");
                    output.write(val.contentInMergedShapelets.get(i).get(contentSize-1).intValue()+"\n");
                    dataset.addSeries(series);
                }
                output.close();

//                XYLineChart_AWT chart = new XYLineChart_AWT("Shapelet Learner", "Shapelets stats for event "+eventCount,dataset);
//                chart.pack( );
//                RefineryUtilities.centerFrameOnScreen( chart );
//                chart.setVisible( true );
            }
        }
    }*/
}