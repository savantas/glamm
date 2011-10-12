package gov.lbl.glamm.client.presenter;

public class RxnPopupPresenter {
	public interface View {
		public void hidePopup();
		public void killPopup();
		public void showPopup(String content, int left, int top);
	}
}
