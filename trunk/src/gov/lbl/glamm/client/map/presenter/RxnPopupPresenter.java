package gov.lbl.glamm.client.map.presenter;

import gov.lbl.glamm.client.map.rpc.GlammServiceAsync;
import gov.lbl.glamm.client.map.view.ReactionView;
import gov.lbl.glamm.shared.model.Organism;
import gov.lbl.glamm.shared.model.OverlayDataGroup;
import gov.lbl.glamm.shared.model.Reaction;
import gov.lbl.glamm.shared.model.Sample;
import gov.lbl.glamm.shared.model.User;

import java.util.HashSet;
import java.util.Set;

import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;

/**
 * Presenter for reaction popups.
 * @author jtbates
 *
 */
public class RxnPopupPresenter {

	private static enum State {
		LOADING,
		NO_REACTIONS,
		HAS_REACTIONS;
	}

	/**
	 * View interface.
	 * @author jtbates
	 *
	 */
	public interface View {
		
		/**
		 * Gets the reaction popup panel.
		 * @return The panel.
		 */
		public Panel getPanel();
		
		/**
		 * Gets the status label.
		 * @return The label.
		 */
		public Label getStatusLabel();
		
		/**
		 * Hides the popup.
		 */
		public void hidePopup();
		
		/**
		 * Kills the popup.
		 */
		public void killPopup();
		
		/**
		 * Shows the popup at a position in client space.
		 * @param left The client space x position.
		 * @param top The client space y position.
		 */
		public void showPopup(int left, int top);
	}

	private GlammServiceAsync rpc;
	private View view;
	private SimpleEventBus eventBus;

	private Organism organism;
	private Set<Reaction> reactions;
	private Sample sample;
	private Set<OverlayDataGroup> dataGroups;
	
	private String isolateHost;
	private String metagenomeHost;
	private String host;
	
	private User user;

	/**
	 * Constructor
	 * @param rpc The GLAMM RPC service.
	 * @param view The View object for this presenter.
	 * @param eventBus The event bus.
	 */
	public RxnPopupPresenter(final GlammServiceAsync rpc, final View view, final SimpleEventBus eventBus) {
		this.rpc = rpc;
		this.view = view;
		this.eventBus = eventBus;

		this.reactions = new HashSet<Reaction>();
		this.user = User.guestUser();
		
		loadIsolateHost();
		loadMetagenomeHost();
		
		bindView();
	}

	private void addReactionsToPopup() { 

		// add new reaction views to panel
		for(Reaction reaction : reactions) {
			
			ReactionView rv = new ReactionView();
			ReactionPresenter rp = new ReactionPresenter(rpc, rv, eventBus);

			Set<OverlayDataGroup> memberGroups = new HashSet<OverlayDataGroup>();
			if (dataGroups != null && !dataGroups.isEmpty()) {
				for (OverlayDataGroup g : dataGroups) {
					if (g.containsReaction(reaction)) {
						memberGroups.add(g);
					}
				}
				System.out.println();
				rp.setDataGroups(memberGroups);
			}
			
			rp.setOrganism(organism);
			rp.setHost(host);
			rp.setReaction(reaction);
			rp.setUser(user);
			
			view.getPanel().add(rv);
		}
	}

	private void bindView() {

	}
	
	private void clearReactionsPanel() {
		// remove children from panel
		view.getPanel().clear();
	}

	/**
	 * Kills the popup.
	 */
	public void killPopup() {
		view.killPopup();
	}
	
	private void loadIsolateHost() {
		rpc.getIsolateHost(new AsyncCallback<String>() {
			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Remote procedure call failure: getIsolateHost");
			}

			@Override
			public void onSuccess(String result) {
				isolateHost = result;
			}
		});
	}

	private void loadMetagenomeHost() {
		rpc.getMetagenomeHost(new AsyncCallback<String>() {
			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Remote procedure call failure: getMetagenomeHost");
			}

			@Override
			public void onSuccess(String result) {
				metagenomeHost = result;
			}
		});
	}
	
	/**
	 * Sets the host for MicrobesOnline links.
	 * If the current organism is null, the global map, or a user-uploaded session organism, the host is null.
	 * If the current organism is a metagenome, the host is the metagenome host (typically MetaMicrobesOnline.)
	 * If the current organism is an isolate (i.e. non-metagnome) the host is MicrobesOnline.
	 */
	public void setHost() {
		if(organism == null || organism.isGlobalMap() || organism.isSessionOrganism())
			host = null;
		else if(organism.isMetagenome())
			host = metagenomeHost;
		else
			host = isolateHost;
	}

	/**
	 * Sets the current organism.
	 * @param organism The organism.
	 */
	public void setOrganism(final Organism organism) {
		this.organism = organism;
		setHost();
	}
	
	public void setDataGroups(Set<OverlayDataGroup> dataGroups) {
		this.dataGroups = dataGroups;
	}

	private void setReactions(final Set<Reaction> reactions) {
		this.reactions.clear();
		if(reactions != null && !reactions.isEmpty())
			this.reactions.addAll(reactions);
	}

	/**
	 * Sets the current sample.
	 * @param sample The sample.
	 */
	public void setSample(final Sample sample) {
		this.sample = sample;
	}

	/**
	 * Shows the popup.
	 * @param ids The set of reaction ids to display.
	 * @param left The client space X coordinate.
	 * @param top The client space Y coordinate.
	 */
	public void showPopup(final Set<String> ids, final int left, final int top) {

		setViewState(State.LOADING);
		view.showPopup(left, top);

		rpc.getReactions(ids, organism, sample, new AsyncCallback<Set<Reaction>>() {
			@Override
			public void onFailure(Throwable caught) {
				// Show the RPC error message to the user
				Window.alert("Remote procedure call failure: getReactions.");
			}

			@Override
			public void onSuccess(Set<Reaction> result) {
				setReactions(result);
				if(reactions.isEmpty())
					setViewState(State.NO_REACTIONS);
				else {
					clearReactionsPanel();
					addReactionsToPopup();
					setViewState(State.HAS_REACTIONS);
				}
			}
		});
	}
	
	/**
	 * Sets the user.
	 * @param user The user.
	 */
	public void setUser(final User user) {
		this.user = user;
	}

	private void setViewState(final State state) {
		switch(state) {
		case LOADING: {
			final String msg = "Loading...";
			view.getStatusLabel().setText(msg);
			view.getStatusLabel().setVisible(true);
			view.getPanel().setVisible(false);
			break;
		}

		case NO_REACTIONS: {
			final String msg = "No reactions found.";
			view.getStatusLabel().setText(msg);
			view.getStatusLabel().setVisible(true);
			view.getPanel().setVisible(false);
			break;
		}

		case HAS_REACTIONS: {
			view.getStatusLabel().setVisible(false);
			view.getPanel().setVisible(true);
			break;
		}
		}	
	}


}
