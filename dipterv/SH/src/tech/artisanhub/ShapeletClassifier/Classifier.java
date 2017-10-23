package tech.artisanhub.ShapeletClassifier;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;

import tech.artisanhub.ShapeletTrainerMD.DoubleVectorMD;
import tech.artisanhub.ShapeletTrainerMD.ShapeletFilterMD;
import tech.artisanhub.ShapeletTrainerMD.TimeSeriesMD;
import weka.core.Instance;

public class Classifier {
	//inputok egy r�sz�t a classOutputiz�f�jlb�l kapja meg (ebben egyetlen sor van)
	//fv2: input-sor/Instance/,shapelet/DoubleVector[]/(csak a sh, oszt�ly nem),k�sz�b,a k�sz�b alatti oszt�lyban van-e(boolean);
	//output-oszt�ly, amibe besorolom a sort
	//megh�vja a ShapeletFilter2.subsequenceDistance(candidate, timeSeries) fv.t --> input: (shapelet,sor), output-->double
	//ennek az eredm�ny�t �sszehasonl�tja a shapelethez tartoz� splitThresholddal(k�sz�b)
	//params: 1 id�sor, shapelet, shapelethez tartoz� oszt�ly, shapelethez tartoz� threshold, id�sorok elemeinek dimenzi�ja
	public String classifyLine(DoubleVectorMD[] ts, DoubleVectorMD[] sh, String shClass, double th, int dim) throws FileNotFoundException, IOException{
 
		double dist = ShapeletFilterMD.subsequenceDistance(sh, ts, dim);
		
        if (dist <= th) return (shClass); //shClass = class of the shapelet
		else return ("NOT " + shClass);
	}
	
	//fv1: input-mx vagy cs�sz�ablak tartalma m�trixban....
	//soronk�nt megh�vja fv2-t
	//output: oszt�lyokb�l �ll� vektor
/*	public String[] classifyMatrix(ArrayList<DoubleVector[]> timeSeries, String shapeletFileName) throws FileNotFoundException, IOException{
		String[] classes = new String[timeSeries.size()];
		for (int i = 0; i < timeSeries.size(); i++){
			classes[i] = classifyLine(timeSeries.get(i), shapeletFileName );
		}
		return classes;
	}
	*/
}
