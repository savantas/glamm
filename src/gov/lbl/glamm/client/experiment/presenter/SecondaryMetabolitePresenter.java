package gov.lbl.glamm.client.experiment.presenter;

import gov.lbl.glamm.client.experiment.events.DataChangedEvent;
import gov.lbl.glamm.client.experiment.events.NameTypeChangeEvent;
import gov.lbl.glamm.client.experiment.events.ObjectCountEvent;
import gov.lbl.glamm.client.experiment.events.PathwayZoomEvent;
import gov.lbl.glamm.client.experiment.events.ScrollEvent;
import gov.lbl.glamm.client.experiment.events.SecondaryVertScrollEvent;
import gov.lbl.glamm.client.experiment.model.ViewCompound;
import gov.lbl.glamm.client.experiment.util.BinarySortedSet;
import gov.lbl.glamm.client.experiment.util.SVGData;
import gov.lbl.glamm.client.experiment.view.ScrollSVGView;

import java.util.ArrayList;

import com.google.gwt.event.shared.HandlerManager;

/**
 * Presenter for secondary metabolite key.
 * Updates view when notified of new data and of relevant zoom and scroll events.
 *
 * @author DHAP Digital, Inc - angie
 *
 */
public class SecondaryMetabolitePresenter {
	public interface View extends ScrollSVGView {
		public SVGData createSecondaryMetaboliteSvg(
				BinarySortedSet<ViewCompound> metabolites
				, ArrayList<String> dynamicDefBaseNameList );

		void showLongNames();
		void showShortNames();
	}

	@SuppressWarnings("unused")
	private View view = null;
	private HandlerManager eventBus = null;

	public SecondaryMetabolitePresenter( final View view, HandlerManager eventBus ) {
		this.view = view;
		this.eventBus = eventBus;
		this.eventBus.addHandler(DataChangedEvent.ASSOCIATED_TYPE, new DataChangedEvent.Handler() {
			@Override
			public void handleDataReceived(DataChangedEvent dcEvent) {
				SVGData svgData = null;
				// construct new svg
				svgData = view.createSecondaryMetaboliteSvg( dcEvent.getSecondaryMetaboliteList()
						, dcEvent.getDynamicDefBaseNameList() );
				// replace svg in view
				view.replaceSvg(svgData);
				view.showShortNames();

				// send object count
				SecondaryMetabolitePresenter.this.eventBus.fireEvent(
						new ObjectCountEvent( ObjectCountEvent.CountType
								.VIEW, view.getObjCount() ) );
			}
		});
		this.eventBus.addHandler( PathwayZoomEvent.ASSOCIATED_TYPE
				, new PathwayZoomEvent.Handler() {
			@Override
			public void handleZoom(PathwayZoomEvent pzEvent) {
				view.setZoomRatio(pzEvent.getZoomRatio());
			}
		});
		this.eventBus.addHandler( SecondaryVertScrollEvent.ASSOCIATED_TYPE
				, new SecondaryVertScrollEvent.Handler() {
			@Override
			@SuppressWarnings("rawtypes")
			public void handleScroll(ScrollEvent se) {
				view.setVerticalScrollRatio(se.getScrollRatio());
			}
		});
		this.eventBus.addHandler( NameTypeChangeEvent.ASSOCIATED_TYPE
				, new NameTypeChangeEvent.Handler() {
			@Override
			public void handleNameTypeChange(NameTypeChangeEvent ntcEvent) {
				if ( ntcEvent == NameTypeChangeEvent.LONG_NAME_EVENT ) {
					view.showLongNames();
				} else {
					view.showShortNames();
				}
			}
		});
	}
}
