package tech.artisanhub.ShapeletTrainerMD;

/*
 * NOTE: As shapelet extraction can be time consuming, there is an option to output shapelets
 * to a text file (Default location is in the root dir of the project, file name "defaultShapeletOutput.txt").
 *
 * Default settings are TO PRODUCE OUTPUT FILE - unless file name is changed, each successive filter will
 * overwrite the output (see "setLogOutputFile(String fileName)" to change file dir and name).
 *
 * To reconstruct a filter from this output, please see the method "createFilterFromFile(String fileName)".
 */

import weka.core.*;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.*;



public class ShapeletFilterMD {
	
	private int minShapeletLength;
	private int maxShapeletLength;
	private int numShapelets;
	private boolean shapeletsTrained;
	private ArrayList<ShapeletMD> shapelets;
	private String ouputFileLocation = "defaultShapeletOutput.txt"; // default
																	// store
																	// location
	private String classOutputFileLocation = "defaultClassShapeletOutput.txt"; // default
	// store
	// location
	private boolean recordShapelets = true; // default action is to write an
											// output file

	public ShapeletFilterMD() {
		this.minShapeletLength = -1;
		this.maxShapeletLength = -1;
		this.numShapelets = -1;
		this.shapeletsTrained = false;
		this.shapelets = null;
	}

	/**
	 *
	 * @param k
	 *            - the number of shapelets to be generated
	 */
	public ShapeletFilterMD(int k) {
		this.minShapeletLength = -1;
		this.maxShapeletLength = -1;
		this.numShapelets = k;
		this.shapelets = new ArrayList<ShapeletMD>();
		this.shapeletsTrained = false;
	}

	/**
	 *
	 * @param k
	 *            - the number of shapelets to be generated
	 * @param minShapeletLength
	 *            - minimum length of shapelets
	 * @param maxShapeletLength
	 *            - maximum length of shapelets
	 */
	public ShapeletFilterMD(int k, int minShapeletLength, int maxShapeletLength) {
		this.minShapeletLength = minShapeletLength;
		this.maxShapeletLength = maxShapeletLength;
		this.numShapelets = k;
		this.shapelets = new ArrayList<ShapeletMD>();
		this.shapeletsTrained = false;
	}

	/**
	 *
	 * @param k
	 *            - the number of shapelets to be generated
	 */
	public void setNumberOfShapelets(int k) {
		this.numShapelets = k;
	}

	/**
	 *
	 * @param minShapeletLength
	 *            - minimum length of shapelets
	 * @param maxShapeletLength
	 *            - maximum length of shapelets
	 */
	public void setShapeletMinAndMax(int minShapeletLength, int maxShapeletLength) {
		this.minShapeletLength = minShapeletLength;
		this.maxShapeletLength = maxShapeletLength;
	}

	/**
	 *
	 * @param inputFormat
	 *            - the format of the input data
	 * @return a new Instances object in the desired output format
	 * @throws Exception
	 *             - if all required attributes of the filter are not
	 *             initialised correctly
	 */
	protected Instances determineOutputFormat(Instances inputFormat) throws Exception {

		if (this.numShapelets < 1) {
			throw new Exception(
					"ShapeletFilter not initialised correctly - please specify a value of k that is greater than 1");
		}

		// Set up instances size and format.
		int length = this.numShapelets;
		// FastVector<Attribute> atts = new FastVector();
		FastVector atts = new FastVector();
		String name;
		for (int i = 0; i < length; i++) {
			name = "Shapelet_" + i;
			atts.addElement(new Attribute(name));
		}

		if (inputFormat.classIndex() >= 0) { // Classification set, set class
			// Get the class values as a fast vector
			Attribute target = inputFormat.attribute(inputFormat.classIndex());

			FastVector vals = new FastVector(target.numValues());
			for (int i = 0; i < target.numValues(); i++) {
				vals.addElement(target.value(i));
			}
			atts.addElement(new Attribute(inputFormat.attribute(inputFormat.classIndex()).name(), vals));
		}
		Instances result = new Instances("Shapelets" + inputFormat.relationName(), atts, inputFormat.numInstances());
		if (inputFormat.classIndex() >= 0) {
			result.setClassIndex(result.numAttributes() - 1);
		}
		return result;
	}

