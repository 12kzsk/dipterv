package tech.artisanhub.ARFFGenerator;

import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.core.converters.CSVLoader;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

import tech.artisanhub.ShapeletTrainerMD.CSVtoARFFMD;

public class CSVtoARFF {

//    private static final String TRAINING_DATA = "dataset/occupancy_withoutTime.data";
  //  private static final String OUTPUT_DATA = "dataset/occupancy_withoutTime.arff";

	public static String TRAINING_DATA = "";
    public static String OUTPUT_DATA = "";

    public static void main(String[] args) throws Exception {
		// propertyk beolvasása
		Properties prop = new Properties();
		InputStream input = null;
		input = new FileInputStream("config.properties");
		prop.load(input);
				
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
