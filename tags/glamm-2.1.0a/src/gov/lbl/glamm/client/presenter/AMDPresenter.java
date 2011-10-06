package gov.lbl.glamm.client.presenter;

import gov.lbl.glamm.client.events.AMDPickedEvent;
import gov.lbl.glamm.client.model.AnnotatedMapDescriptor;
import gov.lbl.glamm.client.rpc.GlammServiceAsync;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.ListBox;

public class AMDPresenter {
	
	public interface View {
		public ListBox getListBox();
	}
	
	private GlammServiceAsync rpc;
	private View view;
	private SimpleEventBus eventBus;
	
	private Map<String, AnnotatedMapDescriptor> title2AMD;
	private Map<String, AnnotatedMapDescriptor> id2AMD;
	
	public AMDPresenter(final GlammServiceAsync rpc, final View view, final SimpleEventBus eventBus) {
		this.rpc = rpc;
		this.view = view;
		this.eventBus = eventBus;
		
		this.title2AMD = new HashMap<String, AnnotatedMapDescriptor>();
		this.id2AMD = new HashMap<String, AnnotatedMapDescriptor>();
		
		bindView();
	}
	
	public void bindView() {
		view.getListBox().addChangeHandler(new ChangeHandler() {
			@Override
			public void onChange(ChangeEvent event) {
				int index = view.getListBox().getSelectedIndex();
				String title = view.getListBox().getItemText(index);
				eventBus.fireEvent(new AMDPickedEvent(title2AMD.get(title)));
			}
		});
	}
	
	public void selectMap(final String mapId) {
		AnnotatedMapDescriptor amd = id2AMD.get(mapId);
		if(amd == null)
			return;
		
		ListBox listBox = view.getListBox();
		for(int i = 0; i < listBox.getItemCount(); i++) {
			if(amd.getTitle().equals(listBox.getItemText(i))) {
				listBox.setSelectedIndex(i);
				eventBus.fireEvent(new AMDPickedEvent(amd));
				break;
			}
		}
	}
	
	public void populate(final String selectMapId) {
		rpc.getAnnotatedMapDescriptors(new AsyncCallback<List<AnnotatedMapDescriptor>>() {

			@Override
			public void onFailure(Throwable caught) {
				// Show the RPC error message to the user
				Window.alert("Remote procedure call failure: getAnnotatedMapDescriptors");
			}

			@Override
			public void onSuccess(List<AnnotatedMapDescriptor> result) {
				for(AnnotatedMapDescriptor amd : result) {
					title2AMD.put(amd.getTitle(), amd);
					id2AMD.put(amd.getMapId(), amd);
					view.getListBox().addItem(amd.getTitle());
				}
				selectMap(selectMapId);
			}
			
		});
	}
}