	/**
	 *
	 * @param data
	 *            - the input data to be transformed (and to find the shapelets
	 *            if this is the first run)
	 * @return the transformed Instances in terms of the distance from each
	 *         shapelet
	 * @throws Exception
	 *             - if the number of shapelets or the length parameters
	 *             specified are incorrect
	 */
	public ArrayList<ShapeletMD> process(Instances data, int dim) throws Exception {
		if (this.numShapelets < 1) {
			throw new Exception(
					"Number of shapelets initialised incorrectly - please select value of k (Usage: setNumberOfShapelets");
		}

		int maxPossibleLength;
		if (data.classIndex() < 0)
			maxPossibleLength = data.instance(0).numAttributes();
		else
			maxPossibleLength = data.instance(0).numAttributes() - 1;

		if (this.minShapeletLength < 1 || this.maxShapeletLength < 1 || this.maxShapeletLength < this.minShapeletLength
				|| this.maxShapeletLength > maxPossibleLength) {
			throw new Exception("Shapelet length parameters initialised incorrectly");
		}

		if (this.shapeletsTrained == false) { // shapelets discovery has not yet
												// been carried out, so do
			// so
			this.shapelets = findBestKShapeletsCache(this.numShapelets, data, this.minShapeletLength,
					this.maxShapeletLength, dim); // get k shapelets ATTENTION
			this.shapeletsTrained = true;
			System.out.println("\n" + shapelets.size() + " Shapelets have been generated");
		}

		// Instances output = determineOutputFormat(data);
		//
		// // for each data, get distance to each shapelet and create new
		// instance
		// for (int i = 0; i < data.numInstances(); i++) { // for each data
		// Instance toAdd = new SparseInstance(this.shapelets.size() + 1);
		// int shapeletNum = 0;
		// for (Shapelet s : this.shapelets) {
		// double dist = subsequenceDistance(s.content, data.instance(i));
		// toAdd.setValue(shapeletNum++, dist);
		// }
		// toAdd.setValue(this.shapelets.size(), data.instance(i).classValue());
		// output.add(toAdd);
		// }
		return shapelets;
	}

	public void setLogOutputFile(String fileName) {
		this.recordShapelets = true;
		this.ouputFileLocation = fileName;
	}

	public void setClassOutputFile(String fileName) {
		this.classOutputFileLocation = fileName;
	}

	public void turnOffLog() {
		this.recordShapelets = false;
	}

