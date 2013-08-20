package gov.lbl.glamm.client.map.presenter;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.ListBox;

/**
 * A presenter for user-uploaded group data.
 * The final format of this data will be determined later. I'm favoring something simple and in tune with other GLAMM upload formats.
 * Some data might require spaces, though, so comma-delimited might be best.
 * 
 * On the other hand, there could be the option to auto-detect a JSON file being uploaded as well, and try to unravel that. 
 * That's probably more appropriate for just the web service, though.
 * 
 * TODO: this is just a stub for now.
 * @author wjriehl
 *
 */
public class GroupDataUploadPresenter {

	public interface View {
		public FileUpload getUpload();
		public HasClickHandlers getCancelButton();
		public HasClickHandlers getSubmitButton();
		public ListBox getServiceList();
		public void hideView();
		public void showView();
	}
	
	@SuppressWarnings("unused")
	private SimpleEventBus eventBus;
	private View view;
	
	public GroupDataUploadPresenter(final View view, final SimpleEventBus eventBus) {
		this.view = view;
		this.eventBus = eventBus;
		
		bindView();
	}
	
	private void bindView() {
		view.getCancelButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				view.hideView();
			}
		});

		view.getSubmitButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				// do stuff.
			}
		});
	}
	
}
