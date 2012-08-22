package gov.lbl.glamm.server.dao.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import com.fasterxml.jackson.databind.ObjectMapper;

import gov.lbl.glamm.client.model.Gene;
import gov.lbl.glamm.client.model.OverlayDataGroup;
import gov.lbl.glamm.client.model.Reaction;
import gov.lbl.glamm.server.GroupDataServiceManager;
import gov.lbl.glamm.server.GlammSession;
import gov.lbl.glamm.server.GroupDataService;
import gov.lbl.glamm.server.dao.GeneDAO;
import gov.lbl.glamm.server.dao.GroupDataDAO;
import gov.lbl.glamm.server.dao.ReactionDAO;

/**
 * Implementation of the Group Data DAO interface.
 * @author wjriehl
 *
 */
public class GroupDataDAOImpl implements GroupDataDAO {

	private GlammSession sm;
	public GroupDataDAOImpl(GlammSession sm) {
		this.sm = sm;
	}
	
	/**
	 * TODO: this is a stub for now.
	 */
	public Set<OverlayDataGroup> getGroupData(String text) {
		/**
		 * needs to:
		 * parse the text.
		 * craft some kind of web service call using text (later)
		 * invoke the service and get the results.
		 * package the results into an OverlayData
		 * return it.
		 */
		
		return new HashSet<OverlayDataGroup>();
	}
	
