package gov.lbl.glamm.shared.model.kbase.workspace;

import java.io.Serializable;

@SuppressWarnings("serial")
public class KBWorkspaceData implements Serializable {

	private String id;
	private String owner;
	private String moddate;
	private Integer numObjects;
	private String userPermission;
	private String globalPermission;

	public KBWorkspaceData() {
		id = "";
		owner = "";
		moddate = "";
		numObjects = 0;
		userPermission = "";
		globalPermission = "";
	}
	
    public String getId() {
    	return id;
    }
    
    public void setId(String id) {
    	this.id = id;
    }
    
    public String getOwner() {
    	return owner;
    }
    
    public void setOwner(String owner) {
    	this.owner = owner;
    }
    
    public String getModDate() {
    	return moddate;
    }
    
    public void setModDate(String moddate) {
    	this.moddate = moddate;
    }
    
    public int getNumObjects() {
    	return numObjects;
    }
    
    public void setNumObjects(int numObjects) {
    	this.numObjects = numObjects;
    }
    
    public String getUserPermission() {
    	return userPermission;
    }
    
    public void setUserPermission(String userPermission) {
    	this.userPermission = userPermission;
    }
    
    public String getGlobalPermission() {
    	return globalPermission;
    }
    
    public void setGlobalPermission(String globalPermission) {
    	this.globalPermission = globalPermission;
    }
    
}
