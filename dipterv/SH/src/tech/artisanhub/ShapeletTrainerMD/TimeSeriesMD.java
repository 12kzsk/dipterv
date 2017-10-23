package tech.artisanhub.ShapeletTrainerMD;

public class TimeSeriesMD {
	public TimeSeriesMD() {
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
		doubleVectorMDArray = new DoubleVectorMD[LearnShapeletsMD.vectorSize]; //TODO
	}
	
	public TimeSeriesMD(Integer d) {
		doubleVectorMDArray = new DoubleVectorMD[d];
	}

	private DoubleVectorMD[] doubleVectorMDArray = null;
	private Double classValue;
	
	public Double getClassValue() {
		return classValue;
	}
	
	public void setClassValue(Double Class)  {
		this.classValue = Class;
	}

	public DoubleVectorMD[] getDoubleVector() {
		return doubleVectorMDArray;
	}

	public void setDoubleVector(DoubleVectorMD[] doubleVetorMDs) {
		this.doubleVectorMDArray = doubleVetorMDs;
	}

	public DoubleVectorMD getElement(int i) {
		return this.doubleVectorMDArray[i];
	}

	public void setElement(int i, DoubleVectorMD element) {
		this.doubleVectorMDArray[i] = element;
	}
	
	public int length() {
		return doubleVectorMDArray.length;
	}
}



