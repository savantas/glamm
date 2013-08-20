package gov.lbl.glamm.client.experiment.events;

import gov.lbl.glamm.client.experiment.model.ViewPathway;
import gov.lbl.glamm.client.experiment.model.ViewCompound;
import gov.lbl.glamm.client.experiment.util.BinarySortedSet;
import gov.lbl.glamm.shared.model.Experiment;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

/**
 * Event indicating that new data has been parsed and is ready for visual rendering.
 * @author DHAP Digital, Inc - angie
 */
public class DataChangedEvent extends GwtEvent<DataChangedEvent.Handler> {
	public interface Handler extends EventHandler {
		public void handleDataReceived(DataChangedEvent dcEvent);
	}

	public static final Type<Handler> ASSOCIATED_TYPE = new Type<Handler>();

	private List<ViewPathway> pathways = null;
	private String[] pathwayGuideColors = null;
	private List<Experiment> experiments = null;
	private BinarySortedSet<ViewCompound> secondaryMetaboliteList = null;
	private ArrayList<String> dynamicDefBaseNameList = null;

	public DataChangedEvent( List<ViewPathway> pathways
			, String[] pathwayGuideColors
			, List<Experiment> experiments
			, BinarySortedSet<ViewCompound> secondaryMetaboliteList
			, ArrayList<String> dynamicDefBaseNameList
	) {
		this.pathways = pathways;
		this.pathwayGuideColors = pathwayGuideColors;
		this.experiments = experiments;
		this.secondaryMetaboliteList = secondaryMetaboliteList;
		this.dynamicDefBaseNameList = dynamicDefBaseNameList;
	}

	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<Handler> getAssociatedType() {
		return ASSOCIATED_TYPE;
	}

	@Override
	protected void dispatch(Handler handler) {
		handler.handleDataReceived(this);
	}

	public List<ViewPathway> getPathways() {
		return this.pathways;
	}

	public String[] getPathwayGuideColors() {
		return pathwayGuideColors;
	}

	public List<Experiment> getExperiments() {
		return experiments;
	}

	public BinarySortedSet<ViewCompound> getSecondaryMetaboliteList() {
		return secondaryMetaboliteList;
	}

	public ArrayList<String> getDynamicDefBaseNameList() {
		return dynamicDefBaseNameList;
	}
}
