package gov.lbl.glamm.shared.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@SuppressWarnings("serial")
public class GlammState implements Serializable {

	private Organism organism = null;
	private String amdId = null;

	private String viewport = null;
	
	private MetabolicModel model = null;
	private Experiment exp = null;
	private Set<OverlayDataGroup> groupData = null;
	
	private boolean uiState = true;
	
	private GlammState() {
		setOrganism(Organism.globalMap());
		amdId = "map01100"; 
		viewport = "";
		uiState = true;
	}
	
	public void setOrganism(Organism organism) {
		if (organism == null)
			this.organism = Organism.globalMap();
		
		this.organism = organism;
	}
	
	public Organism getOrganism() {
		return organism;
	}
	
	public static GlammState defaultState() {
		return new GlammState();
	}
	
	public void setAMDId(String id) {
		if (id != null)
			amdId = id;
	}
	
	public String getAMDId() {
		return amdId;
	}

	public void setViewport(String viewport) {
		// parse the string to get viewport information. should be in the format:
		// tlx,tly,w,h
		// that's - top left x-coord, top left y-coord, width, height
		// all in pixels relative to the given map view (e.g. the SVG being shown).
		
		// maybe it should be centered with some zoom level? what about browser sizes, etc?
		
		this.viewport = viewport;
	}
	
	public String getViewport() {
		return viewport;
	}

	public void setModel(MetabolicModel model) {
		this.model = model;
	}
	
	public MetabolicModel getModel() {
		return model;
	}

	public void setExperiment(Experiment exp) {
		this.exp = exp;
	}
	
	public Experiment getExperiment() {
		return exp;
	}

	public void setGroupData(Set<OverlayDataGroup> groupData) {
		if (groupData == null)
			this.groupData = new HashSet<OverlayDataGroup>();
		else
			this.groupData = groupData;
	}
	
	public Set<OverlayDataGroup> getGroupData() {
		return groupData;
	}

	public void setUIState(boolean uiState) {
		this.uiState = uiState;
	}
	
	public boolean getUIState() {
		return uiState;
	}
}
