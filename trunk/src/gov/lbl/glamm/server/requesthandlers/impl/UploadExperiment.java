package gov.lbl.glamm.server.requesthandlers.impl;

import gov.lbl.glamm.client.model.Experiment;
import gov.lbl.glamm.client.model.Measurement;
import gov.lbl.glamm.client.model.Sample;
import gov.lbl.glamm.client.presenter.ExperimentUploadPresenter;
import gov.lbl.glamm.server.FileUploadHandler;
import gov.lbl.glamm.server.RequestHandler;
import gov.lbl.glamm.server.ResponseHandler;
import gov.lbl.glamm.server.SessionManager;
import gov.lbl.glamm.server.FileUploadHandler.LineParser;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UploadExperiment implements RequestHandler {

	private static final String FILE_PARSE_WARNING_MALFORMED_LINE 		= "Malformed line";

	@Override
	public void handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws IOException {

		// get the session manager, create a new one if necessary
		final SessionManager sm = SessionManager.getSessionManager(request, true);

		// it's always most convenient to hash measurements by the ids of their targets
		final HashMap<String, HashSet<Measurement>> id2Measurement = new HashMap<String, HashSet<Measurement>>();

		final String expId		= sm.nextAvailableExperimentId();
		final String sampleId	= "1";

		// set up the file upload handler- simultaneously building the measurement hash
		FileUploadHandler fuh = new FileUploadHandler(request, new LineParser() {
			public String parseLine(String line) {

				final int MIN_TOKENS		= 2;
				final int TARGET_ID_INDEX 	= 0;
				final int VALUE_INDEX		= 1;
				final int CONFIDENCE_INDEX	= 2;

				String errorMsg = null;
				String[] tokens = line.split("\t");

				// ensure that there are at least two tokens per line
				if(tokens.length < MIN_TOKENS) {
					errorMsg = FILE_PARSE_WARNING_MALFORMED_LINE + ": " + line;
				}
				else {

					String targetId 	= tokens[TARGET_ID_INDEX];
					String value 		= tokens[VALUE_INDEX];
					String confidence 	= (tokens.length >= 3 ? tokens[CONFIDENCE_INDEX] : null);

					HashSet<Measurement> measurements = id2Measurement.get(targetId);
					if(measurements == null) {
						measurements = new HashSet<Measurement>();
						id2Measurement.put(targetId, measurements);
					}
					measurements.add(new Measurement(expId, 
							sampleId, 
							Float.parseFloat(value), 
							(confidence != null ? Float.parseFloat(confidence) : 0f), 
							targetId));
				}

				return errorMsg;
			}
		});

		// get relevant fields
		final float clampMin 	= Float.parseFloat(fuh.getFormField(ExperimentUploadPresenter.View.FIELD_EXP_UPLOAD_CLAMP_MIN));
		final float clampMid 	= Float.parseFloat(fuh.getFormField(ExperimentUploadPresenter.View.FIELD_EXP_UPLOAD_CLAMP_MID));
		final float clampMax 	= Float.parseFloat(fuh.getFormField(ExperimentUploadPresenter.View.FIELD_EXP_UPLOAD_CLAMP_MAX));
		final String stress 	= fuh.getFormField(ExperimentUploadPresenter.View.FIELD_EXP_UPLOAD_STRESS);
		final String treatment 	= fuh.getFormField(ExperimentUploadPresenter.View.FIELD_EXP_UPLOAD_TREATMENT);
		final String control 	= fuh.getFormField(ExperimentUploadPresenter.View.FIELD_EXP_UPLOAD_CONTROL);
		final String taxonomyId = fuh.getFormField(ExperimentUploadPresenter.View.FIELD_EXP_UPLOAD_TAXONOMY_ID);
		final String units 		= fuh.getFormField(ExperimentUploadPresenter.View.FIELD_EXP_UPLOAD_UNITS);


		// construct the sample
		Sample sample = new Sample(expId, sampleId, taxonomyId, Experiment.EXP_SRC_SESSION);
		sample.setClampValues(clampMin, clampMid, clampMax);
		sample.setStress(stress);
		sample.setTreatment(treatment, null);
		sample.setControl(control, null);
		sample.setUnits(units);

		// construct the experiment
		Experiment experiment = new Experiment(expId, taxonomyId, Experiment.EXP_SRC_SESSION);
		experiment.addSample(sample);

		// add the experiment to the session
		sm.addExperiment(experiment, taxonomyId);

		// add the id2Measurement map to the session
		sm.addMeasurements(expId, sampleId, taxonomyId, id2Measurement);

		// handle response
		ResponseHandler.asHtml(response, fuh.getErrorMessages(), HttpServletResponse.SC_OK);

	}

}
