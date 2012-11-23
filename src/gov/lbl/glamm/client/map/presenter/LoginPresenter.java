package gov.lbl.glamm.client.map.presenter;

import gov.lbl.glamm.client.map.events.LogInEvent;
import gov.lbl.glamm.client.map.events.LogOutEvent;
import gov.lbl.glamm.client.map.events.ViewResizedEvent;
import gov.lbl.glamm.client.map.rpc.GlammServiceAsync;
import gov.lbl.glamm.shared.model.User;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.http.client.UrlBuilder;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteHandler;
import com.google.gwt.user.client.ui.FormPanel.SubmitEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitHandler;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.PopupPanel;

/**
 * Presenter for logging a user in and out, as well as displaying login status.
 * @author jtbates
 *
 */
public class LoginPresenter {

	private static enum State {
		LOGGED_OUT,
		LOGGING_IN,
		LOGGED_IN;
	}
	
	/**
	 * Public enum for login form field names.
	 * @author jtbates
	 *
	 */
	public static enum FormField {
		USERNAME("username"),
		PASSWORD("pass1");
		
		private String formField;
		
		private FormField(final String formField) {
			this.formField = formField;
		}
		
		@Override
		public String toString() {
			return formField;
		}
	}
	
	/**
	 * View interface
	 * @author jtbates
	 *
	 */
	public interface View {
		
		/**
		 * Gets the panel containing the log out and view cart buttons.
		 * @return The panel.
		 */
		public Panel getButtonPanel();
		
		/**
		 * Gets the cancel button.
		 * @return The interface for the button.
		 */
		public HasClickHandlers getCancelButton();
		
		/**
		 * Gets the form panel.
		 * @return The form panel.
		 */
		public FormPanel getForm();
		
		/**
		 * Gets the password text box.
		 * @return The text box.
		 */
		public HasText getPasswordTextBox();
		
		/**
		 * Gets the popup panel containing the login form.
		 * @return The popup panel.
		 */
		public PopupPanel getPopupPanel();
		
		/**
		 * Gets the status label.
		 * @return The status label.
		 */
		public Label getStatusLabel();
		
		/**
		 * Gets the log in button.
		 * @return The interface for the button.
		 */
		public HasClickHandlers getLogInButton();
		
		/**
		 * Gets the log out button.
		 * @return The interface for the button.
		 */
		public HasClickHandlers getLogOutButton();
		
		/**
		 * Gets the username text box.
		 * @return The text box.
		 */
		public HasText getUserNameTextBox();
		
		/**
		 * Gets the view cart button.
		 * @return The interface for the button.
		 */
		public HasClickHandlers getViewCartButton();
		
		
	}

	private static final String ACTION_LOGIN = "loginKBase";

	public static final String COOKIE_AUTH = "auth";
	public static final String COOKIE_USERID = "userId";

	private static final String MSG_LOGGED_OUT = "Log in/Register";
	private static final String MSG_LOGGING_IN = "Logging in...";
	private static final String MSG_LOGGED_IN_PREFIX = "Logged in as ";

	private GlammServiceAsync rpc;
	private View view;
	private SimpleEventBus eventBus;

	private User user;
	private State state;

	/**
	 * Constructor
	 * @param rpc The GLAMM RPC service.
	 * @param view The View object for this presenter.
	 * @param eventBus The event bus.
	 */
	public LoginPresenter(final GlammServiceAsync rpc, final View view, final SimpleEventBus eventBus) {

		this.rpc = rpc;
		this.view = view;
		this.eventBus = eventBus;

		user = User.guestUser();
		setState(State.LOGGED_OUT);

		bindView();
		authenticateUser();
	}

	private void authenticateUser() {
		if(Cookies.isCookieEnabled()) {

			String auth = Cookies.getCookie(COOKIE_AUTH);
			String userId = Cookies.getCookie(COOKIE_USERID);

			rpc.authenticateUser(userId, auth, new AsyncCallback<User>() {
				@Override
				public void onFailure(Throwable caught) {
					Window.alert("Remote procedure call failure: authenticateUser");
					setState(State.LOGGED_OUT);
				}

				@Override
				public void onSuccess(User result) {
					logIn(result);
				}
			});
		}
	}

