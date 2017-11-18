package com.sample;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import tech.artisanhub.ShapeletClassifier.Classifier;
import tech.artisanhub.ShapeletTrainerMD.DoubleVectorMD;
import weka.core.Instance;
import weka.core.SparseInstance;

public class TimeSerieClassifier {

	public static DoubleVectorMD[] vectors; //az utols� shapelet hossz�s�g� id�sor
	public static int vecSize = 0;
	public static int lastFoundShapelet = -1;
	//h�ny adattal ezel�tt tal�ltunk shapeletet (az utols� karakterre vonatkozik)
	
	public static boolean isCriticalHeart(DoubleVectorMD vector) throws FileNotFoundException, IOException {
		boolean isCrit = false;
		
		int shLength = DroolsTest.shapelet.length;
		
		// vector t�mb rendez�se
		if (vectors == null)
			vectors = new DoubleVectorMD[shLength];
		if (vecSize < shLength) {
			vectors[vecSize] = vector;
			vecSize++;
		} else {
			for (int i = 0; i < shLength-1; i++) {
				vectors[i] = vectors[i + 1];
			}
			vectors[shLength-1] = vector;

			// kritikus-e?
			Classifier cl = new Classifier();

			//DroolsTest.cls = a shapelet milyen oszt�ly� sorb�l lett kiszedve
			String cls = cl.classifyLine(vectors, DroolsTest.shapelet, DroolsTest.cls, DroolsTest.th, DroolsTest.dimension);

			if (DroolsTest.cls.equals("1.0")){
				if(cls.equals("1.0"))
					isCrit = true;
				else
					isCrit = false;
			}
			else if(DroolsTest.cls.equals("0.0")) {
				if(cls.equals("0.0"))
					lastFoundShapelet = 0;
				else if(lastFoundShapelet!=-1)
					lastFoundShapelet++;

				if (lastFoundShapelet > (DroolsTest.drTsLength - DroolsTest.shapelet.length)){
					isCrit = true;
				}
			}
			else System.out.println("Egyik oszt�ly� sem volt, valami baj van.");
		}
		
		try
		{
		    String filename= "result.csv";
		    FileWriter fw = new FileWriter(filename,true); //the true will append the new data
		    
			String isShapelet = "0";
		    if (lastFoundShapelet < DroolsTest.shapelet.length) isShapelet = "1";
		    
		    String isCritString = "0";
		    if (isCrit) isCritString = "1";
		    
		    //TODO: 2dim be van dr�tozva!!!!
		    //inputvector els� eleme, inputvector 2. eleme, van-e shapelet, hol van a v�ge a shapeletnek
		    if (isShapelet.equals("1") && lastFoundShapelet!=-1)
		    	fw.write(vectors[0].getElement(0) + "," + vectors[0].getElement(1) + "," + DroolsTest.shapelet[lastFoundShapelet].getElement(0) + "," + DroolsTest.shapelet[lastFoundShapelet].getElement(1) + "," + isCritString + "\n");//appends the string to the file
		    else //ha nincs shapelet �ppen
		    	fw.write(vectors[0].getElement(0) + "," + vectors[0].getElement(1) + ",0,0," + isCritString + "\n");//appends the string to the file
		    fw.close();
		}
		catch(IOException ioe)
		{
		    System.err.println("IOException: " + ioe.getMessage());
		}
		//System.out.println(isCrit);
		return isCrit;
	}
/*	
	public static boolean isCriticalFlight(DoubleVectorMD vector) throws FileNotFoundException, IOException {
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
	}*/
}
