package tech.artisanhub.ShapeletTrainer1D;

import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.RefineryUtilities;
import tech.artisanhub.Visualizer.XYLineChart_AWT;
import weka.core.Instances;

import java.io.*;
import java.util.ArrayList;
import java.util.Map;

public class LearnShapelets
{
	public static String ARFFName = "dataset/EEG_Eye_State_short.arff"; //changed: - to public static and moved from first line of try
	
    public static void main( String[] args )
    {       long startTime = System.currentTimeMillis();
        try {
            Instances data = ShapeletFilter.loadData(ARFFName);

            int k = Integer.MAX_VALUE; // number of shapelets
            int minLength = 2;
//          int maxLength = data.get(1).numValues()-1;
            int maxLength = data.instance(1).numValues()-1; //TODO
            //int maxLength = 50;

            String outPutFile = "generatedShapelets.txt";
            ShapeletFilter sf = new ShapeletFilter(k, minLength, maxLength);
            sf.setLogOutputFile(outPutFile); // log file stores shapelet output
            ArrayList<Shapelet> generatedShapelets = sf.process(data);


            Map<Double,Integer> classDist = ShapeletFilter.getClassDistributions(data);
            ArrayList<Integer> arr = new ArrayList<Integer>(); //arr = class ids
            for (double val: classDist.keySet()){
                arr.add((int)val);
            }

            int shapeletClusterSize = (int) Math.sqrt(generatedShapelets.size()); //this defines the threshold. Put a larger number to detect all the events
            ArrayList<Shapelet> mergedShapelets = new MergeShapelets().mergeShapelets(generatedShapelets,shapeletClusterSize); //meerging shapelets
            ArrayList<Shapelet> finalOutputShapelets = new ImportantShapelets().GetImportantShapelets(mergedShapelets,data,arr); //find important shapelets
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

    private static void displayShapeletStats(ArrayList<Shapelet> shapelets,int noOfColumns) throws IOException {
        int size = 0;
        int startPos =0;
        int shapeletVal = 0;
        int sizeOfTheShapelet = 0;
        int eventCount =0;
        BufferedWriter output = null;
        for(Shapelet val : shapelets){
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
    }
}