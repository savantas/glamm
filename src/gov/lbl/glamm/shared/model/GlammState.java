package gov.lbl.glamm.shared.model;
/**
 * A class for describing the state that GLAMM is in.
 * 
 * As of 2/19/2013, this is only used to deal with URL inputs (e.g. http://glamm/#map=xyz)
 * Eventually, it will be used to save/export a user's state.
 * 
 * Or maybe I'll just rewrite the whole thing in Javascript. Won't that be fun.
 * @author wjriehl
 */


import gov.lbl.glamm.shared.model.kbase.fba.model.KBMetabolicModel;
import gov.lbl.glamm.shared.model.kbase.workspace.KBWorkspaceObjectData;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@SuppressWarnings("serial")
public class GlammState implements Serializable {

	private Organism organism 				= null;
	private String amdId 					= null;
	private Experiment exp 					= null;
	private Set<OverlayDataGroup> groupData = null;
	private KBMetabolicModel model 			= null;
	private KBWorkspaceObjectData modelData = null;
	private String workspace 				= null;
	private String viewport 				= null;
	private boolean uiState 				= true;
	
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

	public void setWorkspace(String workspace) {
		this.workspace = workspace;
	}
	
	public String getWorkspace() {
		return workspace;
	}
	
	public void setModel(KBMetabolicModel model) {
		this.model = model;
	}
	
	public KBMetabolicModel getModel() {
		return model;
	}
	
	public void setModelData(KBWorkspaceObjectData modelData) {
		this.modelData = modelData;
	}
	
	public KBWorkspaceObjectData getModelData() {
		return modelData;
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