	/**
	 *
	 * @param numShapelets
	 *            - the target number of shapelets to generate
	 * @param data
	 *            - the data that the shapelets will be taken from
	 * @param minShapeletLength
	 *            - the minimum length of possible shapelets
	 * @param maxShapeletLength
	 *            - the maximum length of possible shapelets
	 * @return an ArrayList of Shapelet objects in order of their fitness (by
	 *         infoGain, seperationGap then shortest length)
	 */
	private ArrayList<ShapeletMD> findBestKShapeletsCache(int numShapelets, Instances data, int minShapeletLength,
			int maxShapeletLength, int dim) throws Exception {

		long startTime = System.nanoTime();
		DoubleVectorMD[] rawContent;

		ArrayList<ShapeletMD> kShapelets = new ArrayList<ShapeletMD>(); // store
																		// (upto)
																		// the
																		// best
																		// k
																		// shapelets
		// overall
		ArrayList<ShapeletMD> seriesShapelets = new ArrayList<ShapeletMD>(); // temp
																			// store
																			// of
																			// all
																			// shapelets
		// for each time series

		/*
		 * new version to allow caching: - for all time series, calculate the
		 * gain of all candidates of all possible lengths - insert into a
		 * strucutre in order of fitness - arraylist with comparable
		 * implementation of shapelets - once all candidates for a series are
		 * established, integrate into store of k best
		 */

		TreeMap<Double, Integer> classDistributions = getClassDistributions(data); // used
																					// to
																					// calc
																					// info
		// gain

		// for all time series
		System.out.println("Processing data: ");
		int numInstances = data.numInstances();
		for (int i = 0; i < numInstances; i++) {

			//if (i == 0 || i % (numInstances / 4) == 0) {
				System.out.println("Currently processing instance " + (i + 1) + " of " + numInstances);
			//}

				
			// TODO: átalakítás
			DoubleVectorMD[] wholeCandidate = doubleArrayToDoubleVectorArray(data.instance(i).toDoubleArray());

			seriesShapelets = new ArrayList<ShapeletMD>();

			//TODO: ITT KELL INICIALIZÁLNI A MAXINFOGAINT és utána..next TODO
			
			for (int length = minShapeletLength; length <= maxShapeletLength; length++) {

				// for all possible starting positions of that length
				for (int start = 0; start <= wholeCandidate.length - length - 1; start++) { // -1
																							// =
																							// avoid
					// classVal -
					// handle later
					// for series
					// with no class
					// val
					// CANDIDATE ESTABLISHED - got original series, length and
					// starting position
					// extract relevant part into a double[] for processing
					DoubleVectorMD[] candidate = new DoubleVectorMD[length];
					rawContent = new DoubleVectorMD[length + 1];
					for (int m = start; m < start + length; m++) {
						candidate[m - start] = wholeCandidate[m];
						rawContent[m - start] = wholeCandidate[m];
					}

					// znorm candidate here so it's only done once, rather than
					// in each distance calculation
					rawContent[length] = new DoubleVectorMD();
					rawContent[length].setElement(0, data.instance(i).classValue());
					candidate = zNorm(candidate, false);
					//TODO itt kell megnézni, hogy nagyobb-e az eddigi maxinfogainnél a shapelet infogainje és belerakni ha igen (a maxba)
					ShapeletMD candidateShapelet = checkCandidate(candidate, data, i, start, classDistributions, rawContent, dim);

					seriesShapelets.add(candidateShapelet);
				}
			}
			// now that we have all shapelets, self similarity can be fairly
			// assessed without fear of
			// removing potentially
			// good shapelets
			Collections.sort(seriesShapelets);
			seriesShapelets = removeSelfSimilar(seriesShapelets);
			kShapelets = combine(numShapelets, kShapelets, seriesShapelets);
		}

		if (this.recordShapelets) {
			FileWriter out = new FileWriter(this.ouputFileLocation);

			// simple output (only the best shapelet with only the necessary
			// info)
			FileWriter classOut = new FileWriter(this.classOutputFileLocation);
			Double th = kShapelets.get(0).splitThreshold;
			classOut.append(th.toString() + "#");

			DoubleVectorMD[] shapeletRC = kShapelets.get(0).rawContent;

			for (int j = 0; j < shapeletRC.length - 1; j++) {
				for (int l = 0; l < LearnShapeletsMD.vectorSize; l++) {
					if (l == LearnShapeletsMD.vectorSize - 1) {
						classOut.append(shapeletRC[j].getElement(l).toString());
					} else
						classOut.append(shapeletRC[j].getElement(l) + ",");
				}
				classOut.append(";");
			}
			classOut.append(shapeletRC[shapeletRC.length - 1].getElement(0) + "\n");

			classOut.close();

			// complex output
			for (int i = 0; i < kShapelets.size(); i++) {
				//
				out.append(kShapelets.get(i).informationGain + "," + kShapelets.get(i).seriesId + ","
						+ kShapelets.get(i).startPos + "\n");
				/*
				 * Uncomment this code block to write information gain to the
				 * file
				 */
				// double[] shapeletContent = kShapelets.get(i).content;
				//
				// for (int j = 0; j < shapeletContent.length; j++) {
				// out.append(shapeletContent[j] + ",");
				// }

				/*
				 * Uncomment this code block to write raw content of the
				 * shapelet to the file
				 */
				DoubleVectorMD[] shapeletRawContent = kShapelets.get(i).rawContent;

				for (int j = 0; j < shapeletRawContent.length - 1; j++) {
					for (int l = 0; l < LearnShapeletsMD.vectorSize; l++) {
						if (l == LearnShapeletsMD.vectorSize - 1) {
							out.append(shapeletRawContent[j].getElement(l).toString());
						} else
							out.append(shapeletRawContent[j].getElement(l) + ",");
					}
					out.append(";");
				}
				out.append(shapeletRawContent[shapeletRawContent.length - 1].getElement(0) + "\n");
			}
			out.close();
		}

		return kShapelets;
	}

