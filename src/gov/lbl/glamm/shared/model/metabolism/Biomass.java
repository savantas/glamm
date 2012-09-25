package gov.lbl.glamm.shared.model.metabolism;


import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Biomass {
	private String dna = null,
				   rna = null,
				   cofactor = null,
				   isLocked = null,
				   name = null,
				   cellwall = null,
				   protein = null,
				   modDate = null,
				   lipid = null,
				   uuid = null;
	private int energy;
	private List<ModelReactionStoichiometry> biomasscompounds = null;
	
	public Biomass() {
		//biomassCompounds = new ArrayList<ModelReactionStoichiometry>();
	}
	
	
	public String getDna() { return dna; }
	public void setDna(String dna) { this.dna = dna; }
	
	public String getRna() { return rna; }
	public void setRna(String rna) { this.rna = rna; }

	public String getCofactor() { return cofactor; }
	public void setCofactor(String cofactor) { this.cofactor = cofactor; }

	public int getEnergy() { return energy; }
	public void setEnergy(int energy) { this.energy = energy; }

	public String getLocked() { return isLocked; }
	public void setLocked(String isLocked) { this.isLocked = isLocked; }

	public String getName() { return name; }
	public void setName(String name) { this.name = name; }

	@JsonProperty("cellwall")
	public String getCellWall() { return cellwall; }
	@JsonProperty("cellwall")
	public void setCellWall(String cellwall) { this.cellwall = cellwall; }

	public String getProtein() { return protein; }
	public void setProtein(String protein) { this.protein = protein; }

	public String getModDate() { return modDate; }
	public void setModDate(String modDate) { this.modDate = modDate; }

	public String getLipid() { return lipid; }
	public void setLipid(String lipid) { this.lipid = lipid; }

	public String getUUID() { return uuid; }
	public void setUUID(String uuid) { this.uuid = uuid; }
	
	@JsonProperty("biomasscompounds")
	public List<ModelReactionStoichiometry> getBiomassCompounds() { return biomasscompounds; }
	@JsonProperty("biomasscompounds")
	public void setBiomassCompounds(List<ModelReactionStoichiometry> biomasscompounds) { this.biomasscompounds = biomasscompounds; }
}
