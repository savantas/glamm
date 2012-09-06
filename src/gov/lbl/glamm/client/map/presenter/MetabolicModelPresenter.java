package gov.lbl.glamm.client.map.presenter;

import java.util.HashMap;
import java.util.Map;

import gov.lbl.glamm.client.map.events.LoadingEvent;
import gov.lbl.glamm.client.map.events.MetabolicModelLoadedEvent;
import gov.lbl.glamm.client.map.rpc.GlammServiceAsync;
import gov.lbl.glamm.shared.model.MetabolicModel;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.ListBox;

/**
 * A presenter for dealing with metabolic models. This allows the user to either select a model from a list, or upload one.
 * @author wjriehl
 *
 */
public class MetabolicModelPresenter {

	public interface View {
		public ListBox getListBox();
	}

	private GlammServiceAsync rpc;
	private View view;
	private SimpleEventBus eventBus;
	private Map<String, String> title2ModelId;
	private Map<String, String> modelId2Title;
	
	public MetabolicModelPresenter(final GlammServiceAsync rpc, final View view, final SimpleEventBus eventBus) {
		this.rpc = rpc;
		this.eventBus = eventBus;
		this.view = view;
		
		title2ModelId = new HashMap<String, String>();
		modelId2Title = new HashMap<String, String>();
		bindView();
	}
	
	private void bindView() {
		view.getListBox().addChangeHandler(new ChangeHandler() {
			@Override
			public void onChange(ChangeEvent event) {
				int index = view.getListBox().getSelectedIndex();
				String title = view.getListBox().getItemText(index);
				loadModelFromId(title2ModelId.get(title));
			}
		});
	}
	
	/**
	 * Selects a model from the set of metabolic models.
	 * @param modelId The model id.
	 */
	public void selectModel(final String modelId) {
		if (modelId == null)
			return;
		if (modelId.equals("0"))
			eventBus.fireEvent(new MetabolicModelLoadedEvent(null));

		ListBox listBox = view.getListBox();
		for(int i = 0; i < listBox.getItemCount(); i++) {
			if(modelId2Title.get(modelId).equals(listBox.getItemText(i))) {
				listBox.setSelectedIndex(i);
				loadModelFromId(modelId);
				break;
			}
		}
	}

	/**
	 * Loads a model with the given id. If it is present and returned through the rpc call, then it is 
	 * used to fire a MetabolicModelLoadedEvent. Otherwise, an error is shown.
	 * @param id the ID for the MetabolicModel to load
	 */
	public void loadModelFromId(String id) {
		eventBus.fireEvent(new LoadingEvent(false));
		
		rpc.getMetabolicModel(id, new AsyncCallback<MetabolicModel>() {
			public void onFailure(Throwable caught) {
				Window.alert("Remote procedure call failure: getMetabolicModel");
			}
			
			public void onSuccess(MetabolicModel model) {
				eventBus.fireEvent(new MetabolicModelLoadedEvent(model));
			}
		});
		
		eventBus.fireEvent(new LoadingEvent(true));
	}
	
	/**
	 * Populates the ListBox for Metabolic Model selection.
	 * @param initialTitle
	 */
	public void populate(String initialTitle) {
		title2ModelId.put("No model loaded", "0");
		modelId2Title.put("0", "No model loaded");
		view.getListBox().addItem("No model loaded");
		
		title2ModelId.put("Test model", "1");
		modelId2Title.put("1", "Test model");
		view.getListBox().addItem("Test model");
		
		selectModel(title2ModelId.get(initialTitle));
	}
}
