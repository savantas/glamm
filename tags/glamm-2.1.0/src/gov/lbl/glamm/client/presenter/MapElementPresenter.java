package gov.lbl.glamm.client.presenter;

import gov.lbl.glamm.client.model.AnnotatedMapData;
import gov.lbl.glamm.client.model.Organism;
import gov.lbl.glamm.client.model.Sample;
import gov.lbl.glamm.client.rpc.GlammServiceAsync;

import java.util.Set;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class MapElementPresenter {

	public interface View {
		public void hidePopup();
		public void killPopup();
		public void showPopup(String content, int left, int top);
	}
	
	private GlammServiceAsync rpc = null;
	private View view = null;
	
	private Organism organism = null;
	private Sample sample = null;
	
	public MapElementPresenter(final GlammServiceAsync rpc, final View view) {
		this.rpc = rpc;
		this.view = view;
		this.organism = Organism.globalMap();
		this.sample = null;
	}
	
	public void hidePopup() {
		view.hidePopup();
	}
	
	public void killPopup() {
		view.killPopup();
	}
	
	public void setOrganism(Organism organism) {
		this.organism = organism;
	}
	
	public void setSample(Sample sample) {
		this.sample = sample;
	}
		
	public void showPopup(final String elementClass, final Set<String> ids, final int clientX, final int clientY) {
		AsyncCallback<String> callback = new AsyncCallback<String>() {
			@Override
			public void onFailure(Throwable caught) {
				// Show the RPC error message to the user
				Window.alert("Remote procedure call failure: generating map element popup.");
			}

			@Override
			public void onSuccess(String result) {
				view.showPopup(result, clientX, clientY);
			}
		};
		
		final String loadingMsg = "<html>Loading...</html>";
		
		if(elementClass.equals(AnnotatedMapData.CLASS_CPD)) {
			String taxonomyId = (organism == null ? null : organism.getTaxonomyId());
			view.showPopup(loadingMsg, clientX, clientY);
			rpc.genCpdPopup(ids, taxonomyId, callback);
		}
		else if(elementClass.equals(AnnotatedMapData.CLASS_RXN)) {
			String taxonomyId = (organism == null ? null : organism.getTaxonomyId());
			view.showPopup(loadingMsg, clientX, clientY);
			rpc.genRxnPopup(ids, taxonomyId, callback);
		}
		else if(elementClass.equals(AnnotatedMapData.CLASS_MAP)) {
			String taxonomyId = (organism == null ? null : organism.getTaxonomyId());
			String experimentId = (sample == null ? null : sample.getExperimentId());
			String sampleId = (sample == null ? null : sample.getSampleId());
			view.showPopup(loadingMsg, clientX, clientY);
			rpc.genPwyPopup(ids, taxonomyId, experimentId, sampleId, callback);
		}
		
	}
}