	/**
	 *
	 * @param shapelets
	 *            the input Shapelets to remove self similar Shapelet objects
	 *            from
	 * @return a copy of the input ArrayList with self-similar shapelets removed
	 */
	private static ArrayList<ShapeletMD> removeSelfSimilar(ArrayList<ShapeletMD> shapelets) {
		// return a new pruned array list - more efficient than removing
		// self-similar entries on the fly and constantly reindexing
		ArrayList<ShapeletMD> outputShapelets = new ArrayList<ShapeletMD>();
		boolean[] selfSimilar = new boolean[shapelets.size()];

		// to keep track of self similarity - assume nothing is similar to begin
		// with
		for (int i = 0; i < shapelets.size(); i++) {
			selfSimilar[i] = false;
		}

		for (int i = 0; i < shapelets.size(); i++) {
			if (selfSimilar[i] == false) {
				outputShapelets.add(shapelets.get(i));
				for (int j = i + 1; j < shapelets.size(); j++) {
					if (selfSimilar[j] == false && selfSimilarity(shapelets.get(i), shapelets.get(j))) { // no
						// point
						// recalc'ing
						// if
						// already
						// self
						// similar
						// to
						// something
						selfSimilar[j] = true;
					}
				}
			}
		}
		return outputShapelets;
	}

	/**
	 *
	 * @param k
	 *            the maximum number of shapelets to be returned after combining
	 *            the two lists
	 * @param kBestSoFar
	 *            the (up to) k best shapelets that have been observed so far,
	 *            passed in to combine with shapelets from a new series
	 * @param timeSeriesShapelets
	 *            the shapelets taken from a new series that are to be merged in
	 *            descending order of fitness with the kBestSoFar
	 * @return an ordered ArrayList of the best k (or less) Shapelet objects
	 *         from the union of the input ArrayLists
	 */

	// NOTE: could be more efficient here
	private ArrayList<ShapeletMD> combine(int k, ArrayList<ShapeletMD> kBestSoFar,
			ArrayList<ShapeletMD> timeSeriesShapelets) {

		ArrayList<ShapeletMD> newBestSoFar = new ArrayList<ShapeletMD>();
		for (int i = 0; i < timeSeriesShapelets.size(); i++) {
			kBestSoFar.add(timeSeriesShapelets.get(i));
		}
		Collections.sort(kBestSoFar);
		if (kBestSoFar.size() < k)
			return kBestSoFar; // no need to return up to k, as there are not k
								// shapelets yet

		for (int i = 0; i < k; i++) {
			newBestSoFar.add(kBestSoFar.get(i));
		}

		return newBestSoFar;
	}

	/**
	 *
	 * @param data
	 *            the input data set that the class distributions are to be
	 *            derived from
	 * @return a TreeMap<Double, Integer> in the form of <Class Value,
	 *         Frequency>
	 */
	public static TreeMap<Double, Integer> getClassDistributions(Instances data) {
		TreeMap<Double, Integer> classDistribution = new TreeMap<Double, Integer>();
		double classValue;
		for (int i = 0; i < data.numInstances(); i++) {
			classValue = data.instance(i).classValue();
			boolean classExists = false;
			for (Double d : classDistribution.keySet()) {
				if (d == classValue) {
					int temp = classDistribution.get(d);
					temp++;
					classDistribution.put(classValue, temp);
					classExists = true;
				}
			}
			if (classExists == false) {
				classDistribution.put(classValue, 1);
			}
		}
		System.out.println(classDistribution.get(0.0) + " <--> " + classDistribution.get(1.0)); // TODO
		return classDistribution;
	}

	/**
	 *
	 * @param candidate
	 *            the data from the candidate Shapelet
	 * @param data
	 *            the entire data set to compare the candidate to
	 * @param data
	 *            the entire data set to compare the candidate to
	 * @return a TreeMap<Double, Integer> in the form of <Class Value,
	 *         Frequency>
	 */
	