	/**
	 * Makes a dummy set of OverlayDataGroups and returns them.
	 * @return
	 */
	public Set<OverlayDataGroup> getGroupDataFromService(String s, Map<String, String> t) {
		Set<OverlayDataGroup> dataSet = new HashSet<OverlayDataGroup>();
		
		Set<String> ecNums = new HashSet<String>();
		ecNums.add("3.1.26.5");
		ecNums.add("2.7.7.7");
		ecNums.add("5.99.1.3");
		ecNums.add("6.1.1.14");
		ecNums.add("3.2.2.20");
		ecNums.add("2.8.1.-");
		ecNums.add("2.3.1.16");
		ecNums.add("4.2.1.17");
		ecNums.add("5.3.3.8");
		ecNums.add("3.4.13.9");
		ecNums.add("1.3.3.4");
		ecNums.add("2.1.1.-");
		ecNums.add("2.1.2.9");
		ecNums.add("3.5.1.88");
		ecNums.add("1.3.3.3");
		ecNums.add("1.1.1.25");
		ecNums.add("3.4.21.102");
		ecNums.add("5.4.2.1");
		ecNums.add("1.1.1.94");
		ecNums.add("2.4.1.-");
		ecNums.add("6.2.1.3");
		ecNums.add("4.1.1.44");
		ecNums.add("3.1.3.77");
		ecNums.add("5.3.2.-");
		ecNums.add("2.4.2.1");
		ecNums.add("3.5.2.7");
		ecNums.add("4.2.1.49");
		ecNums.add("4.3.1.3");
		ecNums.add("1.2.1.2");
		ecNums.add("2.9.1.1");
		ecNums.add("3.4.24.-");
		ecNums.add("4.1.3.40");
		ecNums.add("2.1.1.48");
		ecNums.add("1.16.3.1");
		ecNums.add("4.1.99.12");
		ecNums.add("3.4.21.83");
		ecNums.add("4.1.1.49");
		ecNums.add("3.1.3.7");
		ecNums.add("2.9.1.-");
		ecNums.add("2.7.9.3");
		ecNums.add("1.14.19.1");
		ecNums.add("2.1.1.35");
		ecNums.add("1.1.1.158");
		ecNums.add("6.3.4.15");
		ecNums.add("2.7.1.33");
		ecNums.add("3.6.5.3");
		ecNums.add("2.7.7.6");
		ecNums.add("3.6.3.41");
		ecNums.add("4.1.1.31");
		ecNums.add("1.2.1.38");
		ecNums.add("2.7.2.8");
		ecNums.add("2.1.3.3");
		ecNums.add("6.3.4.5");
		ecNums.add("4.3.2.1");
		ecNums.add("3.4.-.-");
		ecNums.add("2.7.1.71");
		ecNums.add("4.2.3.4");
		ecNums.add("2.1.1.72");
		ecNums.add("5.1.3.1");
		ecNums.add("3.1.3.18");
		ecNums.add("6.1.1.2");
		ecNums.add("2.6.1.42");
		ecNums.add("4.2.1.3");
		ecNums.add("4.2.1.99");
		ecNums.add("2.3.3.5");
		ecNums.add("4.1.3.30");
		ecNums.add("2.7.13.3");
		ecNums.add("2.7.6.5");
		ecNums.add("2.7.4.8");
		ecNums.add("3.1.21.3");
		ecNums.add("1.-.-.-");
		ecNums.add("1.3.99.1");
		ecNums.add("1.6.5.5");
		ecNums.add("3.6.1.-");
		ecNums.add("2.7.1.24");
		ecNums.add("3.4.23.43");
		ecNums.add("1.2.4.1");
		ecNums.add("2.3.1.12");
		ecNums.add("1.8.1.4");
		ecNums.add("4.1.1.37");
		ecNums.add("6.3.4.13");
		ecNums.add("2.1.2.3");
		ecNums.add("3.5.4.10");
		ecNums.add("1.8.1.8");
		ecNums.add("5.2.1.8");
		ecNums.add("2.5.1.-");
		ecNums.add("1.16.1.3");
		ecNums.add("4.1.1.-");
		ecNums.add("4.2.1.10");
		ecNums.add("1.2.1.12");
		ecNums.add("6.3.4.3");
		ecNums.add("2.3.1.51");
		ecNums.add("2.7.11.1");
		ecNums.add("2.1.1.67");
		ecNums.add("3.1.4.46");
		ecNums.add("3.1.-.-");
		ecNums.add("3.5.1.28");
		ecNums.add("2.5.1.75");
		ecNums.add("2.5.1.8");
		ecNums.add("1.10.2.2");
		ecNums.add("2.6.1.11");
		ecNums.add("2.6.1.81");
		ecNums.add("2.3.1.109");
		ecNums.add("6.5.1.-");
		ecNums.add("3.4.24.3");
		ecNums.add("3.2.1.17");
		ecNums.add("5.1.3.3");
		ecNums.add("2.7.1.6");
		ecNums.add("1.11.1.6");
		ecNums.add("1.11.1.7");
		ecNums.add("2.3.2.2");
		ecNums.add("2.5.1.18");
		ecNums.add("1.18.1.2");
		ecNums.add("2.5.1.54");
		ecNums.add("1.1.1.37");
		ecNums.add("6.3.3.2");
		ecNums.add("1.14.13.-");
		ecNums.add("2.1.2.10");
		ecNums.add("1.4.4.2");
		ecNums.add("3.1.1.-");
		ecNums.add("3.1.3.1");
		ecNums.add("2.7.1.15");
		ecNums.add("2.1.1.14");
		ecNums.add("2.1.1.52");
		ecNums.add("6.3.2.3");
		ecNums.add("3.1.21.1");
		ecNums.add("3.5.2.6");
		ecNums.add("6.3.4.14");
		ecNums.add("1.7.99.4");
		ecNums.add("1.1.1.95");
		ecNums.add("6.3.2.1");
		ecNums.add("2.1.2.11");
		ecNums.add("2.7.6.3");
		ecNums.add("2.7.7.19");
		ecNums.add("2.7.1.19");
		ecNums.add("3.5.3.12");
		ecNums.add("1.6.5.-");
		ecNums.add("2.3.1.-");
		ecNums.add("2.5.1.6");
		ecNums.add("2.2.1.1");
		ecNums.add("2.7.2.3");
		ecNums.add("4.1.2.13");
		ecNums.add("5.3.4.1");
		ecNums.add("1.6.4.-");
		ecNums.add("1.11.1.15");
		ecNums.add("3.4.11.1");
		ecNums.add("1.1.1.28");
		ecNums.add("1.1.5.3");
		ecNums.add("6.1.1.6");
		ecNums.add("3.1.1.45");
		ecNums.add("1.6.5.3");
		ecNums.add("2.1.1.13");
		ecNums.add("2.4.2.21");
		ecNums.add("2.7.8.26");
		ecNums.add("2.7.1.156");
		ecNums.add("2.7.7.62");
		ecNums.add("6.3.5.10");
		ecNums.add("2.5.1.17");
		ecNums.add("3.4.11.2");
		ecNums.add("2.5.1.49");
		ecNums.add("1.14.11.-");
		ecNums.add("4.4.1.21");
		ecNums.add("3.4.13.3");
		ecNums.add("2.7.2.11");
		ecNums.add("1.2.1.41");
		ecNums.add("1.3.1.26");
		ecNums.add("6.3.5.5");
		ecNums.add("5.3.1.6");
		ecNums.add("2.3.1.128");
		ecNums.add("2.8.1.8");
		ecNums.add("2.3.1.181");
		ecNums.add("3.4.16.4");
		ecNums.add("2.4.-.-");
		ecNums.add("3.2.1.-");
		ecNums.add("2.1.-.-");
		ecNums.add("2.7.7.18");
		ecNums.add("6.1.1.4");
		ecNums.add("3.1.1.29");
		ecNums.add("2.5.1.15");
		ecNums.add("5.4.2.10");
		ecNums.add("5.3.1.1");
		ecNums.add("2.7.7.8");
		ecNums.add("3.1.21.-");
		ecNums.add("4.1.2.4");
		ecNums.add("2.4.2.2");
		ecNums.add("2.4.2.4");
		ecNums.add("5.4.2.7");
		ecNums.add("3.1.3.3");
		ecNums.add("1.7.2.3");
		ecNums.add("6.3.1.2");
		ecNums.add("6.3.4.4");
		ecNums.add("1.4.3.-");
		ecNums.add("1.2.1.16");
		ecNums.add("2.6.1.19");
		ecNums.add("2.7.7.-");
		ecNums.add("3.4.24.57");
		ecNums.add("2.3.1.15");
		ecNums.add("4.1.2.25");
		ecNums.add("3.6.1.27");
		ecNums.add("2.1.1.51");
		ecNums.add("5.4.3.8");
		ecNums.add("2.1.3.2");
		ecNums.add("2.7.1.-");
		ecNums.add("6.1.1.1");
		ecNums.add("6.3.1.10");
		ecNums.add("3.2.2.16");
		ecNums.add("3.2.2.9");
		ecNums.add("1.4.1.13");
		ecNums.add("4.6.1.1");
		ecNums.add("2.7.3.9");
		ecNums.add("2.4.99.-");
		ecNums.add("2.1.1.45");
		ecNums.add("1.4.3.16");
		ecNums.add("3.4.21.89");
		ecNums.add("3.1.26.3");
		ecNums.add("2.6.99.2");
		ecNums.add("2.7.8.7");
		ecNums.add("2.1.1.31");
		ecNums.add("5.4.99.5");
		ecNums.add("1.7.-.-");
		ecNums.add("3.4.17.19");
		ecNums.add("3.2.-.-");
		ecNums.add("3.4.11.9");
		ecNums.add("3.5.4.1");
		ecNums.add("2.3.3.9");
		ecNums.add("4.1.3.1");
		ecNums.add("1.1.1.1");
		ecNums.add("2.4.1.25");
		ecNums.add("2.4.1.18");
		ecNums.add("2.4.1.1");
		ecNums.add("2.7.7.27");
		ecNums.add("2.4.1.21");
		ecNums.add("2.2.1.7");
		ecNums.add("2.5.1.10");
		ecNums.add("2.7.3.-");
		ecNums.add("1.11.1.9");
		ecNums.add("3.11.1.2");
		ecNums.add("1.7.1.13");
		ecNums.add("2.7.1.69");
		ecNums.add("3.5.1.10");
		ecNums.add("2.3.1.117");
		ecNums.add("2.7.7.59");
		ecNums.add("3.4.11.18");
		ecNums.add("2.7.4.22");
		ecNums.add("2.5.1.31");
		ecNums.add("2.7.7.41");
		ecNums.add("1.1.1.267");
		ecNums.add("4.2.1.-");
		ecNums.add("2.3.1.129");
		ecNums.add("2.4.1.182");
		ecNums.add("3.1.26.4");
		ecNums.add("6.3.4.-");
		ecNums.add("1.1.1.133");
		ecNums.add("2.7.1.4");
		ecNums.add("5.1.3.2");
		ecNums.add("2.7.7.9");
		ecNums.add("1.14.16.1");
		ecNums.add("4.2.1.96");
		ecNums.add("5.2.1.2");
		ecNums.add("2.3.1.46");
		ecNums.add("2.3.1.9");
		ecNums.add("1.2.1.27");
		ecNums.add("1.3.99.-");
		ecNums.add("1.1.1.31");
		ecNums.add("3.6.3.4");
		ecNums.add("3.6.3.27");
		ecNums.add("2.3.1.180");
		ecNums.add("5.4.2.8");
		ecNums.add("2.7.1.31");
		ecNums.add("6.1.1.18");
		ecNums.add("6.1.1.16");
		ecNums.add("1.5.1.5");
		ecNums.add("3.5.4.9");
		ecNums.add("3.4.21.92");
		ecNums.add("3.4.21.53");
		ecNums.add("1.3.1.44");
		ecNums.add("4.4.1.11");
		ecNums.add("4.2.1.60");
		ecNums.add("3.1.25.-");
		ecNums.add("2.7.8.5");
		ecNums.add("4.1.1.19");
		ecNums.add("4.1.1.50");
		ecNums.add("4.2.1.52");
		ecNums.add("2.8.3.5");
		ecNums.add("4.1.3.4");
		ecNums.add("1.3.99.10");
		ecNums.add("1.1.1.44");
		ecNums.add("3.1.2.-");
		ecNums.add("2.3.3.1");
		ecNums.add("1.2.4.2");
		ecNums.add("2.3.1.61");
		ecNums.add("6.2.1.5");
		ecNums.add("3.5.1.-");
		ecNums.add("2.3.1.79");
		ecNums.add("1.13.11.27");
		ecNums.add("1.13.11.5");
		ecNums.add("2.7.6.1");
		ecNums.add("3.1.1.1");
		ecNums.add("3.1.3.5");
		ecNums.add("2.4.2.7");
		ecNums.add("2.7.4.3");
		ecNums.add("4.99.1.1");
		ecNums.add("2.7.1.73");
		ecNums.add("6.3.1.5");
		ecNums.add("4.4.1.5");
		ecNums.add("3.4.21.26");
		ecNums.add("1.1.1.");
		ecNums.add("1.1.1.284");
		ecNums.add("3.1.2.12");
		ecNums.add("3.5.4.19");
		ecNums.add("3.6.1.31");
		ecNums.add("4.1.3.-");
		ecNums.add("5.3.1.16");
		ecNums.add("2.4.2.-");
		ecNums.add("3.1.3.15");
		ecNums.add("4.2.1.19");
		ecNums.add("2.6.1.9");
		ecNums.add("1.1.1.23");
		ecNums.add("2.4.2.17");
		ecNums.add("6.1.1.20");
		ecNums.add("3.4.19.5");
		ecNums.add("2.1.1.80");
		ecNums.add("3.5.1.44");
		ecNums.add("3.1.1.61");
		ecNums.add("1.2.1.10");
		ecNums.add("3.1.11.5");
		ecNums.add("3.6.3.-");
		ecNums.add("1.11.1.5");
		ecNums.add("1.1.1.42");
		ecNums.add("3.6.1.11");
		ecNums.add("4.4.1.8");
		ecNums.add("2.7.1.107");
		ecNums.add("6.5.1.1");
		ecNums.add("3.1.3.48");
		ecNums.add("3.2.1.20");
		ecNums.add("6.3.2.4");
		ecNums.add("6.1.1.22");
		ecNums.add("2.6.1.85");
		ecNums.add("4.2.1.2");
		ecNums.add("5.1.1.1");
		ecNums.add("4.3.1.17");
		ecNums.add("3.2.1.52");
		ecNums.add("3.6.1.7");
		ecNums.add("3.1.3.25");
		ecNums.add("2.3.1.30");
		ecNums.add("2.8.1.7");
		ecNums.add("2.7.4.6");
		ecNums.add("2.2.1.6");
		ecNums.add("2.5.1.9");
		ecNums.add("6.1.1.3");
		ecNums.add("1.8.1.9");
		ecNums.add("6.1.1.11");
		ecNums.add("5.4.2.2");
		ecNums.add("1.8.4.11");
		ecNums.add("3.5.1.96");
		ecNums.add("1.2.4.4");
		ecNums.add("2.3.1.168");
		ecNums.add("2.5.1.72");
		ecNums.add("2.6.1.57");
		ecNums.add("3.6.3.3");
		ecNums.add("1.9.3.1");
		ecNums.add("2.8.-.-");
		ecNums.add("2.7.8.8");
		ecNums.add("3.4.99.-");
		ecNums.add("4.1.1.23");
		ecNums.add("2.7.4.14");
		ecNums.add("2.5.1.19");
		ecNums.add("1.97.1.4");
		ecNums.add("2.6.1.52");
		ecNums.add("2.1.1.64");
		ecNums.add("1.17.4.1");
		ecNums.add("1.3.1.34");
		ecNums.add("3.4.21.-");
		ecNums.add("3.5.1.1");
		ecNums.add("3.1.22.4");
		ecNums.add("6.1.1.12");
		ecNums.add("2.5.1.3");
		ecNums.add("3.2.1.3");
		ecNums.add("3.5.1.18");
		ecNums.add("4.2.1.1");
		ecNums.add("2.7.7.38");
		ecNums.add("3.1.5.1");
		ecNums.add("4.1.2.14");
		ecNums.add("4.1.3.16");
		ecNums.add("4.2.1.12");
		ecNums.add("3.1.1.31");
		ecNums.add("1.1.1.49");
		ecNums.add("2.7.1.40");
		ecNums.add("3.4.15.1");
		ecNums.add("4.2.99.18");
		ecNums.add("3.1.3.8");
		ecNums.add("3.1.2.6");
		ecNums.add("3.1.13.5");
		ecNums.add("4.2.1.24");
		ecNums.add("1.3.3.1");
		ecNums.add("2.7.4.9");
		ecNums.add("4.1.3.38");
		ecNums.add("3.5.4.13");
		ecNums.add("2.7.1.48");
		ecNums.add("3.6.3.21");
		ecNums.add("3.6.3.31");
		ecNums.add("3.5.1.53");
		ecNums.add("2.7.1.66");
		ecNums.add("1.3.1.12");
		ecNums.add("4.2.1.51");
		ecNums.add("1.1.1.41");
		ecNums.add("6.4.1.3");
		ecNums.add("6.3.5.1");
		ecNums.add("1.12.5.1");
		ecNums.add("1.8.4.6");
		ecNums.add("2.6.1.1");
		ecNums.add("1.8.99.3");
		ecNums.add("2.7.4.7");
		ecNums.add("4.1.1.65");
		ecNums.add("1.17.4.3");
		ecNums.add("1.11.1.-");
		ecNums.add("2.1.2.-");
		ecNums.add("1.6.1.1");
		ecNums.add("1.5.99.8");
		ecNums.add("1.12.7.2");
		ecNums.add("1.7.99.5");
		ecNums.add("2.5.1.64");
		ecNums.add("4.1.1.71");
		ecNums.add("2.7.7.25");
		ecNums.add("1.3.99.2");
		ecNums.add("1.1.1.178");
		ecNums.add("2.3.3.13");
		ecNums.add("2.3.1.1");
		ecNums.add("2.3.1.157");
		ecNums.add("2.3.1.86");
		ecNums.add("2.3.1.29");
		ecNums.add("2.7.1.25");
		ecNums.add("2.7.7.4");
		ecNums.add("2.1.1.107");
		ecNums.add("1.8.4.8");
		ecNums.add("1.8.1.2");
		ecNums.add("1.6.1.2");
		ecNums.add("2.7.7.42");
		ecNums.add("2.5.1.16");
		ecNums.add("1.5.1.12");
		ecNums.add("4.4.1.16");
		ecNums.add("2.4.2.8");
		ecNums.add("6.3.2.-");
		ecNums.add("1.1.1.169");
		ecNums.add("2.5.1.55");
		ecNums.add("1.2.1.70");
		ecNums.add("2.7.1.148");
		ecNums.add("1.1.1.38");
		ecNums.add("3.6.3.29");
		ecNums.add("5.99.1.-");
		ecNums.add("3.6.1.13");
		ecNums.add("2.5.1.7");
		ecNums.add("5.3.1.13");
		ecNums.add("3.1.3.45");
		ecNums.add("1.7.2.2");
		ecNums.add("3.4.14.5");
		ecNums.add("3.1.3.11");
		ecNums.add("1.5.1.20");
		ecNums.add("2.5.1.48");
		ecNums.add("6.3.2.6");
		ecNums.add("1.3.1.74");
		ecNums.add("3.1.26.-");
		ecNums.add("1.1.1.40");
		ecNums.add("6.1.1.19");
		ecNums.add("2.4.2.3");
		ecNums.add("4.1.1.17");
		ecNums.add("3.4.25.-");
		ecNums.add("3.6.1.1");
		ecNums.add("6.3.2.8");
		ecNums.add("2.4.1.227");
		ecNums.add("6.3.2.9");
		ecNums.add("2.7.8.13");
		ecNums.add("6.3.2.10");
		ecNums.add("6.3.2.13");
		ecNums.add("2.4.1.129");
		ecNums.add("2.7.1.30");
		ecNums.add("4.2.1.33");
		ecNums.add("4.2.1.35");
		ecNums.add("1.1.1.85");
		ecNums.add("4.1.1.36");
		ecNums.add("6.3.2.5");
		ecNums.add("3.6.1.23");
		ecNums.add("3.5.4.16");
		ecNums.add("2.4.2.10");
		ecNums.add("2.7.7.56");
		ecNums.add("1.6.99.-");
		ecNums.add("5.1.1.7");
		ecNums.add("4.1.1.20");
		ecNums.add("2.5.1.61");
		ecNums.add("4.2.1.75");
		ecNums.add("4.3.1.19");
		ecNums.add("4.2.1.9");
		ecNums.add("1.1.1.86");
		ecNums.add("3.4.11.5");
		ecNums.add("1.5.5.1");
		ecNums.add("1.2.1.3");
		ecNums.add("1.3.1.-");
		ecNums.add("2.2.1.9");
		ecNums.add("4.2.99.20");
		ecNums.add("4.2.1.113");
		ecNums.add("6.2.1.26");
		ecNums.add("3.4.21.88");
		ecNums.add("2.8.1.1");
		ecNums.add("1.1.1.103");
		ecNums.add("2.-.-.-");
		ecNums.add("2.7.7.3");
		ecNums.add("5.1.3.-");
		ecNums.add("1.1.1.22");
		ecNums.add("3.4.24.70");
		ecNums.add("1.8.1.7");
		ecNums.add("5.4.4.2");
		ecNums.add("3.2.2.23");
		ecNums.add("3.5.4.4");
		ecNums.add("3.1.1.5");
		ecNums.add("4.1.3.36");
		ecNums.add("2.7.7.23");
		ecNums.add("5.1.1.3");
		ecNums.add("4.2.3.5");
		ecNums.add("2.7.1.23");
		ecNums.add("3.5.1.16");
		ecNums.add("1.3.99.12");
		ecNums.add("3.7.1.2");
		ecNums.add("3.5.1.94");
		ecNums.add("3.5.3.8");
		ecNums.add("3.1.7.2");
		ecNums.add("3.6.3.30");
		ecNums.add("3.2.2.1");
		ecNums.add("1.1.99.5");
		ecNums.add("6.3.5.3");
		ecNums.add("6.3.5.2");
		ecNums.add("1.1.1.205");
		ecNums.add("3.1.11.6");
		ecNums.add("6.1.1.21");
		ecNums.add("1.17.7.1");
		ecNums.add("4.1.2.5");
		ecNums.add("1.5.1.2");
		ecNums.add("3.6.1.15");
		ecNums.add("1.3.99.22");
		ecNums.add("3.5.1.2");
		ecNums.add("2.1.1.33");
		ecNums.add("3.2.2.-");
		ecNums.add("2.1.1.79");
		ecNums.add("4.1.99.3");
		ecNums.add("4.2.3.1");
		ecNums.add("2.7.1.39");
		ecNums.add("2.7.2.4");
		ecNums.add("6.1.1.9");
		ecNums.add("6.1.1.7");
		ecNums.add("2.1.1.77");
		ecNums.add("3.1.3.6");
		ecNums.add("5.4.99.12");
		ecNums.add("4.6.1.12");
		ecNums.add("2.7.7.60");
		ecNums.add("4.2.1.11");
		ecNums.add("6.3.4.2");
		ecNums.add("3.1.3.27");
		ecNums.add("2.7.4.16");
		ecNums.add("6.3.3.-");
		ecNums.add("1.1.1.193");
		ecNums.add("3.5.4.26");
		ecNums.add("2.1.2.1");
		ecNums.add("2.6.1.18");
		ecNums.add("3.5.1.25");
		ecNums.add("2.6.1.16");
		ecNums.add("1.6.99.3");
		ecNums.add("1.17.1.2");
		ecNums.add("3.4.23.36");
		ecNums.add("6.1.1.5");
		ecNums.add("3.2.2.1");
		ecNums.add("1.1.1.100");
		ecNums.add("2.3.1.54");
		ecNums.add("2.3.1.8");
		ecNums.add("2.3.1.47");
		ecNums.add("2.3.1.179");
		ecNums.add("2.3.1.39");
		ecNums.add("2.3.1.41");

	
		Set<String> ecNums2 = new HashSet<String>();
		ecNums2.add("6.1.1.10");
		ecNums2.add("2.3.2.6");
		ecNums2.add("2.3.2.8");
		ecNums2.add("2.1.1.61");
		ecNums2.add("4.3.2.2");
		ecNums2.add("2.7.9.2");
		ecNums2.add("5.99.1.2");
		ecNums2.add("3.5.3.23");
		ecNums2.add("3.4.13.21");
		ecNums2.add("6.3.3.3");
		ecNums2.add("2.8.1.6");
		ecNums2.add("2.6.1.62");
		ecNums2.add("6.2.1.1");
		ecNums2.add("3.1.13.-");
		ecNums2.add("2.4.2.9");
		ecNums2.add("6.3.3.1");
		ecNums2.add("2.1.2.2");
		ecNums2.add("6.3.5.4");
		ecNums2.add("3.1.26.12");
		ecNums2.add("3.1.4.-");
		ecNums2.add("3.1.11.1");
		ecNums2.add("3.5.4.5");
		ecNums2.add("2.7.1.130");
		ecNums2.add("3.5.4.25");
		ecNums2.add("1.17.4.2");
		ecNums2.add("1.20.4.1");
		ecNums2.add("1.15.1.1");
		ecNums2.add("1.4.3.5");
		ecNums2.add("6.5.1.2");
		ecNums2.add("2.5.1.47");
		ecNums2.add("2.7.2.1");
		ecNums2.add("4.1.3.27");
		ecNums2.add("2.4.2.18");
		ecNums2.add("4.1.1.48");
		ecNums2.add("4.2.1.20");
		ecNums2.add("3.1.11.2");
		ecNums2.add("2.4.2.14");
		ecNums2.add("6.3.2.12");
		ecNums2.add("6.3.2.17");
		ecNums2.add("1.2.1.11");
		ecNums2.add("3.1.3.-");
		ecNums2.add("1.1.1.3");
		ecNums2.add("2.4.2.29");
		ecNums2.add("5.-.-.-");
		ecNums2.add("2.1.1.63");
		ecNums2.add("3.2.2.21");
		ecNums2.add("2.7.1.21");
		ecNums2.add("3.4.15.5");
		ecNums2.add("6.1.1.15");
		ecNums2.add("5.1.3.13");
		ecNums2.add("4.2.1.46");
		ecNums2.add("2.7.7.24");
		ecNums2.add("3.6.3.14");
		ecNums2.add("2.6.1.-");
		ecNums2.add("1.10.3.-");

		
		Set<String> ecNums3 = new HashSet<String>();
		ecNums3.add("2.7.1.26");
		ecNums3.add("2.7.7.2");
		ecNums3.add("2.2.1.2");
		ecNums3.add("5.3.1.9");
		ecNums3.add("4.1.1.21");
		ecNums3.add("5.4.99.18");
		ecNums3.add("6.3.4.18");
		ecNums3.add("6.3.2.2");
		ecNums3.add("3.1.4.16");
		ecNums3.add("3.6.3.25");
		ecNums3.add("1.1.1.262");
		ecNums3.add("3.6.1.41");
		ecNums3.add("1.5.1.3");
		ecNums3.add("3.6.3.34");
		ecNums3.add("3.5.2.3");


//		
//		Set<String> ecNums = new HashSet<String>();
//		ecNums.add("1.2.3.1");
//		ecNums.add("1.2.3.11");
//		ecNums.add("2.7.7.6");
//		ecNums.add("1.2.3.4");
//		ecNums.add("5.5.1.1");
//		String name1 = "test 1";
//		
//		Set<String> ecNums2 = new HashSet<String>();
//		ecNums2.add("2.7.7.7");
//		ecNums2.add("6.1.1.17");
//		ecNums2.add("2.8.1.2");
//		ecNums2.add("2.3.3.1");
//		String name2 = "test 2";
//
		
		
		
		ReactionDAO reactionDao = new ReactionGlammDAOImpl(sm);
		Set<Reaction> rxns = reactionDao.getReactionsForEcNums(ecNums);
		OverlayDataGroup group1 = new OverlayDataGroup("1", "Annotation", "http://glamm.lbl.gov", "SEED");

		for (Reaction rxn : rxns) {
			group1.addElement(rxn);
		}
		
		OverlayDataGroup group2 = new OverlayDataGroup("2", "GapFill", "http://regprecise.lbl.gov/", "GapFill");
		Set<Reaction> rxns2 = reactionDao.getReactionsForEcNums(ecNums2);
		for (Reaction rxn : rxns2) {
			group2.addElement(rxn);
		}
		
		OverlayDataGroup group3 = new OverlayDataGroup("3", "GrowMatch", "http://regprecise.lbl.gov/", "GrowMatch");
		Set<Reaction> rxns3 = reactionDao.getReactionsForEcNums(ecNums3);
		for (Reaction rxn : rxns3) {
			group3.addElement(rxn);
		}

		dataSet.add(group1);
		dataSet.add(group2);
		dataSet.add(group3);
		return dataSet;
	}

