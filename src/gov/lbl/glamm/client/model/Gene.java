package gov.lbl.glamm.client.model;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;

/**
 * Representation of a Gene
 * @author jtbates
 *
 */
@SuppressWarnings({ "unused", "serial" })
public class Gene extends GlammPrimitive implements Serializable {

	public static transient GlammPrimitive.Type TYPE = new GlammPrimitive.Type();
	public static transient final String SYNONYM_TYPE_NAME		= "0";
	public static transient final String SYNONYM_TYPE_NCBI		= "1";
	public static transient final String SYNONYM_TYPE_SESSION	= "S";
	public static transient final String SYNONYM_TYPE_VIMSS		= "VIMSS";

	
	private String			vimmsId		= null;	
	private HashSet<String> 	ecNums = null;
	private ArrayList<GlammPrimitive.Reference> regulators = null;

	public Gene() {}

	//********************************************************************************

	/**
	 * @return The set of Enzyme Commission (EC) numbers associated with this Gene
	 */
	public HashSet<String> getEcNums() {
		return ecNums;
	}

	//********************************************************************************

	/**
	 * Adds an EC number to the set of EC numbers associated with this Gene
	 * @param ecNum The EC number
	 */
	public void addEcNum(String ecNum) {
		if(ecNums == null)
			ecNums = new HashSet<String>();
		ecNums.add(ecNum);
	}	

	//********************************************************************************

	/**
	 * @return The GlammPrimitive Type
	 */
	@Override
	public Type getType() {
		return TYPE;
	}

}