package gov.lbl.glamm.client.experiment.util;

public class LengthsAndRatios {
	private float[] lengths = null;
	private float totalLength = 0;
	private float[] ratios = null;

	/**
	 * @param lengths
	 * @param totalLength
	 * @param ratios
	 */
	public LengthsAndRatios(float[] lengths, float totalLength, float[] ratios) {
		super();
		this.lengths = lengths;
		this.totalLength = totalLength;
		this.ratios = ratios;
	}

	public float[] getLengths() {
		return lengths;
	}

	public float getTotalLength() {
		return totalLength;
	}

	public float[] getRatios() {
		return ratios;
	}
}
