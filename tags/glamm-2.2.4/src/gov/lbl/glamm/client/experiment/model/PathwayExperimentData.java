package gov.lbl.glamm.client.experiment.model;


import gov.lbl.glamm.shared.model.Compound;
import gov.lbl.glamm.shared.model.Gene;
import gov.lbl.glamm.shared.model.Organism;
import gov.lbl.glamm.shared.model.Pathway;
import gov.lbl.glamm.shared.model.Experiment;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * @author DHAP Digital, Inc - angie
 *
 */
public class PathwayExperimentData implements Serializable, IsSerializable {
	private static final long serialVersionUID = 1L;

	/**
	 * @gwt.typeArgs <gov.lbl.glamm.experiment.shared.model.Experiment>
	 */
	private List<Pathway> pathways = null;
	/**
	 * @gwt.typeArgs <gov.lbl.glamm.experiment.shared.model.Experiment>
	 */
	private List<Experiment> experiments = null;
	/**
	 * @gwt.typeArgs <java.lang.String,gov.lbl.glamm.experiment.shared.model.Compound>
	 */
	private Map<String,Compound> idCompoundMap = new HashMap<String,Compound>();
	/**
	 * @gwt.typeArgs <java.lang.String,gov.lbl.glamm.experiment.shared.model.Gene>
	 */
	private Map<String,Gene> idGeneMap = new HashMap<String,Gene>();
	/**
	 * @gwt.typeArgs <java.lang.String,gov.lbl.glamm.experiment.shared.model.Organism>
	 */
	private Map<String,Organism> idOrganismMap = new HashMap<String,Organism>();

	public PathwayExperimentData() { }

	/**
	 * @return
	 * @gwt.typeArgs <gov.lbl.glamm.experiment.shared.model.Pathway>
	 */
	public List<Pathway> getPathways() {
		return pathways;
	}
	public void setPathways( List<Pathway> pathways ) {
		this.pathways = pathways;
	}
	/**
	 * @return
	 * @gwt.typeArgs <gov.lbl.glamm.experiment.shared.model.Experiment>
	 */
	public List<Experiment> getExperiments() {
		return experiments;
	}
	public void setExperiments( List<Experiment> experiments ) {
		this.experiments = experiments;
	}

	/**
	 * @return
	 * @gwt.typeArgs <java.lang.String,gov.lbl.glamm.experiment.shared.model.Compound>
	 */
	public Map<String, Compound> getIdCompoundMap() {
		return idCompoundMap;
	}
	public void setIdCompoundMap( Map<String, Compound> idCompoundMap ) {
		this.idCompoundMap = idCompoundMap;
	}
	/**
	 * @return
	 * @gwt.typeArgs <java.lang.String,gov.lbl.glamm.experiment.shared.model.Gene>
	 */
	public Map<String, Gene> getIdGeneMap() {
		return idGeneMap;
	}
	public void setIdGeneMap( Map<String, Gene> idGeneMap ) {
		this.idGeneMap = idGeneMap;
	}
	/**
	 * @return
	 * @gwt.typeArgs <java.lang.String,gov.lbl.glamm.experiment.shared.model.Organism>
	 */
	public Map<String, Organism> getIdOrganismMap() {
		return idOrganismMap;
	}
	public void setIdOrganismMap( Map<String, Organism> idOrganismMap ) {
		this.idOrganismMap = idOrganismMap;
	}
}
