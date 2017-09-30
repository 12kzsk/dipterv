package tech.artisanhub.ShapeletTrainerMD;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Properties;

public class DoubleVectorMD {
	public DoubleVectorMD() {
/*		// Propertyk beolvasása
		Properties prop = new Properties();
		InputStream input = null;
		try {
			input = new FileInputStream("config.properties");
			prop.load(input);
		} catch (IOException e) {
			e.printStackTrace();
		}
*/
		//doubleVector = new Double[Integer.parseInt(prop.get("dimension").toString())];
		doubleVector = new Double[LearnShapeletsMD.vectorSize]; //TODO
	}

	private Double[] doubleVector = null;

	public Double[] getDoubleVector() {
		return doubleVector;
	}

	public void setDoubleVector(Double[] doubleVector) {
		this.doubleVector = doubleVector;
	}

	public Double getElement(int i) {
		return this.doubleVector[i];
	}

	public void setElement(int i, Double element) {
		this.doubleVector[i] = element;
	}
}
