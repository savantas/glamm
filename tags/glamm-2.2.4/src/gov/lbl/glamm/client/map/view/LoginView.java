package gov.lbl.glamm.client.map.view;

import gov.lbl.glamm.client.map.presenter.LoginPresenter;
import gov.lbl.glamm.shared.DeploymentDomain;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DecoratedPopupPanel;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

/** 
 * View for displaying login status and the login form.
 * @author jtbates
 *
 */
public class LoginView extends Composite
implements LoginPresenter.View {
	
	private static final String LABEL_USERNAME = "Username: ";
	private static final String LABEL_PASSWORD = "Password: ";
	private static final String BUTTON_LOG_IN = "Log In";
	private static final String BUTTON_LOG_OUT = "Log Out";
	private static final String BUTTON_CANCEL = "Cancel";
	private static final String BUTTON_VIEW_CART = "View Cart";
	
	
	
	private static final String HTML_REGISTER_TITLE		= "Register for an account.";
	private static final String HTML_REGISTER_URL_MO 	= "http://microbesonline.org/cgi-bin/register";
	private static String HTML_REGISTER_LINK_MO 		= "<a href=\"" + HTML_REGISTER_URL_MO + "\" target=\"_new\">" + HTML_REGISTER_TITLE + "</a>";
	private static final String HTML_REGISTER_URL_KBASE = "https://gologin.kbase.us/SignUp";
	private static String HTML_REGISTER_LINK_KBASE 		= "<a href=\"" + HTML_REGISTER_URL_KBASE + "\" target=\"_new\">" + HTML_REGISTER_TITLE + "</a>";
	
	private static final String HTML_RESET_TITLE		= "Forgot your password?";
	private static final String HTML_RESET_URL_MO 		= "http://microbesonline.org/cgi-bin/resetPassword";
	private static String HTML_RESET_LINK_MO 			= "<a href=\"" + HTML_RESET_URL_MO + "\" target=\"_new\">" + HTML_RESET_TITLE + "</a>";
	private static final String HTML_RESET_URL_KBASE 	= "https://gologin.kbase.us/ResetPassword";
	private static String HTML_RESET_LINK_KBASE 		= "<a href=\"" + HTML_RESET_URL_KBASE + "\" target=\"_new\">" + HTML_RESET_TITLE + "</a>";
	
	// the status window
	private DecoratorPanel decoratorPanel;
	private VerticalPanel mainPanel;
	private Label statusLabel;
	private HorizontalPanel buttonPanel;
	private Button viewCartButton;
	private Button logOutButton;
	
	
	// the form popup
	private DecoratedPopupPanel popup;
	private FormPanel formPanel;
	private VerticalPanel wrapperPanel;
	private Grid grid;
	private TextBox userNameTextBox;
	private PasswordTextBox passwordTextBox;
	private HorizontalPanel formButtonPanel;
	private Button logInButton;
	private Button cancelButton;
	
	private HTML registerLink;
	private HTML resetLink;
	
	
	/**
	 * Constructor
	 */
	public LoginView() {
		decoratorPanel = new DecoratorPanel();
		mainPanel = new VerticalPanel();
		statusLabel = new Label();
		buttonPanel = new HorizontalPanel();
		logOutButton = new Button(BUTTON_LOG_OUT);
		viewCartButton = new Button(BUTTON_VIEW_CART);
		
		popup = new DecoratedPopupPanel(false);
		formPanel = new FormPanel();
		wrapperPanel = new VerticalPanel();
		grid = new Grid(3, 2);
		userNameTextBox = new TextBox();
		passwordTextBox = new PasswordTextBox();
		formButtonPanel = new HorizontalPanel();
		logInButton = new Button(BUTTON_LOG_IN);
		cancelButton = new Button(BUTTON_CANCEL);
		
		registerLink = new HTML(HTML_REGISTER_LINK_KBASE);
		resetLink = new HTML(HTML_RESET_LINK_KBASE);
		
		init();
	}
	
	private void init() {
		
		// set up status window
		buttonPanel.setSpacing(5);
//		buttonPanel.add(viewCartButton);
		buttonPanel.add(logOutButton);
		
		mainPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		mainPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		mainPanel.add(statusLabel);
		mainPanel.add(buttonPanel);
		mainPanel.setWidth("12em");
		decoratorPanel.add(mainPanel);
		mainPanel.setStylePrimaryName("glamm-picker");

		// set up form popup
		grid.setWidget(0, 0, new Label(LABEL_USERNAME));
		grid.setWidget(0, 1, userNameTextBox);
		grid.setWidget(1, 0, new Label(LABEL_PASSWORD));
		grid.setWidget(1, 1, passwordTextBox);
		grid.setWidget(2, 1, resetLink);
		
		formButtonPanel.setSpacing(5);
		formButtonPanel.add(logInButton);
		formButtonPanel.add(cancelButton);
		
		wrapperPanel.add(registerLink);
		wrapperPanel.add(grid);
		wrapperPanel.add(formButtonPanel);
		
		formPanel.add(wrapperPanel);
		popup.add(formPanel);
		
		formPanel.setStylePrimaryName("glamm-picker");

		// set up form field names
		userNameTextBox.setName(LoginPresenter.FormField.USERNAME.toString());
		passwordTextBox.setName(LoginPresenter.FormField.PASSWORD.toString());
		
		initWidget(decoratorPanel);
	}
	
	@Override
	public Panel getButtonPanel() {
		return buttonPanel;
	}

	@Override
	public HasClickHandlers getCancelButton() {
		return cancelButton;
	}

	@Override
	public FormPanel getForm() {
		return formPanel;
	}

	@Override
	public HasText getPasswordTextBox() {
		return passwordTextBox;
	}

	@Override
	public PopupPanel getPopupPanel() {
		return popup;
	}

	@Override
	public Label getStatusLabel() {
		return statusLabel;
	}

	@Override
	public HasClickHandlers getLogInButton() {
		return logInButton;
	}
	
	@Override
	public HasClickHandlers getLogOutButton() {
		return logOutButton;
	}

	@Override
	public HasText getUserNameTextBox() {
		return userNameTextBox;
	}
	
	@Override
	public Button getViewCartButton() {
		return viewCartButton;
	}
	
	@Override
	public void setDomain(DeploymentDomain domain) {
		if (domain == DeploymentDomain.LBL) {
			registerLink.setHTML(HTML_REGISTER_LINK_MO);
			resetLink.setHTML(HTML_RESET_LINK_MO);
		}
		else {
			registerLink.setHTML(HTML_REGISTER_LINK_KBASE);
			resetLink.setHTML(HTML_RESET_LINK_KBASE);
		}
	}
}

