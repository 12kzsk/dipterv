package tech.artisanhub.ShapeletTrainerMD;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Properties;

public class LogProcessorMDSVM {
	public static void main(String[] args) throws Exception {
		// propertyk beolvas�sa
		Properties prop = new Properties();
		InputStream input = null;
		input = new FileInputStream("config.properties");
		prop.load(input);

		// log input f�jl
		String logFile = prop.getProperty("logFileName");

		// magyar�z� v�ltoz�k
		ArrayList<String> inputVars = new ArrayList<String>();
		String inV = prop.getProperty("inputVariables");
		String[] inputVariables = inV.split(",");
		for (int i = 0; i < inputVariables.length; i++) {
			inputVars.add(inputVariables[i]);
		}

		// csv --> megfelel� form�tum� input file
		csvToInputConverter(inputVars, prop.getProperty("targetVariable"), logFile,
//				prop.getProperty("SVMInput").toString());
				"RT_Reg_P.txt");

		// learn shapelets
		//LearnShapeletsMD.main(args);
	}

	public static void csvToInputConverter(ArrayList<String> vars, String tar, String inputFile, String outputFile)
			throws Exception {
		// propertyk beolvas�sa
		Properties prop = new Properties();
		InputStream input = null;
		input = new FileInputStream("config.properties");
		prop.load(input);

		// if (!(vars.contains((tar)))) {
		// X: beolvasok 1 oszlopot a csv f�jlb�l
		ArrayList<ArrayList<Double>> X = new ArrayList<ArrayList<Double>>();
		// timestamp oszlop
		ArrayList<Double> tsCol = new ArrayList<Double>();
		// van-e overlap, azaz egy m�r�sb�l vannak-e az adatok vagy sem
		boolean overlap = false;

		String splitBy = ";";

		// els� sor beolvas�sa (header)
		BufferedReader brX = new BufferedReader(new FileReader(inputFile));
		String line = brX.readLine();
		// header: v�ltoz�neveket tartalmazza
		String[] header = line.split(";");

		// c�lv�ltoz� oszlop�nak sorsz�ma
		int tarCol = 0;
		// input v�ltoz�k oszlopainak sorsz�mai
		ArrayList<Integer> varCol = new ArrayList<Integer>();
		for (int x = 0; x < vars.size(); x++) {
			varCol.add(x, 0);
		}

		// seg�dv�ltoz�k
		boolean foundTar = false;
		boolean foundVar = false;

		// timestamp oszlop index�nek megkeres�se
		boolean foundTsCol = false;
		int tsColIndex = 0;
		for (int h = 0; h < header.length; h++) {
			String hdr = header[h];
			String tsc = prop.get("timestampColumnName").toString();
			if (!(hdr.equals(tsc)) && !foundTsCol) {
				tsColIndex++;
			}

			if (hdr.equals(tsc) && !foundTsCol) {
				foundTsCol = true;
				h = header.length; // ha megtal�lta, l�pjen ki
			}
		}

		// megkeress�k, hogy a c�lv�ltoz� h�nyas index� oszlopban van
		for (int h = 0; h < header.length; h++) {
			if (!(header[h].equals(tar)) && !foundTar) {
				tarCol++;
			}

			if ((header[h].equals(tar)) && !foundTar) {
				foundTar = true;
			}
		}

		// megkeress�k, hogy a magyar�z� v�ltoz�(k) h�nyas index�
		// oszlop(ok)ban vannak
		for (int l = 0; l < varCol.size(); l++) {
			foundVar = false;
			for (int h = 0; h < header.length; h++) {
				if ((header[h].equals(vars.get(l))) && !foundVar) {
					varCol.set(l, h);
					foundVar = true;
				}
			}
		}

		// minden sorb�l beolvassa a megadott v�ltoz�khoz tartoz� �rt�keket
		// ezeket az X-hez adja,
		// ez az X az �sszes vektort tartalmazza
		// X: Double ArrayListek ArrayListje
		// ezeken k�v�l:
		// Y: beolvasom a c�lv�ltoz� �rt�k�t a csv f�jlb�l
		ArrayList<Double> Y = new ArrayList<Double>();
		while ((line = brX.readLine()) != null) {
			String[] b = line.split(splitBy);
			// X = b[oszlopsorszam]
			ArrayList<Double> vector = new ArrayList<Double>();
			for (int x = 0; x < varCol.size(); x++) {
				try {
					vector.add(Double.parseDouble(b[varCol.get(x)]));
				} catch (Exception e) {
					// ha nem sz�m van benne,
					// rakjon bele -1-t
					vector.add(-1.0);
				}
			}
			X.add(vector);

			// timestamp oszlop felt�lt�se (0.oszlop)
			// tsCol.add(Integer.parseInt(b[0]));
			tsCol.add(Double.parseDouble(b[tsColIndex]));

			Y.add(Double.parseDouble(b[tarCol]));
		}
		brX.close();

		PrintWriter pw = new PrintWriter(new File(outputFile));
		//StringBuilder outputHeader = new StringBuilder();
		// seg�dv�ltoz�: ahhoz sz�ks�ges, hogy
		// a csvb�l szab�lyos arff f�jlt lehessen csin�lni
//		int attNum = 0;

		//int timeseriesLength = Integer.parseInt(prop.get("timeseriesLength").toString());
		int timeseriesLength = 1;
		int classifierInterval = Integer.parseInt(prop.get("classifierInterval").toString());
		int sampleGap = Integer.parseInt(prop.get("sampleGap").toString());
		int smoothingLength = Integer.parseInt(prop.get("smoothingLength").toString());

		boolean hdr = true; // header-e

		for (int k = 0; k < Y.size() - (timeseriesLength + classifierInterval + sampleGap); k++) {
			overlap = false; // �j minta, �j es�ly

			// megvizsg�ljuk, hogy van-e overlap, azaz
			// az id�ablakban ne legyen k�t k�l�nb�z� m�r�sb�l adat
			for (int i = k; i < (k + timeseriesLength + classifierInterval + sampleGap); i++) {
				// az�rt kell a -1, mert az utols� olyann�l, amit vizsg�lunk
				// nincs m�g k�vetkez� vizsg�land� timestamp
				if ((i < k + timeseriesLength + classifierInterval + sampleGap - 1)
						&& (tsCol.get(i + 1) <= tsCol.get(i))) {
					overlap = true;
					i = k + timeseriesLength + classifierInterval + sampleGap;
					// l�pjen ki �s az overlap maradjon true
				}
			}

			// csak akkor �rja ki, ha nem volt overlap,
			// ha volt, keresse a k�vetkez� mint�t
			if (!overlap) {

				// outputLine: ez lesz a f�jl egy sora
				StringBuilder outputLine = new StringBuilder();
				// veszem az els� timeseriesLength mennyis�g� adatot X-b�l
				for (int i = k; i < (k + timeseriesLength); i++) {
					for (int j = 0; j < X.get(i).size(); j++) {
						int num = j+1;
						if (j == X.get(i).size()-1) {
							outputLine.append(num + ":" + X.get(i).get(j));
						}
						else
							outputLine.append(num + ":" + X.get(i).get(j) + " ");
					}
				}

				// Y1: a classifierIntervalnak megfelel� adatokat beleteszem
				// Y-b�l
				/*
				 * ArrayList<Double> Y1 = new ArrayList<Double>(); for (int i =
				 * k + timeseriesLength + sampleGap; i < k + timeseriesLength +
				 * classifierInterval + sampleGap; i++) { Y1.add(Y.get(i)); }
				 */
				ArrayList<Double> Y1 = new ArrayList<Double>();
				for (int i = k + timeseriesLength + sampleGap; i < k + timeseriesLength + classifierInterval
						+ sampleGap; i++) {
					// sim�t�s
					double sum = 0.0;
					for (int j = i - smoothingLength + 1; j < i + 1; j++) {
						sum += Y.get(j);
					}
					Y1.add(sum / smoothingLength);
				}

					// �sszesz�moljuk, hogy h�ny van threshold felett ezen a
					// szakaszon
					int count = 0;
					for (int x = 0; x < Y1.size(); x++) {
						// ha mindegyik criticalThreshold felett van ezen a
						// szakaszon, akkor lesz
						// criticalClass oszt�lyba tartoz�
						if (Y1.get(x) >= Integer.parseInt(prop.get("criticalThreshold").toString()))
							count++;
					}

					if (count == Y1.size()) {
						outputLine.insert(0, prop.get("criticalClass").toString() + " ");
					} else {
						outputLine.insert(0, prop.get("normalClass").toString() + " ");
					}

					// ahhoz, hogy a csv-t szab�lyos arff f�jll� lehessen
					// alak�tani,
					// kellenek attrib�tumnevek
					// ez az oszt�ly attrib�tumneve
					if (hdr) {
					//	outputHeader.append("attr" + attNum + "\n");
					//	attNum++;

						// a legelej�n �rja ki a headert is az output f�jlba
					//	pw.write(outputHeader.toString());
					}

					hdr = false;

					// a f�jl aktu�lisan �ssze�ll�tott sor�t ki�rja
					outputLine.append("\n");
					pw.write(outputLine.toString());
//				}

//				int end = outputHeader.length();
//				outputHeader.delete(0, end);
			}

		}
		// output f�jl 1 sora:
		// X1, illetve m�g 1:
		// ut�na ha Y1 minden adata 1000 felett van, 1, egy�bk�nt 0
		pw.close();
		// }
	}
}