package gov.lbl.glamm.client.events;

import gov.lbl.glamm.client.model.interfaces.HasType;

import java.util.HashSet;
import java.util.Set;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

/**
 * Event indicating a search target has been found.
 * @author jtbates
 *
 */
public class SearchTargetEvent extends GwtEvent<SearchTargetEvent.Handler> {
	
	/**
	 * The EventHandler interface for this event.
	 * @author jtbates
	 *
	 */
	public interface Handler extends EventHandler {
		public void onPicked(SearchTargetEvent event);
	}
	
	/**
	 * The Type associated with this event.
	 */
	public static final Type<Handler> TYPE = new Type<Handler>();
	
	private Set<HasType> targets;

	/**
	 * Gets the Type associated with this event.
	 * @return The Type associated with this event.
	 */
	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<Handler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(Handler handler) {
		handler.onPicked(this);
	}
	
	/**
	 * Constructor
	 * @param target The target for this event.
	 */
	public SearchTargetEvent(HasType target) {
		this.targets = new HashSet<HasType>();
		if(target != null)
			this.targets.add(target);
	}
	
	/**
	 * Constructor
	 * @param targets The set of targets for this event.
	 */
	public SearchTargetEvent(Set<HasType> targets) {
		this.targets = new HashSet<HasType>();
		if(targets != null && !targets.isEmpty())
			this.targets.addAll(targets);
	}
	
	/**
	 * Gets the targets associated with this event.
	 * @return The set of targets associated with this event.
	 */
	public Set<HasType> getTargets() {
		return targets;
	}
	
}