	//NEM egy candidatenél kell nézni a maxInfoGaint, hanem a candidateek között --> át kell adni paraméterként az eddigi maxot
	private static ShapeletMD checkCandidate(DoubleVectorMD[] candidate, Instances data, int seriesId, int startPos,
			TreeMap classDistribution, DoubleVectorMD[] rawContent, int dim) {
		// create orderline by looping through data set and calculating the
		// subsequence
		// distance from candidate to all data, inserting in order.
		ArrayList<OrderLineObjMD> orderline = new ArrayList<OrderLineObjMD>();

		double maxInfoGain = -1;
		double maxDist = -1;
		boolean endFor = false;
		boolean pruning = false; //SET PRUNING

		for (int i = 0; i < data.numInstances(); i++) {
			if (!endFor) {
				double distance = subsequenceDistance(candidate, data.instance(i), dim);
				
				double classVal = data.instance(i).classValue();

				if (maxDist < distance)
					maxDist = distance;

				// without early abandon, it is faster to just add and sort at
				// the
				// end

				// ENTROPY PRUNING
				if (pruning) {
					// 1.lemásoljuk az aktuális orderlinet
					ArrayList<OrderLineObjMD> orderlineCopy1 = new ArrayList<OrderLineObjMD>();
					orderlineCopy1.addAll(orderline);
					// 2.amit még nem számoltunk ki (=a ciklus maradékában)

					for (int j = i; j < data.numInstances(); j++) {
						// ahol a classVal 0.0 ott 0 távolságot adunk meg
						double classValCopy = data.instance(j).classValue();
						double distanceCopy = 0.0;
						if (classValCopy == 0.0) {
							distanceCopy = 0;
						}
						// ahol a classVal 1.0 ott az eddigi legnagyobb
						// távolságot
						// adjuk meg (ezt nyilván kell tartani!!!!)
						else
							distanceCopy = maxDist;

						orderlineCopy1.add(new OrderLineObjMD(distanceCopy, classValCopy));

					}

					Collections.sort(orderlineCopy1, new orderLineComparator());

					// 3.így kiszámoljuk az infogaint
					// (calcInfoGainAndThreshold()
					// /classDistribution u.az/
					ShapeletMD shapeletCopy1 = new ShapeletMD(candidate, seriesId, startPos);
					shapeletCopy1.rawContent = rawContent;
					shapeletCopy1.calcInfoGainAndThreshold(orderlineCopy1, classDistribution);
					double iG1 = shapeletCopy1.informationGain;
					// 3.5!!!!!!!!!!!!!!u.ezt végig kell csinálni úgy is, hogy
					// 0.0nál adjuk meg a legnagyobbat és 1.0nál 0-t
					ArrayList<OrderLineObjMD> orderlineCopy2 = new ArrayList<OrderLineObjMD>();
					orderlineCopy2.addAll(orderline);
					for (int j = i; j < data.numInstances(); j++) {
						// ahol a classVal 0.0 ott 0 távolságot adunk meg
						double classValCopy = data.instance(j).classValue();
						double distanceCopy = 0.0;
						if (classValCopy == 0.0) {
							distanceCopy = 0;
						}
						// ahol a classVal 1.0 ott az eddigi legnagyobb
						// távolságot
						// adjuk meg (ezt nyilván kell tartani!!!!)
						else
							distanceCopy = maxDist;

						orderlineCopy2.add(new OrderLineObjMD(distanceCopy, classValCopy));

					}

					Collections.sort(orderlineCopy2, new orderLineComparator());

					// 3.így kiszámoljuk az infogaint
					// (calcInfoGainAndThreshold()
					// /classDistribution u.az/
					ShapeletMD shapeletCopy2 = new ShapeletMD(candidate, seriesId, startPos);
					shapeletCopy2.rawContent = rawContent;
					shapeletCopy2.calcInfoGainAndThreshold(orderlineCopy2, classDistribution);
					double iG2 = shapeletCopy2.informationGain;
					// 3.75 MEG KELL NÉZNI, HOGY A KÉT INFOGAIN KÖZÜL MELYIK A
					// NAGYOBB ÉS AZZAL KELL DOLGOZNI A 4-ES PONTNÁL
					double iG12max = -99999.0;
					if (iG1 > iG2)
						iG12max = iG1;
					else
						iG12max = iG2;

					// 4.ha az így kiszámolt infogain kisebb, mint az eddigi
					// legjobb
					// (ennél már csak kisebb lehet, úgy van szétosztva)
					// akkor nem folytatjuk tovább (kilépünk a ciklusból? iffel
					// oldjuk meg mint a másiknál? vmi ilyesmi
					if (iG12max < maxInfoGain)
						endFor = true;
					//NEM endFor = true HANEM return shapeletCopy1 vagy 2 az attól függ h melyik a nagyobb infogain; TODO

					// ellenõrzés: bool változóval szabályozni hogy melyik
					// módszert
					// használjuk és ha u.az jön ki, akkor jó (fõleg ha
					// gyorsabban
					// is fut, akkor pláne)
					// +1 RENDEZNI KELL KÖZBEN IS COLLECTIONS.SORT IZÉVEL (lásd
					// 2
					// sorral lejjebb)
				}

				orderline.add(new OrderLineObjMD(distance, classVal));
			}
		}
		Collections.sort(orderline, new orderLineComparator());

		// create a shapelet object to store all necessary info, i.e.
		// content, seriesId, then calc info gain, plit threshold and separation
		// gap
		ShapeletMD shapelet = new ShapeletMD(candidate, seriesId, startPos);
		shapelet.rawContent = rawContent;
		shapelet.calcInfoGainAndThreshold(orderline, classDistribution);

		if (shapelet.informationGain > maxInfoGain) 
			//TODO ezt nem is itt kell nézni hanem kívül (emiatt át kell adni paramként a maxinfogaint)
			maxInfoGain = shapelet.informationGain;
		// note: early abandon entropy pruning would appear here, but has been
		// ommitted
		// in favour of a clear multi-class information gain calculation. Could
		// be added in
		// this method in the future for speed up, but distance early abandon is
		// more important
		
		return shapelet;
	}

