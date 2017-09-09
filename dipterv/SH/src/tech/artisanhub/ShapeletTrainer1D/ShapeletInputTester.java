package tech.artisanhub.ShapeletTrainer1D;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;

import tech.artisanhub.ARFFGenerator.CSVtoARFF;

public class ShapeletInputTester {
	public static void main(String[] args) throws Exception {
		String input_csv = "baseline_SLA.csv";
		
		//************* 1 var START *************
		
		String inputVar = "HomesteadCPUUsage";
		PrintWriter pwSh1 = new PrintWriter(new File("result" + inputVar +".txt"));
		
		tester(inputVar, "RegisterRT", input_csv);
		
		//SHAPELETS
		//csv to arff
		CSVtoARFF.TRAINING_DATA = "baseline_out.csv";
		CSVtoARFF.OUTPUT_DATA =  "baseline_out.arff";
		CSVtoARFF.main(args);
		
		//learn shapelets
		LearnShapelets.ARFFName = "baseline_out.arff";
		LearnShapelets.main(args);
		
		//read from generated shapelets
		BufferedReader brSh1 = new BufferedReader(new FileReader("generatedShapelets.txt"));
		String lineSh1 = brSh1.readLine();
		String[] sh1 = lineSh1.split(",");
		brSh1.close();
		
		pwSh1.write("*infogain of *" + inputVar + "* is: *" + sh1[0] + "\n");
		pwSh1.close();
		
		//************* 1 var END *************
	
		
		/*
		//*************ALL vars START*************

		// init
		//BufferedReader brInit = new BufferedReader(new FileReader("baseline_SLA.csv"));
		BufferedReader brInit = new BufferedReader(new FileReader(input_csv));
		String lineInit = brInit.readLine();
		String[] headerInit = lineInit.split("\",\"");
		brInit.close();

		PrintWriter pwSh = new PrintWriter(new File("resultAll.txt"));
		
		// for all variables and get the "best" one (based on infogain)
		//for (int hd = headerInit.length-3; hd >= 0; hd--) {
		for (int hd = 0; hd < headerInit.length-2; hd++) {
			tester(headerInit[hd], "RegisterRT", input_csv);
			
			// lefuttatom a keletkezett output fájlra a
			// LearnShapelets.java-t
			// (elõször arffé alakítom és aztán)
			
			//SHAPELETS
			//csv to arff
			CSVtoARFF.TRAINING_DATA = "baseline_out.csv";
			CSVtoARFF.OUTPUT_DATA =  "baseline_out.arff";
			CSVtoARFF.main(args);
			
			//learn shapelets
			LearnShapelets.ARFFName = CSVtoARFF.OUTPUT_DATA;
			LearnShapelets.main(args);
			
			//read from generated shapelets
			BufferedReader brSh = new BufferedReader(new FileReader("generatedShapelets.txt"));
			String lineSh = brSh.readLine();
			String[] sh = lineSh.split(",");
			brSh.close();
			
			pwSh.write("*infogain of *" + headerInit[hd] + "* is: *" + sh[0] + "\n");
		}
		pwSh.close();
		//*************ALL vars END*************
		*/
	}
			public static void tester(String var, String tar, String input_csv_file) throws Exception{
			//if ((headerInit[hd].equals("\"WorkloadPhase\"")) || (headerInit[hd].equals("\"withinSLA\""))){}
			//else
			//tar != var

			//if (hd==0){
			//	headerInit[hd] = headerInit[hd].split("\"")[1];
			//	System.out.println(headerInit[hd]);
			//}	
			
			if (!(var.equals(tar))){// && !(headerInit[hd].equals("\"WorkloadPhase\"")) && !(headerInit[hd].equals("\"withinSLA\""))) {
				System.out.println("\n>>> Current column: " + var + "\n");
				// ***********************************************************************************

				// X: beolvasok 1 oszlopot a csv fájlból
				ArrayList<Double> X = new ArrayList<Double>();
				//timestamp oszlop
				ArrayList<Integer> tsCol = new ArrayList<Integer>();
				boolean overlap = false;
				
				String splitBy = ",";
				//BufferedReader brX = new BufferedReader(new FileReader("baseline_SLA.csv")); //TODO
				BufferedReader brX = new BufferedReader(new FileReader(input_csv_file));
				String line = brX.readLine();
				String[] header = line.split("\",\"");

				int tarCol = 0; // column of target variable
				int varCol = 0; // column of the selected variable
				boolean foundTar = false;
				boolean foundVar = false;
				for (int h = 0; h < header.length; h++) {
					// target
					if (!(header[h].equals(tar)) && !foundTar) {
						tarCol++;
					}

					if ((header[h].equals(tar)) && !foundTar) {
						foundTar = true;
					}
					// selected variable
					if (!(header[h].equals(var)) && !foundVar) {
						varCol++;
					}

					if ((header[h].equals(var)) && !foundVar) {
						foundVar = true;
					}
				}

				while ((line = brX.readLine()) != null) {
					String[] b = line.split(splitBy);
					// X = b[oszlopsorszam]
					X.add(Double.parseDouble(b[varCol]));
					// timestamp oszlop feltöltése (0.oszlop)
					tsCol.add(Integer.parseInt(b[0]));
				}
				brX.close();

				// Y: beolvasom a RegisterRT értékét a csv fájlból
				ArrayList<Double> Y = new ArrayList<Double>();
				//BufferedReader brY = new BufferedReader(new FileReader("baseline_SLA.csv"));
				BufferedReader brY = new BufferedReader(new FileReader(input_csv_file));
				line = brY.readLine();
				while ((line = brY.readLine()) != null) {
					String[] b = line.split(splitBy);
					// Y = b[oszlopsorszam]
					Y.add(Double.parseDouble(b[tarCol])); // tarCol = column of
															// target variable
				}
				brY.close();

				PrintWriter pw = new PrintWriter(new File("baseline_out.csv"));
				StringBuilder outputHeader = new StringBuilder();
				int attNum = 0;

				for (int k = 0; 60 + k * 61 < Y.size(); k++) {
					overlap = false; //új minta, új esély
					
					// X1: veszem az elsõ 50 adatot X-bõl
					StringBuilder sb1 = new StringBuilder();
					ArrayList<Double> X1 = new ArrayList<Double>();

					for (int i = k * 61; i < k * 61 + 50; i++) {
						if (tsCol.get(i+1) <= tsCol.get(i)  && (i < k * 61 + 50 - 1)){
							overlap = true;
							i = k*61+50; //lépjen ki és az overlap maradjon true
						}
						X1.add(X.get(i));
						sb1.append(X.get(i) + ",");
						outputHeader.append("attr" + attNum + ",");
						attNum++;
					}

					// Y1: veszem az 51-60. adatokat Yból
					ArrayList<Double> Y1 = new ArrayList<Double>();
					for (int i = 50 + k * 61; i < 60 + k * 61; i++) {
						if ((tsCol.get(i+1) <= tsCol.get(i)) && (i < 60 + k * 61 - 1)){
							overlap = true;
							i = 60 + k * 61; //lépjen ki és az overlap maradjon true
						}
						Y1.add(Y.get(i));
					}

					int count = 0;
					for (int x = 0; x < Y1.size(); x++) {
						if (Y1.get(x) >= 1000)
							count++;
					}

					if (count >= Y1.size() - 1) {
						sb1.append("1\n");
						outputHeader.append("attr" + attNum + "\n");
						attNum++;
					} else {
						sb1.append("0\n");
						outputHeader.append("attr" + attNum + "\n");
						attNum++;
					}
					// System.out.println(sb1.toString());
					if (k == 0)
						pw.write(outputHeader.toString());
					if (!overlap){	//csak akkor írja ki, ha nem volt overlap, ha volt, keresse a következõ mintát
						pw.write(sb1.toString());
					}

				}
				// output fájl 1 sora: X1, illetve még 1: utána ha Y1 minden
				// adata
				// 1000
				// felett van, 1, egyébként 0
				pw.close();
			}
	}
}
