package gov.lbl.glamm.client.map.events;

import gov.lbl.glamm.shared.model.kbase.workspace.KBWorkspaceObjectData;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

public class MetabolicModelChooserEvent extends GwtEvent<MetabolicModelChooserEvent.Handler> {

	public enum Action {
		REQUEST,
		SELECT
	}
	
	/**
	 * The EventHandler instance of this event.
	 * @author wjriehl
	 *
	 */
	public interface Handler extends EventHandler {
		public void onRequest(MetabolicModelChooserEvent event);
		public void onSelected(MetabolicModelChooserEvent event);
	}

	private List<KBWorkspaceObjectData> modelInfo;
	private Action action;
	
	/**
	 * The Type associated with this event.
	 */
	public static final Type<Handler> TYPE = new Type<Handler>();
	
	/**
	 * Gets the Type associated with this event.
	 * @return the Type associated with this event.
	 */
	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<Handler> getAssociatedType() {
		return TYPE;
	}

	/**
	 * Dispatches this event.
	 */
	@Override
	protected void dispatch(Handler handler) {
		if (action == Action.REQUEST)
			handler.onRequest(this);
		else
			handler.onSelected(this);
	}
	
	/**
	 * Constructor
	 * @param model the MetabolicModel
	 */
	public MetabolicModelChooserEvent(Action action) {
		this.action = action;
		modelInfo = new ArrayList<KBWorkspaceObjectData>();
	}
	
	public MetabolicModelChooserEvent(Action action, List<KBWorkspaceObjectData> dataList) {
		this(action);
		modelInfo.addAll(dataList);
	}
	
	public List<KBWorkspaceObjectData> getModelInfo() {
		return modelInfo;
	}
}