	/**
	 * Gets group data from a data service.
	 * TODO:
	 * This is still a horrible, yet functional, prototype. It should ultimately:
	 * 	- call the service with the right parameters.
	 *  - craft a proper error (or throw a proper exception) if necessary
	 *  - invoke the DataService's parser to properly construct the DataGroups
	 * 
	 * Right now, well, one of out three ain't bad. The parser is wrapped up in the code below, and this only works for
	 * Pavel's RegPrecise service.
	 * 
	 * @param serviceName the name of the service to invoke.
	 * @param parameters the parameters for the service, where the key is the parameter name and the value is its value.
	 * @return a Set of OverlayDataGroups from the service
	 */
//	@Override
	public Set<OverlayDataGroup> getGroupDataFromService2(String serviceName, Map<String, String> parameters) {
		
		Set<OverlayDataGroup> dataSet = new HashSet<OverlayDataGroup>();

		GroupDataService service = GroupDataServiceManager.getServiceFromName(serviceName);
		
		if (service == null)
			return dataSet;
		
		String uri = service.getUrl();
		uri += buildParameterString(service, parameters);

		// Horrible hack for prototyping this up for now.
		// Later, should make a separate parser for each service, and invoke through reflection.
		String taxId = "";
		if (parameters.containsKey("MO taxonomy id"))
			taxId = parameters.get("MO taxonomy id");
		
		try {
			URL url = new URL(uri);
			
			ObjectMapper mapper = new ObjectMapper();
			
			InputStream stream = url.openStream();
			ElementSet dataElements = mapper.readValue(stream, ElementSet.class);
			stream.close();
			
/** Maybe this structure would work better?
 * 			{
				[
					{ 
					  "groupName" : "name",
					  "groupId"   : "id",
					  "URL"		  : "url",
					  "source"    : "source"
					  [ 
					  	{ 
					  	  "ec"    : "##",
					  	  "vimss  : "##"
					  	},
					  	{ 
					  	  "ec"    : "##",
					  	  "vimss" : "##"
					  	}, ...
					  ]
					},
					{ 
					  "groupName" : "name",
					  "groupId"   : "id",
					  "URL"		  : "url",
					  "source"    : "source"
					  [ 
					  	{ 
					  	  "ec"    : "##",
					  	  "vimss  : "##"
					  	},
					  	{ 
					  	  "ec"    : "##",
					  	  "vimss" : "##"
					  	}, ...
					  ]
					}					
				]
			}
**/			

			/**
			 *  Shuffle around elements and restructure into a Set<OverlayDataGroup>
			 *  
			 *  OverlayData works on a set of reactions or compounds.
			 *  In this case, we're dealing with reactions (not always, though).
			 *  So we want to go from either VIMSS ids (if that's the only option) to reactions
			 *  Or form EC nums to reactions.
			 *  
			 *  First, break things up into sets.
			 *  	group id -> set of VIMSS ids
			 *      group id -> set of EC nums
			 *  
			 *  Look up the Genes for the VIMSS ids in each group. Now we should have JUST
			 *      group id -> set of EC nums
			 *  
			 *  Look up the Reactions for the EC nums.
			 *  
			 *  Now make one OverlayDataGroup for each group id/name, populate with reactions.
			 */

			// Map of all data groups by their group id.
			Map<String, OverlayDataGroup> id2DataGroup = new HashMap<String, OverlayDataGroup>();
			
			Map<String, Set<String>> locus2GroupId = new HashMap<String, Set<String>>();
			
			Map<String, Set<String>> ecNum2GroupId = new HashMap<String, Set<String>>();
			
			Map<String, Set<Gene>> ecNum2Genes = new HashMap<String, Set<Gene>>();
			
			
			/** Initial setup - parse info that was downloaded **/
			for (Element elem : dataElements.getGlammElement()) {
				
				// Check the groupId for format (if no group Id, move on to the next Element)
				String groupId = elem.getGroupId();
				if (groupId == null || groupId.isEmpty())
					continue;

				String ecNum = elem.getEcNumber();
				String locusId = elem.getVimssId();
				
				// If there's no OverlayDataGroup yet, make one.
				if (!id2DataGroup.containsKey(groupId))
					id2DataGroup.put(groupId, new OverlayDataGroup(groupId, elem.getGroupName(), elem.getCallbackURL(), service.getName()));
				
				// Put the EC num in that group.
				if (ecNum != null && !ecNum.isEmpty()) {
					if (!ecNum2GroupId.containsKey(ecNum))
						ecNum2GroupId.put(ecNum, new HashSet<String>());
					ecNum2GroupId.get(ecNum).add(groupId);
				}
				
				// Put the locusId in that group.
				if (locusId != null && !locusId.isEmpty()) {
					if (!locus2GroupId.containsKey(locusId))
						locus2GroupId.put(locusId, new HashSet<String>());
					locus2GroupId.get(locusId).add(groupId);
				}
				
			}

			// Get all genes from VIMSS locus IDs.
			GeneDAO geneDao = new GeneDAOImpl(sm);
			Set<Gene> genes = geneDao.getGenesForVimssIds(taxId, locus2GroupId.keySet());
			
			// Shuffle them around to be in the right groups.
			for (Gene gene : genes) {
				if (!locus2GroupId.containsKey(gene.getVimssId()))
					continue;
				
				for (String groupId : locus2GroupId.get(gene.getVimssId())) {
					id2DataGroup.get(groupId).addElement(gene);
				}
				
				for (String ecNum : gene.getEcNums()) {
					if (!ecNum2GroupId.containsKey(ecNum))
						ecNum2GroupId.put(ecNum, new HashSet<String>());
					ecNum2GroupId.get(ecNum).addAll(locus2GroupId.get(gene.getVimssId()));
					
					if (!ecNum2Genes.containsKey(ecNum))
						ecNum2Genes.put(ecNum, new HashSet<Gene>());
					ecNum2Genes.get(ecNum).add(gene);
				}
			}
			
			// NOW we need gene->EC
			// get all ECs.
			// get all Reactions for those ECs.
			// now we can map (for all genes), which reactions they belong to (reaction -> EC set, EC -> gene set, gene -> Group)
			
			ReactionDAO rxnDao = new ReactionGlammDAOImpl(sm);
			Set<Reaction> reactions = rxnDao.getReactions(rxnDao.getRxnIdsForEcNums(ecNum2GroupId.keySet()));

			for (Reaction rxn : reactions) {
				for (String ecNum : rxn.getEcNums()) {
					Set<Gene> geneSet = ecNum2Genes.get(ecNum);
					if (geneSet != null && !geneSet.isEmpty()) {
						for (Gene g : geneSet) {
							rxn.addGene(g);
						}
					}
					
					Set<String> groupIds = ecNum2GroupId.get(ecNum);
					if (groupIds == null || groupIds.isEmpty())
						continue;
					
					for (String id : groupIds) {
						id2DataGroup.get(id).addReaction(rxn);
					}
				}
			}
			
			dataSet.addAll(id2DataGroup.values());
			
		} catch (MalformedURLException e) {
			System.out.println(e.getLocalizedMessage());
		} catch (IOException e) {
			System.out.println(e.getLocalizedMessage());
		}
		return dataSet;
	}
	
