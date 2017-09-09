package tech.artisanhub.ShapeletTrainerMD;

public class DoubleVectorMD {
	private Double[] doubleVector = new Double[LearnShapeletsMD.vectorSize];
	
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
