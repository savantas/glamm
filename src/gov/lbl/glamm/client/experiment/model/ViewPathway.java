package gov.lbl.glamm.client.experiment.model;

import java.util.ArrayList;

/**
 * A pathway. This pathway class is a composite view and model object,
 * with an array of ViewReaction objects and references to length.
 *
 * @author DHAP Digital, Inc - angie
 *
 */
public class ViewPathway {
	// View placement: note that measurements are generally taken from centers of drawn objects
	private float totalLength = 0;

	// Information
	private String id = null;
	private String name = null;
	private int order = 0;

	// Children
	private ArrayList<ViewReaction> viewReactions = null;

	// Description methods
	public String toHtml() {
		StringBuilder builder = new StringBuilder();
		builder.append("<span class='subtitle'>").append(id)
			.append("</span>: <span class='subtitle'>")
			.append(name)
			.append("</span>")
			;
		return builder.toString();
	}

	// Bean methods
	public float getTotalLength() {
		return totalLength;
	}
	public void setTotalLength(float totalLength) {
		this.totalLength = totalLength;
	}

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getOrder() {
		return order;
	}
	public void setOrder(int order) {
		this.order = order;
	}
	public void setOrder(String orderStr) {
		this.order = 0;
		if ( orderStr != null && !orderStr.trim().equals("") ) {
			this.order = Integer.parseInt(orderStr);
		}
	}
	public ArrayList<ViewReaction> getViewReactions() {
		if ( viewReactions == null ) {
			viewReactions = new ArrayList<ViewReaction>();
		}
		return viewReactions;
	}
	public void setViewReactions(ArrayList<ViewReaction> viewReactions) {
		this.viewReactions = viewReactions;
	}

}