	private void bindView() {

		view.getStatusLabel().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				switch(state) {
				case LOGGED_OUT:
					showLoginForm();
					break;
				default:
					break;
				}
			}
		});
		
		view.getLogOutButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent evnet) {
				logOut();
			}
		});

		view.getCancelButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				view.getPopupPanel().hide();
				setState(State.LOGGED_OUT);
			}
		});

		view.getLogInButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				view.getForm().submit();
			}
		});
		
		view.getViewCartButton().addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				viewCart();
			}
		});

		UrlBuilder urlBuilder = Window.Location.createUrlBuilder();
		urlBuilder.setPath("glamm/glammServlet");
		urlBuilder.setParameter("action", ACTION_LOGIN);

		view.getForm().setAction(urlBuilder.buildString());
		view.getForm().setEncoding(FormPanel.ENCODING_MULTIPART);
		view.getForm().setMethod(FormPanel.METHOD_POST);

		view.getForm().addSubmitHandler(new SubmitHandler() {
			@Override
			public void onSubmit(SubmitEvent event) {
				if(!validateForm())
					event.cancel();
			}
		});

		view.getForm().addSubmitCompleteHandler(new SubmitCompleteHandler() {


			@Override
			public void onSubmitComplete(SubmitCompleteEvent event) {
				String results = event.getResults();
				if(results != null && !results.isEmpty())
					Window.alert(results);
				else {
					rpc.getLoggedInUser(new AsyncCallback<User>() {

						@Override
						public void onFailure(Throwable caught) {
							Window.alert("Remote procedure call failure: getLoggedInUser");
						}

						@Override
						public void onSuccess(User result) {
							view.getPopupPanel().hide();
							logIn(result);	
						}
					});
				}
			}

		});

	}

	private void viewCart() {
		
	}
	
	private void showLoginForm() {
		setState(State.LOGGING_IN);
		view.getPopupPanel().center();
		view.getPopupPanel().show();
	}

	private void logIn(final User user) {
		
		this.user = user;
		if(user.isGuestUser()) {
			setState(State.LOGGED_OUT);
			return;
		}
		
		if(Cookies.isCookieEnabled()) {
			Cookies.setCookie(COOKIE_AUTH, user.getAuth());
			Cookies.setCookie(COOKIE_USERID, user.getUserId());
		}
		eventBus.fireEvent(new LogInEvent(user));
		setState(State.LOGGED_IN);
	}

	private void logOut() {
		rpc.logOutUser(new AsyncCallback<Void>() {
			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Remote procedure call failure: logOutUser");				
			}

			@Override
			public void onSuccess(Void result) {
				if(Cookies.isCookieEnabled()) {
					Cookies.removeCookie(COOKIE_AUTH);
					Cookies.removeCookie(COOKIE_USERID);
				}
				eventBus.fireEvent(new LogOutEvent());
				setState(State.LOGGED_OUT);
			}
		});
	}

	private void setState(final State state) {
		this.state = state;

		switch(state) {
		default:
		case LOGGED_OUT:
			view.getStatusLabel().setText(MSG_LOGGED_OUT);
			view.getButtonPanel().setVisible(false);
			break;
		case LOGGING_IN:
			view.getStatusLabel().setText(MSG_LOGGING_IN);
			view.getButtonPanel().setVisible(false);
			break;
		case LOGGED_IN:
			view.getStatusLabel().setText(MSG_LOGGED_IN_PREFIX + user.getUserId());
			view.getButtonPanel().setVisible(true);
			break;
		}

		eventBus.fireEvent(new ViewResizedEvent());
	}


	private boolean validateForm() {
		boolean isValid = true;
		String errorMsg = "";
		
		if(view.getUserNameTextBox().getText().isEmpty() || view.getPasswordTextBox().getText().isEmpty()) {
			isValid = false;
			errorMsg = "Missing required fields.\n";
		}

		// show error message
		if(!isValid)
			Window.alert(errorMsg);

		return isValid;
	}

}
