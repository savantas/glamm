package gov.lbl.glamm.client.presenter;

import gov.lbl.glamm.client.model.Organism;
import gov.lbl.glamm.client.model.Reaction;
import gov.lbl.glamm.client.model.Sample;
import gov.lbl.glamm.client.rpc.GlammServiceAsync;
import gov.lbl.glamm.client.view.ReactionView;

import java.util.HashSet;
import java.util.Set;

import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;

public class RxnPopupPresenter {

	private static enum State {
		LOADING,
		NO_REACTIONS,
		HAS_REACTIONS;
	}

	public interface View {
		public Panel getPanel();
		public Label getStatusLabel();
		public void hidePopup();
		public void killPopup();
		public void showPopup(int left, int top);
	}

	private GlammServiceAsync rpc;
	private View view;
	private SimpleEventBus eventBus;

	private Organism organism;
	private Set<Reaction> reactions;
	private Sample sample;
	
	private String isolateHost;
	private String metagenomeHost;
	private String host;

	public RxnPopupPresenter(final GlammServiceAsync rpc, final View view, final SimpleEventBus eventBus) {
		this.rpc = rpc;
		this.view = view;
		this.eventBus = eventBus;

		this.reactions = new HashSet<Reaction>();

		loadIsolateHost();
		loadMetagenomeHost();
		
		bindView();
	}

	private void addReactionsToPopup() { 

		// add new reaction views to panel
		for(Reaction reaction : reactions) {
			
			ReactionView rv = new ReactionView();
			ReactionPresenter rp = new ReactionPresenter(rpc, rv, eventBus);
			
			rp.setOrganism(organism);
			rp.setHost(host);
			rp.setReaction(reaction);
			
			view.getPanel().add(rv);
		}
	}

	private void bindView() {

	}
	
	private void clearReactionsPanel() {
		// remove children from panel
		view.getPanel().clear();
	}

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
	
	public void setHost() {
		if(organism == null || organism.isGlobalMap() || organism.isSessionOrganism())
			host = null;
		else if(organism.isMetagenome())
			host = metagenomeHost;
		else
			host = isolateHost;
	}

	public void setOrganism(final Organism organism) {
		this.organism = organism;
		setHost();
	}

	public void setReactions(final Set<Reaction> reactions) {
		this.reactions.clear();
		if(reactions != null && !reactions.isEmpty())
			this.reactions.addAll(reactions);
	}

	public void setSample(final Sample sample) {
		this.sample = sample;
	}

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
