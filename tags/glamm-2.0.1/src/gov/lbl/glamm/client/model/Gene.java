package gov.lbl.glamm.client.model;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;


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

	public HashSet<String> getEcNums() {
		return ecNums;
	}

	//********************************************************************************

	public void addEcNum(String ecNum) {
		if(ecNums == null)
			ecNums = new HashSet<String>();
		ecNums.add(ecNum);
	}	

	//********************************************************************************

	@Override
	public Type getType() {
		return TYPE;
	}

}