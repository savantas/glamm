package gov.lbl.glamm.client.experiment.rpc;

import gov.lbl.glamm.client.experiment.model.PathwayExperimentData;

import com.google.gwt.http.client.RequestException;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client side stub for the RPC service.
 */
//@RemoteServiceRelativePath("experiment")
@RemoteServiceRelativePath("../glamm/rpc")
public interface PathwayExperimentService extends RemoteService {
	public static String PATHWAY_SELECTION_COOKIE = "pathwaySelection";
	public static String EXPERIMENT_SELECTION_COOKIE = "experimentSelection";

	PathwayExperimentData getPathwayData( String pathwayIds, String experimentIds )
			throws IllegalArgumentException, RequestException;
}
