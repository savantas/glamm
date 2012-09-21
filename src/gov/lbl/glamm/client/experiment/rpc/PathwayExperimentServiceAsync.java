package gov.lbl.glamm.client.experiment.rpc;

import gov.lbl.glamm.client.experiment.model.PathwayExperimentData;

import com.google.gwt.http.client.RequestException;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async counterpart of <code>PathwayExperimentService</code>.
 */
public interface PathwayExperimentServiceAsync {
	void getPathwayData( String pathwayIds, String experimentIds
			, AsyncCallback<PathwayExperimentData> callback )
			throws IllegalArgumentException, RequestException;
}
