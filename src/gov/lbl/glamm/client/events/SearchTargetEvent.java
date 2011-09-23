package gov.lbl.glamm.client.events;

import gov.lbl.glamm.client.model.interfaces.Mappable;

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
	public interface Handler extends EventHandler {
		public void onPicked(SearchTargetEvent event);
	}
	
	public static final Type<Handler> TYPE = new Type<Handler>();
	
	private Set<Mappable> targets;

	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<Handler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(Handler handler) {
		handler.onPicked(this);
	}
	
	public SearchTargetEvent(Mappable target) {
		this.targets = new HashSet<Mappable>();
		if(target != null)
			this.targets.add(target);
	}
	
	public SearchTargetEvent(Set<Mappable> targets) {
		this.targets = new HashSet<Mappable>();
		if(targets != null && !targets.isEmpty())
			this.targets.addAll(targets);
	}
	
	public Set<Mappable> getTargets() {
		return targets;
	}
	
}
