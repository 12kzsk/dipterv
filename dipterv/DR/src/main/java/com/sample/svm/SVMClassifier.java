package com.sample.svm;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import com.sample.DroolsTest;

import libsvm.svm;
import libsvm.svm_node;
import tech.artisanhub.ShapeletClassifier.Classifier;
import tech.artisanhub.ShapeletTrainerMD.DoubleVectorMD;
import weka.core.Instance;
import weka.core.SparseInstance;

public class SVMClassifier {

	public static int count = 0;
    
	public static boolean isCriticalSVM(DoubleVectorMD vector) throws FileNotFoundException, IOException {
		boolean isCrit = false;
		
		int m = vector.getDoubleVector().length;
		svm_node[] x = new svm_node[m];
		for(int j=0;j<m;j++)
		{
			x[j] = new svm_node();
			x[j].index = j+1;
			x[j].value = vector.getDoubleVector()[j];
		}
		
		double v = svm.svm_predict(DroolsTestSVM.model, x);
		
		if (v == 1.0){
			isCrit = true;
		}
		
		try
		{
		    String filename= "resultSVM.csv";
		    FileWriter fw = new FileWriter(filename,true); //the true will append the new data
		    
		    String isCritString = "0";
		    if (isCrit) isCritString = "1";
		    count++;
		    //THRESHOLD BE VAN ÉGETVE TODO és a 20-szal felszorzás is!!!!
		    fw.write(vector.getDoubleVector()[0] + "," + vector.getDoubleVector()[1] + "," + vector.getDoubleVector()[2] + "," + (Integer.parseInt(isCritString)*20) + "," + count + ",600" +  "\n");//appends the string to the file
		    fw.close();
		}
		catch(IOException ioe)
		{
		    System.err.println("IOException: " + ioe.getMessage());
		}
		
		return isCrit;
	}
}