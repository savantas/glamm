package gov.lbl.glamm.server.kbase.dao.impl;

import gov.doe.kbase.workspaceservice.list_workspace_objects_params;
import gov.doe.kbase.workspaceservice.list_workspaces_params;
import gov.doe.kbase.workspaceservice.object_metadata;
import gov.doe.kbase.workspaceservice.workspaceService;
import gov.doe.kbase.workspaceservice.workspace_metadata;
import gov.lbl.glamm.server.ConfigurationManager;
import gov.lbl.glamm.server.GlammSession;
import gov.lbl.glamm.server.kbase.dao.KBWorkspaceDAO;
import gov.lbl.glamm.shared.model.User;
import gov.lbl.glamm.shared.model.kbase.workspace.KBWorkspaceData;
import gov.lbl.glamm.shared.model.kbase.workspace.KBWorkspaceObjectData;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class KBWorkspaceDAOImpl implements KBWorkspaceDAO {

	private enum ObjectType {
		MODEL("Model"),
		FBA("FBA"),
		PHENOTYPE_SET("PhenotypeSet"),
		PROB_ANNO("ProbAnno"),
		GAP_FILL("GapFill"),
		MEDIA("Media"),
		MAPPING("Mapping"),
		PHENOTYPE_SIMULATION_SET("PhenotypeSimulationSet"),
		GENOME("Genome"),
		GENOME_CONTIGS("GenomeContigs"),
		FBA_JOB("FBAJob"),
		PROM_MODEL("PROMModel"),
		TEST_DATA("TestData"),
		BIOCHEMISTRY("Biochemistry"),
		GAP_GEN("GapGen"),
		ANNOTATION("Annotation"),
		UNSPECIFIED("Unspecified"),
		PROM_CONSTRAINTS("PromConstraints"),
		GROWMATCH_DATA("Growmatch data");
		
		private String name;
		private ObjectType(String name) {
			this.name = name;
		}
		
		public String getName() {
			return name;
		}

	}
	
	private static final String WORKSPACE_URL = ConfigurationManager.getKBaseServiceURL("workspace"); 
	//"http://bio-data-1.mcs.anl.gov/services/fba_gapfill";
	private GlammSession sm;
	private static workspaceService wsClient;
	
	static {
		try {
			wsClient = new workspaceService(WORKSPACE_URL);
		} catch (MalformedURLException e) {
			wsClient = null;
		}
	}
	
	public KBWorkspaceDAOImpl(GlammSession sm) {
		this.sm = sm;
	}

	@Override
	public List<KBWorkspaceData> getWorkspaceList() {
		List<KBWorkspaceData> workspaceList = new ArrayList<KBWorkspaceData>();
		
		list_workspaces_params params = new list_workspaces_params();
		if (sm.getUser() != User.guestUser())
			params.auth = sm.getUser().getAuth();
		try {
			List<workspace_metadata> workspaceDataList = wsClient.list_workspaces(params);
			
			for (workspace_metadata metadata : workspaceDataList) {
				KBWorkspaceData data = new KBWorkspaceData();
				data.setId(metadata.id);
				data.setModDate(metadata.moddate);
				data.setNumObjects(metadata.objects);
				data.setOwner(metadata.owner);
				data.setGlobalPermission(metadata.global_permission);
				data.setUserPermission(metadata.user_permission);
				workspaceList.add(data);
			}
			
			Collections.sort(workspaceList, new Comparator<KBWorkspaceData>() {
				@Override
				public int compare(KBWorkspaceData ws0, KBWorkspaceData ws1) {
					return ws0.getId().compareToIgnoreCase(ws1.getId());
				}
			});
		} catch (Exception e) {
			workspaceList.clear();
			return workspaceList;
		}
		
		return workspaceList;
	}

	@Override
	public List<KBWorkspaceObjectData> getWorkspaceModelList(final String workspaceName) {
		return getWorkspaceObjectData(workspaceName, ObjectType.MODEL);
	}
	
	@Override
	public List<KBWorkspaceObjectData> getWorkspaceFbaList(final String workspaceName) {
		return getWorkspaceObjectData(workspaceName, ObjectType.FBA);
	}
	
	private List<KBWorkspaceObjectData> getWorkspaceObjectData(final String workspaceName, final ObjectType type) {
		List<KBWorkspaceObjectData> dataList = new ArrayList<KBWorkspaceObjectData>();
		
		list_workspace_objects_params params = new list_workspace_objects_params();
		params.showDeletedObject = 0;
		params.workspace = workspaceName;
		params.type = type.getName();
		
		if (sm.getUser() != User.guestUser())
			params.auth = sm.getUser().getAuth();
		
		try {
			List<object_metadata> objDataList = wsClient.list_workspace_objects(params);
			for (object_metadata metadata : objDataList) {
				KBWorkspaceObjectData data = new KBWorkspaceObjectData();
				data.setId(metadata.id);
				data.setOwner(metadata.owner);
				data.setCommand(metadata.command);
				data.setChecksum(metadata.chsum);
				data.setModDate(metadata.moddate);
				data.setWorkspace(metadata.workspace);
				data.setMetadataMap(metadata.metadata);
				data.setRef(metadata.ref);
				data.setLastModifier(metadata.lastmodifier);
				data.setType(metadata.type);
				data.setInstance(metadata.instance);
				
				dataList.add(data);
			}
			return dataList;
		} catch (Exception e) {
			dataList.clear();
		}
		return dataList;
	}
	
}
