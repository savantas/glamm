package gov.lbl.glamm.server.actions.requesthandlers;

import gov.lbl.glamm.client.model.Experiment;
import gov.lbl.glamm.client.model.Measurement;
import gov.lbl.glamm.client.model.Organism;
import gov.lbl.glamm.client.model.Sample;
import gov.lbl.glamm.client.model.Sample.DataType;
import gov.lbl.glamm.client.model.Sample.TargetType;
import gov.lbl.glamm.client.presenter.ExperimentUploadPresenter;
import gov.lbl.glamm.server.FormRequestHandler;
import gov.lbl.glamm.server.FormRequestHandler.LineParser;
import gov.lbl.glamm.server.GlammSession;
import gov.lbl.glamm.server.RequestHandler;
import gov.lbl.glamm.server.ResponseHandler;
import gov.lbl.glamm.server.dao.OrganismDAO;
import gov.lbl.glamm.server.dao.impl.OrganismDAOImpl;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UploadExperiment implements RequestHandler {

	private static final String FILE_PARSE_WARNING_MALFORMED_LINE 		= "Malformed line";

	@Override
	public void handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws IOException {

		// get the session manager, create a new one if necessary
		final GlammSession sm = GlammSession.getGlammSession(request);

		// it's always most convenient to hash measurements by the ids of their targets
		final Map<String, Set<Measurement>> id2Measurement = new HashMap<String, Set<Measurement>>();

		final String expId		= sm.nextAvailableExperimentId();
		final String sampleId	= "1";

		// set up the file upload handler- simultaneously building the measurement hash
		FormRequestHandler fuh = new FormRequestHandler(request, new LineParser() {
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

					Set<Measurement> measurements = id2Measurement.get(targetId);
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
		final float clampMin 	= Float.parseFloat(fuh.getFormField(ExperimentUploadPresenter.FormField.CLAMP_MIN.toString()));
		final float clampMid 	= Float.parseFloat(fuh.getFormField(ExperimentUploadPresenter.FormField.CLAMP_MID.toString()));
		final float clampMax 	= Float.parseFloat(fuh.getFormField(ExperimentUploadPresenter.FormField.CLAMP_MAX.toString()));
		final String stress 	= fuh.getFormField(ExperimentUploadPresenter.FormField.STRESS.toString());
		final String treatment 	= fuh.getFormField(ExperimentUploadPresenter.FormField.TREATMENT.toString());
		final String control 	= fuh.getFormField(ExperimentUploadPresenter.FormField.CONTROL.toString());
		final String taxonomyId = fuh.getFormField(ExperimentUploadPresenter.FormField.TAXONOMY_ID.toString());
		final String units 		= fuh.getFormField(ExperimentUploadPresenter.FormField.UNITS.toString());
		final TargetType targetType = Sample.TargetType.fromString(fuh.getFormField(ExperimentUploadPresenter.FormField.TARGET_TYPE.toString()));

		// construct the sample
		Sample sample = new Sample(expId, sampleId, DataType.SESSION);
		sample.setClampValues(clampMin, clampMid, clampMax);
		sample.setStress(stress);
		sample.setTreatment(treatment, null);
		sample.setControl(control, null);
		sample.setUnits(units);
		sample.setTargetType(targetType);
		
		// construct the experiment
		Experiment experiment = new Experiment(expId);
		experiment.addSample(sample);
		
		// get the organism associated with this taxonomyId
		OrganismDAO organismDao = new OrganismDAOImpl(sm);
		Organism organism = organismDao.getOrganismForTaxonomyId(taxonomyId);

		// add the experiment to the session
		sm.addExperiment(experiment, organism);

		// add the id2Measurement map to the session
		sm.addMeasurements(expId, sampleId, id2Measurement);

		// handle response
		ResponseHandler.asHtml(response, fuh.getErrorMessages(), HttpServletResponse.SC_OK);

	}

}