	// for sorting the orderline
	private static class orderLineComparator implements Comparator<OrderLineObjMD> {
		public int compare(OrderLineObjMD o1, OrderLineObjMD o2) {
			if (o1.distance < o2.distance)
				return -1;
			else if (o1.distance > o2.distance)
				return 1;
			else
				return 0;
		}
	}

	/**
	 *
	 * @param candidate
	 * @param timeSeriesIns
	 * @return
	 */
	public static double subsequenceDistance(DoubleVectorMD[] candidate, Instance timeSeriesIns, int dim) {
		DoubleVectorMD[] timeSeries = doubleArrayToDoubleVectorArray(timeSeriesIns.toDoubleArray());
		return subsequenceDistance(candidate, timeSeries, dim);
	}

	public static double subsequenceDistance(DoubleVectorMD[] candidate, DoubleVectorMD[] timeSeries, int dim) {
		// TODO: euklideszi távolság
		double bestSum = Double.MAX_VALUE;
		double sum = 0;
		DoubleVectorMD[] subseq;

		// for all possible subsequences of two
		for (int i = 0; i <= timeSeries.length - candidate.length - 1; i++) {

			sum = 0;
			// get subsequence of two that is the same lenght as one
			subseq = new DoubleVectorMD[candidate.length];

			for (int j = i; j < i + candidate.length; j++) {
				subseq[j - i] = timeSeries[j];
			}
			
			
			
			subseq = zNorm(subseq, false); // Z-NORM HERE
			
			for (int j = 0; j < candidate.length; j++) {
				for (int k = 0; k < dim; k++) {
					if (sum < bestSum) { // this line: early abandon done
						sum += (candidate[j].getElement(k) - subseq[j].getElement(k))
								* (candidate[j].getElement(k) - subseq[j].getElement(k));
					}
				}
			}

			if (sum < bestSum) {
				bestSum = sum;
			}
		}
		return (1.0 / candidate.length * bestSum);
	}

