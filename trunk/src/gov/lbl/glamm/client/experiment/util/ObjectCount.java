package gov.lbl.glamm.client.experiment.util;

public class ObjectCount {
	public long primaryObjCount = 0;
	public long listCount = 0;
	public long viewObjCount = 0;
	public long svgObjCount = 0;
	public long svgUseCount = 0;
	public long expListCount = 0;
	public long expObjCount = 0;

	public void reset() {
		this.primaryObjCount = 0;
		this.listCount = 0;
		this.viewObjCount = 0;
		this.svgObjCount = 0;
		this.svgUseCount = 0;
		this.expListCount = 0;
		this.expObjCount = 0;
	}

	public void add( ObjectCount otherCount ) {
		this.primaryObjCount += otherCount.primaryObjCount;
		this.listCount += otherCount.listCount;
		this.viewObjCount += otherCount.viewObjCount;
		this.svgObjCount += otherCount.svgObjCount;
		this.svgUseCount += otherCount.svgUseCount;
		this.expListCount += otherCount.expListCount;
		this.expObjCount += otherCount.expObjCount;
	}

	public long getModelObjectsTotal() {
		return this.primaryObjCount + this.listCount + this.viewObjCount
				+ this.expListCount + this.expObjCount;
	}

	public long getSVGElementsTotal() {
		return this.svgObjCount + this.svgUseCount;
	}

	public String toString() {
		return "primary: " + primaryObjCount + "; collections: " + listCount
			+ "; view objects: " + viewObjCount
			+ "; svg elements: " + svgObjCount + "; svg use elements: " + svgUseCount
			+ "; experiment collections: " + expListCount
			+ "; experiment objects with data: " + expObjCount
			;
	}
}
