package gov.lbl.glamm.client.experiment.presenter;

import gov.lbl.glamm.client.experiment.events.DataChangedEvent;
import gov.lbl.glamm.client.experiment.events.ExpHorizScrollEvent;
import gov.lbl.glamm.client.experiment.events.ObjectCountEvent;
import gov.lbl.glamm.client.experiment.events.PathwayZoomEvent;
import gov.lbl.glamm.client.experiment.events.ScrollEvent;
import gov.lbl.glamm.client.experiment.util.DrawUtil;
import gov.lbl.glamm.client.experiment.util.LengthsAndRatios;
import gov.lbl.glamm.client.experiment.view.ScrollSVGView;
import gov.lbl.glamm.shared.model.Experiment;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;

public class SampleGuidePresenter {
	public interface View extends ScrollSVGView {
		public void reDraw( List<Experiment> experiments );
	}

	private View view = null;
	private HandlerManager eventBus = null;
	private DrawUtil drawUtil = null;

	private ArrayList<HandlerRegistration> handlerRegistrations = new ArrayList<HandlerRegistration>();

	public SampleGuidePresenter( final View view, HandlerManager eventBus
			, DrawUtil drawUtil ) {
		this.view = view;
		this.eventBus = eventBus;
		this.drawUtil = drawUtil;
		HandlerRegistration handlerReg = eventBus.addHandler(
				DataChangedEvent.ASSOCIATED_TYPE
				, new DataChangedEvent.Handler() {
			@Override
			public void handleDataReceived(DataChangedEvent dcEvent) {
				view.reDraw(dcEvent.getExperiments());
				@SuppressWarnings("unused")
				LengthsAndRatios lengthsRatios = SampleGuidePresenter.this.drawUtil
						.getExperimentLengthsAndRatios( dcEvent.getExperiments() );

				// send object count
				SampleGuidePresenter.this.eventBus.fireEvent(
						new ObjectCountEvent( ObjectCountEvent.CountType
								.VIEW, view.getObjCount() ) );
			}
		});
		handlerRegistrations.add(handlerReg);
		handlerReg = eventBus.addHandler(
				PathwayZoomEvent.ASSOCIATED_TYPE
				, new PathwayZoomEvent.Handler() {
			@Override
			public void handleZoom(PathwayZoomEvent pzEvent) {
				view.setZoomRatio(pzEvent.getZoomRatio());
			}
		});
		handlerRegistrations.add(handlerReg);
		handlerReg = eventBus.addHandler(
				ExpHorizScrollEvent.ASSOCIATED_TYPE
				, new ExpHorizScrollEvent.Handler() {
			@Override
			@SuppressWarnings("rawtypes")
			public void handleScroll(ScrollEvent se) {
				view.setHorizScrollRatio(se.getScrollRatio());
			}
		});
		handlerRegistrations.add(handlerReg);
	}

	public void removeHandlers() {
		for ( HandlerRegistration reg : handlerRegistrations ) {
			reg.removeHandler();
		}
		handlerRegistrations.clear();
	}

	public SampleGuidePresenter.View getView() {
		return view;
	}
}