	/**
	 *
	 * @param input
	 * @param classValOn
	 * @return
	 */
	public static DoubleVectorMD[] zNorm(DoubleVectorMD[] input, boolean classValOn) { // TODO:
																					// normalizálás
		double mean;
		double stdv;

		double classValPenalty = 0;
		if (classValOn) {
			classValPenalty = 1;
		}
		DoubleVectorMD[] output = new DoubleVectorMD[input.length];
		for (int k = 0; k < input.length; k++) {
			output[k] = new DoubleVectorMD();
		}

		for (int j = 0; j < LearnShapeletsMD.vectorSize; j++) { //TODO
			double seriesTotal = 0;

			for (int i = 0; i < input.length - classValPenalty; i++) {
				seriesTotal += input[i].getElement(j);
			}

			mean = seriesTotal / (input.length - classValPenalty);
			stdv = 0;
			for (int i = 0; i < input.length - classValPenalty; i++) {
				stdv += (input[i].getElement(j) - mean) * (input[i].getElement(j) - mean);
			}

			stdv = stdv / (input.length - classValPenalty);
			stdv = Math.sqrt(stdv);

			for (int i = 0; i < input.length - classValPenalty; i++) {
				output[i].setElement(j, (input[i].getElement(j) - mean) / stdv);
			}
		}

		if (classValOn == true) {
			output[output.length - 1] = input[input.length - 1];
		}

		return output;
	}

	/**
	 *
	 * @param fileName
	 * @return
	 */
	public static Instances loadData(String fileName) {
		Instances data = null;
		try {
			FileReader r;
			r = new FileReader(fileName);
			data = new Instances(r);

			data.setClassIndex(data.numAttributes() - 1);
		} catch (Exception e) {
			System.out.println(" Error =" + e + " in method loadData");
			e.printStackTrace();
		}
		return data;
	}

	private static boolean selfSimilarity(ShapeletMD shapelet, ShapeletMD candidate) {
		if (candidate.seriesId == shapelet.seriesId) {
			if (candidate.startPos >= shapelet.startPos
					&& candidate.startPos < shapelet.startPos + shapelet.content.length) { // candidate
																							// starts
				// within exisiting
				// shapelet
				return true;
			}
			if (shapelet.startPos >= candidate.startPos
					&& shapelet.startPos < candidate.startPos + candidate.content.length) {
				return true;
			}
		}
		return false;
	}

	// /**
	// *
	// * @param args
	// * @throws Exception
	// */
	// public static void main(String[] args) throws Exception{
	// ShapeletFilter sf = new ShapeletFilter(10, 5, 5);
	// Instances data = loadData("example.arff");
	//
	// sf.process(data);
	// }
	/*
	 * public static ShapeletFilter2 createFilterFromFile(String fileName)
	 * throws Exception {
	 * 
	 * File input = new File(fileName); Scanner scan = new Scanner(input);
	 * scan.useDelimiter("\n");
	 * 
	 * ShapeletFilter2 sf = new ShapeletFilter2(); ArrayList<Shapelet2>
	 * shapelets = new ArrayList<Shapelet2>();
	 * 
	 * String shapeletContentString; ArrayList<DoubleVector> content;
	 * DoubleVector[] contentArray; Scanner lineScan;
	 * 
	 * while (scan.hasNext()) { scan.next(); shapeletContentString =
	 * scan.next();
	 * 
	 * lineScan = new Scanner(shapeletContentString);
	 * lineScan.useDelimiter(",");
	 * 
	 * content = new ArrayList<DoubleVector>(); while (lineScan.hasNext()) {
	 * content.add(Double.parseDouble(lineScan.next().trim())); }
	 * 
	 * contentArray = new DoubleVector[content.size()]; for (int i = 0; i <
	 * content.size(); i++) { contentArray[i] = content.get(i); }
	 * 
	 * Shapelet2 s = new Shapelet2(contentArray); shapelets.add(s); }
	 * 
	 * sf.shapelets = shapelets; sf.shapeletsTrained = true; sf.numShapelets =
	 * shapelets.size(); sf.setShapeletMinAndMax(1, 1);
	 * 
	 * return sf; }
	 */
	public static DoubleVectorMD[] doubleArrayToDoubleVectorArray(double[] dA) {
		Integer dCount = dA.length / LearnShapeletsMD.vectorSize;
		DoubleVectorMD[] dV = new DoubleVectorMD[dCount];
		for (int i = 0; i < dV.length; i++) {
			dV[i] = new DoubleVectorMD();
		}

		for (int i = 0; i < dCount; i++) {
			for (int j = 0; j < LearnShapeletsMD.vectorSize; j++) {
				dV[i].setElement(j, dA[i * LearnShapeletsMD.vectorSize + j]);
			}
		}

		return dV;
	}

}
