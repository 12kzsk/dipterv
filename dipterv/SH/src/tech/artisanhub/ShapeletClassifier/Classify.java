package tech.artisanhub.ShapeletClassifier;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Properties;

import tech.artisanhub.ARFFGenerator.CSVtoARFF;
import tech.artisanhub.ShapeletTrainer1D.ShapeletFilter;
import tech.artisanhub.ShapeletTrainerMD.DoubleVectorMD;
import tech.artisanhub.ShapeletTrainerMD.LogProcessorMD;
import tech.artisanhub.ShapeletTrainerMD.ShapeletFilterMD;
import weka.core.Instance;
import weka.core.Instances;

public class Classify {

	public static void main(String[] args) throws Exception {
		// Propertyk beolvas�sa
		Properties prop = new Properties();
		InputStream input = null;
		input = new FileInputStream("config.properties");
		prop.load(input);

		// READ DATA
		// csv to csv
		// magyar�z� v�ltoz�k
		ArrayList<String> inputVars = new ArrayList<String>();
		String inV = prop.getProperty("inputVariables");
		String[] inputVariables = inV.split(",");
		for (int i = 0; i < inputVariables.length; i++) {
			inputVars.add(inputVariables[i]);
		}

		//LogProcessorMD.csvToCsvConverter(inputVars, prop.getProperty("targetVariable"), prop.getProperty("inputDataOriginalFileNameCsv"), prop.getProperty("inputDataFileNameCsv"));
		
		// csv to arff
		CSVtoARFF.TRAINING_DATA = prop.getProperty("inputDataFileNameCsv");
		CSVtoARFF.OUTPUT_DATA = prop.getProperty("inputDataFileNameArff");
		CSVtoARFF.main(args);
		
		// TODO NEXT: classify line-t h�vjuk soronk�nt, soronk�nt olvasunk a
		// f�jlb�l, a shapeletet csak egyszer olvassuk be - ezt k�ne
		// megcsin�lni!!!!
		String shapeletSource = prop.getProperty("bestShapeletsFileName");
		Classifier cl = new Classifier();

		String cls = "";
		double th = -1;

		String line;
		DoubleVectorMD[] sh = null;

		try (InputStream fis = new FileInputStream(shapeletSource);
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
			sh = new DoubleVectorMD[shStr.length];
			// j: number of vectors in the shapelets
			for (int j = 0; j < shStr.length; j++) {
				DoubleVectorMD d = new DoubleVectorMD();
				String[] membersOfVector = shStr[j].split(",");
				// k: number of doubles in a vector
				for (int k = 0; k < membersOfVector.length; k++) {
					d.setElement(k, Double.parseDouble(membersOfVector[k]));
				}
				sh[j] = d;
			}

			// TODO sh normaliz�l�sa
			sh = ShapeletFilterMD.zNorm(sh, false);
		}

		Instances dataSet = ShapeletFilter.loadData(CSVtoARFF.OUTPUT_DATA);

		BufferedWriter writer = null;

		try {
			File outPut = new File("classifiedData.txt");

			writer = new BufferedWriter(new FileWriter(outPut));

			Double d = dataSet.instance(0).value(0);
			int dim = d.intValue();

			for (int i = 1; i < dataSet.numInstances(); i++) {
				// az�rt 1t�l,mert a 0. sor a dimenzi�
				// params: 1 id�sor, shapelet, shapelethez tartoz� oszt�ly,
				// shapelethez tartoz� threshold, id�sorok elemeinek dimenzi�ja
				writer.write(cl.classifyLine(dataSet.instance(i), sh, cls, th, dim));
				writer.newLine();
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				// Close the writer regardless of what happens...
				writer.close();
			} catch (Exception e) {
			}
		}
	}

}
