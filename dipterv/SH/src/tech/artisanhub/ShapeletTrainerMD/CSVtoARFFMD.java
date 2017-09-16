package tech.artisanhub.ShapeletTrainerMD;

import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.core.converters.CSVLoader;

import java.io.File;

public class CSVtoARFFMD {

//    private static final String TRAINING_DATA = "dataset/occupancy_withoutTime.data";
  //  private static final String OUTPUT_DATA = "dataset/occupancy_withoutTime.arff";


    //private static final String TRAINING_DATA = "baseline_out.csv";
    //private static final String OUTPUT_DATA = "baseline_out.arff";
	public static String TRAINING_DATA = "";
    public static String OUTPUT_DATA = "";

    public static void main(String[] args) throws Exception {

        // load CSV
        CSVLoader loader = new CSVLoader();
        loader.setSource(new File(TRAINING_DATA));
        Instances data = loader.getDataSet();
        System.out.println("Source CSV has been successfully loaded");

        // save ARFF
        ArffSaver saver = new ArffSaver();
        saver.setInstances(data);
        saver.setFile(new File(OUTPUT_DATA));
        saver.setDestination(new File(OUTPUT_DATA));
        saver.writeBatch();
        System.out.println("ARFF has been successfully generated");

    }
}
