package gov.lbl.glamm.client.view;

import gov.lbl.glamm.client.presenter.LoginPresenter;

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
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class LoginView extends Composite
implements LoginPresenter.View {
	
	private static final String LABEL_USERNAME = "Username: ";
	private static final String LABEL_PASSWORD = "Password: ";
	private static final String BUTTON_LOGIN = "Login";
	private static final String BUTTON_CANCEL = "Cancel";
	
	
	
	private static final String HTML_REGISTER_TITLE	= "Register for an account.";
	private static final String HTML_REGISTER_URL 	= "http://microbesonline.org/cgi-bin/register";
	private static final String HTML_REGISTER_LINK 	= "<a href=\"" + HTML_REGISTER_URL + "\" target=\"_new\">" + HTML_REGISTER_TITLE + "</a>";
	
	private static final String HTML_RESET_TITLE	= "Forgot your password?";
	private static final String HTML_RESET_URL 		= "http://microbesonline.org/cgi-bin/resetPassword";
	private static final String HTML_RESET_LINK 	= "<a href=\"" + HTML_RESET_URL + "\" target=\"_new\">" + HTML_RESET_TITLE + "</a>";
	
	// the status window
	private DecoratorPanel decoratorPanel;
	private VerticalPanel mainPanel;
	private Label statusLabel;
	
	// the form popup
	private DecoratedPopupPanel popup;
	private FormPanel formPanel;
	private VerticalPanel wrapperPanel;
	private Grid grid;
	private TextBox userNameTextBox;
	private PasswordTextBox passwordTextBox;
	private HorizontalPanel buttonPanel;
	private Button loginButton;
	private Button cancelButton;
	
	
	public LoginView() {
		decoratorPanel = new DecoratorPanel();
		mainPanel = new VerticalPanel();
		statusLabel = new Label("THIS IS A TEST");
		
		popup = new DecoratedPopupPanel(false);
		formPanel = new FormPanel();
		wrapperPanel = new VerticalPanel();
		grid = new Grid(3, 2);
		userNameTextBox = new TextBox();
		passwordTextBox = new PasswordTextBox();
		buttonPanel = new HorizontalPanel();
		loginButton = new Button(BUTTON_LOGIN);
		cancelButton = new Button(BUTTON_CANCEL);
		
		init();
	}
	
	private void init() {
		
		// set up status window
		mainPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		mainPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		mainPanel.add(statusLabel);
		mainPanel.setWidth("7em");
		decoratorPanel.add(mainPanel);
		
		mainPanel.setStylePrimaryName("glamm-picker");

		// set up form popup
		grid.setWidget(0, 0, new Label(LABEL_USERNAME));
		grid.setWidget(0, 1, userNameTextBox);
		grid.setWidget(1, 0, new Label(LABEL_PASSWORD));
		grid.setWidget(1, 1, passwordTextBox);
		grid.setWidget(2, 1, new HTML(HTML_RESET_LINK));
		
		buttonPanel.setSpacing(5);
		buttonPanel.add(loginButton);
		buttonPanel.add(cancelButton);
		
		wrapperPanel.add(new HTML(HTML_REGISTER_LINK));
		wrapperPanel.add(grid);
		wrapperPanel.add(buttonPanel);
		
		formPanel.add(wrapperPanel);
		popup.add(formPanel);
		
		formPanel.setStylePrimaryName("glamm-picker");

		// set up form field names
		userNameTextBox.setName(LoginPresenter.FormField.USERNAME.toString());
		passwordTextBox.setName(LoginPresenter.FormField.PASSWORD.toString());
		
		initWidget(decoratorPanel);
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
	public HasClickHandlers getLoginButton() {
		return loginButton;
	}

	@Override
	public HasText getUserNameTextBox() {
		return userNameTextBox;
	}
}

