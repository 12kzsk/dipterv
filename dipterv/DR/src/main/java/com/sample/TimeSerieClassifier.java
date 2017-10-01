package com.sample;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import tech.artisanhub.ShapeletClassifier.Classifier;
import tech.artisanhub.ShapeletTrainerMD.DoubleVectorMD;
import weka.core.Instance;
import weka.core.SparseInstance;

public class TimeSerieClassifier {

	public static DoubleVectorMD[] vectors;
	public static int vecSize = 0;

	public static boolean isCritical(DoubleVectorMD vector) throws FileNotFoundException, IOException {
		boolean isCrit = false;
		
		int tsLength = DroolsTest.shapelet.length;
		
		// vector t�mb rendez�se
		if (vectors == null)
			vectors = new DoubleVectorMD[tsLength];
		if (vecSize < tsLength) {
			vectors[vecSize] = vector;
			vecSize++;
		} else {
			for (int i = 0; i < tsLength-1; i++) {
				vectors[i] = vectors[i + 1];
			}
			vectors[tsLength-1] = vector;

			// kritikus-e?
			double[] dArr = new double[vectors.length * DroolsTest.dimension];
			for (int x = 0; x < vectors.length; x++) {
				for (int y = 0; y < DroolsTest.dimension; y++) {
					dArr[x * DroolsTest.dimension + y] = vectors[x].getElement(y);
				}
			}

			Instance ts = new SparseInstance(1.0, dArr);

			Classifier cl = new Classifier();

			String cls = cl.classifyLine(ts, DroolsTest.shapelet, DroolsTest.cls, DroolsTest.th, DroolsTest.dimension);

			if (cls.equals("NOT 0.0") || cls.equals("1.0"))
				isCrit = true;
			else
				isCrit = false;

		}

		return isCrit;
	}
}
