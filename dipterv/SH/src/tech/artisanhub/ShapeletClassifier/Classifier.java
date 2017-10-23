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
	//inputok egy részét a classOutputizéfájlból kapja meg (ebben egyetlen sor van)
	//fv2: input-sor/Instance/,shapelet/DoubleVector[]/(csak a sh, osztály nem),küszöb,a küszöb alatti osztályban van-e(boolean);
	//output-osztály, amibe besorolom a sort
	//meghívja a ShapeletFilter2.subsequenceDistance(candidate, timeSeries) fv.t --> input: (shapelet,sor), output-->double
	//ennek az eredményét összehasonlítja a shapelethez tartozó splitThresholddal(küszöb)
	//params: 1 idõsor, shapelet, shapelethez tartozó osztály, shapelethez tartozó threshold, idõsorok elemeinek dimenziója
	public String classifyLine(DoubleVectorMD[] ts, DoubleVectorMD[] sh, String shClass, double th, int dim) throws FileNotFoundException, IOException{
 
		double dist = ShapeletFilterMD.subsequenceDistance(sh, ts, dim);
		
        if (dist <= th) return (shClass); //shClass = class of the shapelet
		else return ("NOT " + shClass);
	}
	
	//fv1: input-mx vagy csúszóablak tartalma mátrixban....
	//soronként meghívja fv2-t
	//output: osztályokból álló vektor
/*	public String[] classifyMatrix(ArrayList<DoubleVector[]> timeSeries, String shapeletFileName) throws FileNotFoundException, IOException{
		String[] classes = new String[timeSeries.size()];
		for (int i = 0; i < timeSeries.size(); i++){
			classes[i] = classifyLine(timeSeries.get(i), shapeletFileName );
		}
		return classes;
	}
	*/
}
