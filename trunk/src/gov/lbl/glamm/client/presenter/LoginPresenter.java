package gov.lbl.glamm.client.presenter;

import gov.lbl.glamm.client.rpc.GlammServiceAsync;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Label;

public class LoginPresenter {
	
	private static String COOKIE_MOL_USER_ID = "userId";
	private static String MOL_DOMAIN = "microbesonline.org";
	
	public interface View {
		public Label getLoginLabel();
	}
	
	private enum State {
		LOGGED_OUT_NON_MOL,
		LOGGED_OUT_MOL,
		LOGGED_IN;
	}
	
	private GlammServiceAsync rpc = null;
	private View view = null;
	private SimpleEventBus eventBus = null;
	
	private State state = State.LOGGED_OUT_NON_MOL;
	private String userEmail = null;
	private String userId = null;
	
	public LoginPresenter(GlammServiceAsync rpc, View view, SimpleEventBus eventBus) {
		this.rpc = rpc;
		this.view = view;
		this.eventBus = eventBus;
		
		bindView();
	}
	
	private void bindView() {
		view.getLoginLabel().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				
			}
		});
	}
	
	
	
	public void update() {
		if(Window.Location.getHostName().endsWith(MOL_DOMAIN)) {
			if(Cookies.isCookieEnabled())
				userId = Cookies.getCookie(COOKIE_MOL_USER_ID);
			if(userId == null || userId.isEmpty())
				state = State.LOGGED_OUT_MOL;
			else
				state = State.LOGGED_IN;
		}
		else
			state = State.LOGGED_OUT_NON_MOL;
		
		setViewState();
	}
	
	private void setViewState() { 
		switch(state) {
			default:
			case LOGGED_OUT_NON_MOL:
				break;
			
			case LOGGED_OUT_MOL:
				break;
				
			case LOGGED_IN:
				break;
		}
	}
}
