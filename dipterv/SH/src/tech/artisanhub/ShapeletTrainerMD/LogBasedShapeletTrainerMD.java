package tech.artisanhub.ShapeletTrainerMD;

import java.util.ArrayList;
import java.util.Properties;

import tech.artisanhub.ShapeletTrainer1D.ShapeletInputTester;

public class LogBasedShapeletTrainerMD {

	//Main fv.: a log alapj�n megtal�lja a shapeleteket
	//Innen h�v�dik ennek a folyamatnak minden r�sze
	public static void main(String[] args) throws Exception {
		//config file-ban a param�terek nevei:
		//a f�jl nevek v�gz�d�ssel egy�tt vannak megadva
		/*
		 	logFileName
			trainingDataFileNameCsv
			trainingDataFileNameArff
			shapeletsFileName
			minShapeletLength
			maxShapeletLength
			dimension
			targetVariable
			criticalThreshold
			inputVariables (splitted: ,)
		 */

		//propertyk lek�rdez�se:
		/*	Properties prop = new Properties();
			InputStream input = null;
			input = new FileInputStream("config.properties");
			prop.load(input);
		*/

		LogProcessorMD.main(args);
	
	}

}
