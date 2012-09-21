package gov.lbl.glamm.client.experiment.model;

/**
 * View class that references a pathway element and associates it with
 * a vertical displacement.
 *
 * @author DHAP Digital, Inc - angie
 *
 */
public abstract class ViewSymbol<T> {
	protected T baseObject = null;
	private float relY = 0;

	// Instantiation methods
	public ViewSymbol(T baseObject) {
		this.baseObject = baseObject;
	}

	// Display methods
	public abstract String toHtml();

	// Bean methods
	public T getBaseObject() {
		return baseObject;
	}
	public float getRelY() {
		return relY;
	}
	public void setRelY(float relY) {
		this.relY = relY;
	}
}
