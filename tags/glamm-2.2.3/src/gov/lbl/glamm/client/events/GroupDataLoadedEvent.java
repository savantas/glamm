package gov.lbl.glamm.client.events;

import java.util.Set;

import gov.lbl.glamm.client.model.OverlayDataGroup;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

/**
 * Event signaling that group data has been loaded.
 * @author wjriehl
 *
 */
public class GroupDataLoadedEvent extends GwtEvent<GroupDataLoadedEvent.Handler> {
	
	/**
	 * The EventHandler instance of this event.
	 * @author wjriehl
	 *
	 */
	public interface Handler extends EventHandler {
		public void onLoaded(GroupDataLoadedEvent event);
	}

	/**
	 * The Type associated with this event.
	 */
	public static final Type<Handler> TYPE = new Type<Handler>();
	
	private Set<OverlayDataGroup> dataSet;
	
	/**
	 * Constructor
	 * @param model the Set of OverlayDataGroups
	 */
	public GroupDataLoadedEvent(Set<OverlayDataGroup> dataSet) {
		this.dataSet = dataSet;
	}
	
	/**
	 * Gets the set of OverlayDataGroups associated with this event.
	 * @return the set of OverlayDataGroups associated with this event.
	 */
	public Set<OverlayDataGroup> getData() {
		return dataSet;
	}
	
	/**
	 * Gets the Type associated with this event.
	 * @return the Type associated with this event.
	 */
	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<Handler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(Handler handler) {
		handler.onLoaded(this);
	}

}
