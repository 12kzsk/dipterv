package tech.artisanhub.ShapeletTrainerMD;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Properties;

import tech.artisanhub.ShapeletTrainer1D.ShapeletInputTester;

public class LogBasedShapeletTrainerMDFlights {

	//Main fv.: a log alapj�n megtal�lja a shapeleteket
	//Innen h�v�dik ennek a folyamatnak minden r�sze
	public static void main(String[] args) throws Exception {
		/*FlightDataProcessor.main(args);


		// propertyk beolvas�sa
				Properties prop = new Properties();
				InputStream input = null;
				input = new FileInputStream("config.properties");
				prop.load(input);
		
		// csv --> arff
		CSVtoARFFMD.TRAINING_DATA = prop.getProperty("logFileName");
		CSVtoARFFMD.OUTPUT_DATA = prop.getProperty("trainingDataFileNameArff");
		CSVtoARFFMD.main(args);
*/
		// learn shapelets
		LearnShapeletsMD.main(args);	
	}

}
