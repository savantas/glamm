package gov.lbl.glamm.server.dao.impl;

import gov.lbl.glamm.client.model.Reaction;
import gov.lbl.glamm.client.model.util.Xref;
import gov.lbl.glamm.server.GlammDbConnectionPool;
import gov.lbl.glamm.server.GlammSession;
import gov.lbl.glamm.server.dao.ReactionDAO;
import gov.lbl.glamm.server.util.GlammUtils;
import gov.lbl.glammdb.domain.Pathway;
import gov.lbl.glammdb.domain.PwyElement;
import gov.lbl.glammdb.util.HibernateUtil;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

public class ReactionGlammDAOImpl implements ReactionDAO {

	private GlammSession sm;

	public ReactionGlammDAOImpl(final GlammSession sm) {
		this.sm = sm;
	}

	@Override
	public Set<Reaction> getReactions(Collection<String> rxnIds) {

		Set<Reaction> rxns = new HashSet<Reaction>();

		if(rxnIds == null || rxnIds.isEmpty())
			return rxns;

		Map<String, Reaction> def2Rxn = new HashMap<String, Reaction>();

		String sql = "select E.ecNum, R.definition, X.toXrefId, X.xrefDbName " +
		"from glamm.GlammEnzyme E " +
		"right outer join glamm.GlammReaction R on (E.reactionGuid=R.guid) " +
		"join glamm.GlammXref X on (X.fromGuid=R.guid) " +
		"where X.toXrefId in (" + GlammUtils.joinCollection(rxnIds) + ");"; 

		try {

			Connection connection = GlammDbConnectionPool.getConnection(sm);
			Statement statement = connection.createStatement();

			ResultSet rs = statement.executeQuery(sql);

			while(rs.next()) {

				String ecNum 		= rs.getString("ecNum");
				String definition 	= rs.getString("definition");
				String xrefId		= rs.getString("toXrefId");
				String xrefDbName	= rs.getString("xrefDbName");

				Reaction reaction = def2Rxn.get(definition);
				if(reaction == null) {
					reaction = new Reaction();
					reaction.setDefinition(definition);
					reaction.addXref(new Xref(xrefId, xrefDbName));
					def2Rxn.put(definition, reaction);
				}
				reaction.addEcNum(ecNum);

			}

			rxns.addAll(def2Rxn.values());
			rs.close();
			connection.close();

		} catch(Exception e) {
			e.printStackTrace();
		}

		return rxns;
	}

	@Override
	public Set<Reaction> getReactionsForEcNums(Collection<String> ecNums) {
		Set<Reaction> reactions = new HashSet<Reaction>();

		if(ecNums == null || ecNums.isEmpty()) 
			return reactions;
		String sql = "select distinct(X.toXrefId), X.xrefDbName, E.ecNum " +
		"from glamm.GlammEnzyme E " +
		"join glamm.GlammXref X on (E.reactionGuid=X.fromGuid) " +
		"where E.ecNum in (" + GlammUtils.joinCollection(ecNums) + ");";

		try {

			Connection connection = GlammDbConnectionPool.getConnection(sm);
			Statement statement = connection.createStatement();

			ResultSet rs = statement.executeQuery(sql);

			while(rs.next()) {

				Reaction reaction = new Reaction();
				reaction.addXref(new Xref(rs.getString("toXrefId"), rs.getString("xrefDbName")));
				reaction.addEcNum(rs.getString("ecNum"));
				reactions.add(reaction);
			}

			rs.close();
			connection.close();

		} catch(Exception e) {
			e.printStackTrace();
		}

		return reactions;
	}



	@Override
	public Set<Reaction> getReactionsForSearch(String mapId) {
		
		Session session = HibernateUtil.getSessionFactory(sm).getCurrentSession();
		session.beginTransaction();
		Pathway result = (Pathway) session.createCriteria(Pathway.class)
		.add(Restrictions.eq("mapId", mapId))
		.uniqueResult();
		session.getTransaction().commit();
		
		if(result == null)
			return new HashSet<Reaction>();
		
		Set<String> rxnIds = new HashSet<String>();
		for(PwyElement element : result.getElements()) {
			if(element.getType() == PwyElement.Type.RXN)
				rxnIds.add(element.getXrefId());
		}
		
		return getReactions(rxnIds);
		
	}

	@Override
	public Set<String> getRxnIdsForEcNums(Collection<String> ecNums) {

		Set<String> rxnIds = new HashSet<String>();

		if(ecNums == null || ecNums.isEmpty())
			return rxnIds;

		Set<Reaction> rxns = getReactionsForEcNums(ecNums);
		for(Reaction rxn : rxns) {
			Set<Xref> xrefs = rxn.getXrefs();
			for(Xref xref : xrefs)
				rxnIds.add(xref.getXrefId());
		}
		return rxnIds;
	}



}
