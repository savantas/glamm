package gov.lbl.glamm.client.view;

import gov.lbl.glamm.client.model.Gene;
import gov.lbl.glamm.client.presenter.ReactionPresenter;

import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHTML;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * View for displaying reactions, typically in the context of RxnPopupViews.
 * @author jtbates
 *
 */
public class ReactionView extends Composite
implements ReactionPresenter.View {
	
	private final String ADD_TO_CART = "Add to cart";
	private VerticalPanel mainPanel;
	private HTML definitionHtml;
	private HTML ecNumHtml;
	private Button addToCart;
	private CellTable<Gene> geneTable;
	private ScrollPanel geneTableScrollPanel;

	/**
	 * Constructor
	 */
	public ReactionView() {
		mainPanel = new VerticalPanel();
		definitionHtml = new HTML();
		ecNumHtml = new HTML();
		addToCart = new Button(ADD_TO_CART);
		geneTable = new CellTable<Gene>();
		geneTableScrollPanel = new ScrollPanel();
		
		mainPanel.add(definitionHtml);
		mainPanel.add(ecNumHtml);
//		mainPanel.add(addToCart);
		mainPanel.add(geneTableScrollPanel);
		geneTableScrollPanel.add(geneTable);
		
		geneTableScrollPanel.setSize("100%", "10em");
		geneTable.setWidth("100%");
		mainPanel.setWidth("30em");
		mainPanel.setStylePrimaryName("glamm-picker");
		initWidget(mainPanel);
	}

	@Override
	public Button getAddToCartButton() {
		return addToCart;
	}
	
	@Override
	public HasHTML getDefinitionHtml() {
		return definitionHtml;
	}

	@Override
	public HasHTML getEcNumHtml() {
		return ecNumHtml;
	}

	@Override
	public CellTable<Gene> getGeneTable() {
		return geneTable;
	}

	@Override
	public void hideGeneTable() {
		geneTable.setVisible(false);
		geneTableScrollPanel.setVisible(false);
	}

	@Override
	public void showGeneTable() {
		geneTable.setVisible(true);
		geneTableScrollPanel.setVisible(true);
	}
}
