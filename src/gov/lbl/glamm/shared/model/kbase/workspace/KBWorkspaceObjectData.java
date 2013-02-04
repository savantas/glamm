package gov.lbl.glamm.shared.model.kbase.workspace;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.google.gwt.view.client.ProvidesKey;

@SuppressWarnings("serial")
public class KBWorkspaceObjectData implements Serializable {
	private String id;
	private String owner;
	private String command;
	private String checksum;
	private String modDate;
	private String workspace;
	private Map<String, String> metadataMap;
	private String ref;
	private String lastModifier;
	private String type;
	private int instance;
	
	public KBWorkspaceObjectData() {
		id = "";
		owner = "";
		command = "";
		checksum = "";
		modDate = "";
		workspace = "";
		ref = "";
		lastModifier = "";
		type = "";
		instance = 0;
		metadataMap = new HashMap<String, String>();
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


	public String getCommand() {
		return command;
	}


	public void setCommand(String command) {
		this.command = command;
	}


	public String getChecksum() {
		return checksum;
	}


	public void setChecksum(String checksum) {
		this.checksum = checksum;
	}


	public String getModDate() {
		return modDate;
	}


	public void setModDate(String modDate) {
		this.modDate = modDate;
	}


	public String getWorkspace() {
		return workspace;
	}


	public void setWorkspace(String workspace) {
		this.workspace = workspace;
	}


	public Map<String, String> getMetadataMap() {
		return metadataMap;
	}


	public void setMetadataMap(Map<String, String> metadataMap) {
		this.metadataMap.putAll(metadataMap);
	}


	public String getRef() {
		return ref;
	}


	public void setRef(String ref) {
		this.ref = ref;
	}


	public String getLastModifier() {
		return lastModifier;
	}


	public void setLastModifier(String lastModifier) {
		this.lastModifier = lastModifier;
	}


	public String getType() {
		return type;
	}


	public void setType(String type) {
		this.type = type;
	}


	public int getInstance() {
		return instance;
	}


	public void setInstance(int instance) {
		this.instance = instance;
	}


	/**
	 * Key provider. 
	 */
	public static final transient ProvidesKey<KBWorkspaceObjectData> KEY_PROVIDER = new ProvidesKey<KBWorkspaceObjectData>() {
		public Object getKey(KBWorkspaceObjectData item) {
			return item == null ? null : item.getId() + "_" + item.getWorkspace();
		}
	};

	
}