	/**
	 * 
	 * @param service
	 * @param parameters
	 * @return
	 */
	private String buildParameterString(GroupDataService service, Map<String, String> parameters) {
		if (parameters == null || parameters.size() == 0)
			return "";

		String paramString = "?";
		List<String> pairs = new ArrayList<String>();
		for (String p : parameters.keySet()) {
			String urlParamName = service.getUrlParamName(p);
			if (urlParamName != null)
				pairs.add(urlParamName + "=" + parameters.get(p));
		}
		
		paramString += pairs.get(0);
		for (int i=1; i<pairs.size(); i++) {
			paramString += "&" + pairs.get(i);
		}
		return paramString;
	}
}

/**
 * A simple class used by Jackson for unmarshalling the JSON-encoded results from RegPrecise
 */
class ElementSet {
	private Collection<Element> glammElement;
	
	public ElementSet() { }
	
	public void setGlammElement(Collection<Element> glammElement) {
		this.glammElement = glammElement;
	}
	
	public Collection<Element> getGlammElement() { return glammElement; }
}		

/**
 * A simple class used by Jackson for unmarshalling the JSON-encoded results from RegPrecise
 */
class Element {
	private String vimssId;
	private String ecNumber;
	private String groupId;
	private String groupName;
	private String callbackURL;

	public Element() {
		vimssId = "";
		ecNumber = "";
		groupId = "";
		groupName = "";
		callbackURL = "";
	}
	
	public void setVimssId(String vimssId) {
		this.vimssId = vimssId;
	}
	
	public void setEcNumber(String ecNumber) {
		this.ecNumber = ecNumber;
	}
	
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	
	public void setCallbackURL(String callbackURL) {
		this.callbackURL = callbackURL;
	}
	
	public String getVimssId() { return vimssId; }
	
	public String getEcNumber() { return ecNumber; }
	
	public String getGroupId() { return groupId; }
	
	public String getGroupName() { return groupName; }
	
	public String getCallbackURL() { return callbackURL; }
}