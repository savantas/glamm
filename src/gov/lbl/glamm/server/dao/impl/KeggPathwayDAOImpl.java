package gov.lbl.glamm.server.dao.impl;

import gov.lbl.glamm.client.model.Organism;
import gov.lbl.glamm.client.model.Pathway;
import gov.lbl.glamm.client.model.Reaction;
import gov.lbl.glamm.server.GlammSession;
import gov.lbl.glamm.server.actions.GetReactions;
import gov.lbl.glamm.server.dao.PathwayDAO;
import gov.lbl.glammdb.domain.PersistentPathway;
import gov.lbl.glammdb.domain.PersistentPwyElement;
import gov.lbl.glammdb.util.HibernateUtil;

import java.util.HashSet;
import java.util.Set;

import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

/**
 * Implementation of the Pathway DAO allowing access to KEGG pathways stored in the GLAMM database.
 * @author jtbates
 *
 */
public class KeggPathwayDAOImpl implements PathwayDAO {
	
	private GlammSession sm;
	
	/**
	 * Constructor.
	 * @param sm The GLAMM Session.
	 */
	public KeggPathwayDAOImpl(final GlammSession sm) {
		this.sm = sm;
	}

	@Override
	public Pathway getPathway(String mapId) {
		return getPathway(mapId, null);

	}
	
	@Override
	public Pathway getPathway(String mapId, final Organism organism) {
		Pathway pathway = null;
		
		try {
			Session session = HibernateUtil.getSessionFactory(sm).getCurrentSession();
			session.beginTransaction();
			PersistentPathway result = (PersistentPathway) session.createCriteria(PersistentPathway.class)
																  .add(Restrictions.eq("mapId", mapId))
																  .uniqueResult();
			session.getTransaction().commit();

			if(result != null)
			{
				pathway = new Pathway();

				pathway.setName(result.getTitle());
				pathway.setMapId(mapId);
				
				Set<String> rxnIds = new HashSet<String>();
				for(PersistentPwyElement element : result.getElements()) {
					if(element.getType() == PersistentPwyElement.Type.RXN)
						rxnIds.add(element.getXrefId());
				}
				
				Set<Reaction> mapReactions = GetReactions.getReactions(sm, rxnIds, organism, null);

				for (Reaction r : mapReactions) {
					pathway.addReaction(r);
				}
			}

		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return pathway;
	}

}
