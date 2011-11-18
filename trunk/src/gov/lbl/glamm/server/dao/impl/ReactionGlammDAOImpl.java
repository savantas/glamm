package gov.lbl.glamm.server.dao.impl;

import gov.lbl.glamm.client.model.Compound;
import gov.lbl.glamm.client.model.Reaction;
import gov.lbl.glamm.client.model.Reaction.Participant;
import gov.lbl.glamm.client.model.Reaction.Participant.KeggRpairRole;
import gov.lbl.glamm.client.model.util.Xref;
import gov.lbl.glamm.server.GlammDbConnectionPool;
import gov.lbl.glamm.server.GlammSession;
import gov.lbl.glamm.server.dao.ReactionDAO;
import gov.lbl.glamm.server.util.GlammUtils;
import gov.lbl.glammdb.domain.PersistentPathway;
import gov.lbl.glammdb.domain.PersistentPwyElement;
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

	private Set<Reaction> addParticipatingCompounds(final Set<Reaction> reactions) {

		if(reactions == null || reactions.isEmpty())
			return reactions;

		HashMap<String, Reaction> guid2Rxn = new HashMap<String, Reaction>();
		for(Reaction rxn : reactions) 
			guid2Rxn.put(rxn.getGuid(), rxn);

		String sql = "select distinct RP.reactionGuid, RP.compoundGuid, RP.coefficient, RP.pType, " +
		"KRP.rpairRole, C.commonName, C.mass, C.formula, C.smiles, C.inchi " +
		"from glamm.GlammReactionParticipant RP " +
		"join glamm.GlammKeggRpair KRP using (reactionGuid) " +
		"join glamm.GlammCompound C on (C.guid=RP.compoundGuid) " +
		"where RP.reactionGuid in (" + GlammUtils.joinCollection(guid2Rxn.keySet()) + ") " + 
		"order by RP.reactionGuid;";

		try {
			HashMap<String, Participant> participants = new HashMap<String, Participant>();
			HashMap<String, Compound> guid2Cpd = new HashMap<String, Compound>();
			
			Connection connection = GlammDbConnectionPool.getConnection(sm);
			Statement statement = connection.createStatement();
			
			ResultSet rs = statement.executeQuery(sql);
			
			while(rs.next()) {
	
				String rxnGuid = rs.getString("reactionGuid");
				String cpdGuid = rs.getString("compoundGuid");
				String coefficient = rs.getString("coefficient");
				String pType = rs.getString("pType");
				String role = rs.getString("rpairRole");
				String commonName = rs.getString("commonName");
				String mass = rs.getString("mass");
				String formula = rs.getString("formula");
				String smiles = rs.getString("smiles");
				String inchi = rs.getString("inchi");
				
				// build compound
				Compound cpd = guid2Cpd.get(cpdGuid);
				if(cpd == null) {
					cpd = new Compound();
					cpd.setGuid(cpdGuid);
					cpd.setName(commonName);
					cpd.setMass(mass);
					cpd.setFormula(formula);
					cpd.setSmiles(smiles);
					cpd.setInchi(inchi);
					guid2Cpd.put(cpdGuid, cpd);
				}
				
				// build participant
				String participantsKey = rxnGuid + "_" + cpdGuid;
				Participant p = participants.get(participantsKey);
				if(p == null) {
					p = new Participant(cpd, coefficient, KeggRpairRole.fromString(role));
					participants.put(participantsKey, p);
				}
				else if(p.getRole() == KeggRpairRole.OTHER) {
					p.setRole(KeggRpairRole.fromString(role));
				}
				
				// add participants to reaction based on their pType 
				Reaction rxn = guid2Rxn.get(rxnGuid);
				if(rxn != null) {
					if(pType.equals("REACTANT"))
						rxn.addSubstrate(p);
					else
						rxn.addProduct(p);
				}
			}
			
			rs.close();
			connection.close();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return reactions;
	}

	@Override
	public Set<Reaction> getReactions(Collection<String> rxnIds) {

		Set<Reaction> rxns = new HashSet<Reaction>();

		if(rxnIds == null || rxnIds.isEmpty())
			return rxns;

		Map<String, Reaction> def2Rxn = new HashMap<String, Reaction>();

		String sql = "select R.guid, E.ecNum, R.definition, X.toXrefId, X.xrefDbName " +
		"from glamm.GlammEnzyme E " +
		"right outer join glamm.GlammReaction R on (E.reactionGuid=R.guid) " +
		"join glamm.GlammXref X on (X.fromGuid=R.guid) " +
		"where X.toXrefId in (" + GlammUtils.joinCollection(rxnIds) + ");"; 

		try {

			Connection connection = GlammDbConnectionPool.getConnection(sm);
			Statement statement = connection.createStatement();

			ResultSet rs = statement.executeQuery(sql);

			while(rs.next()) {

				String guid			= rs.getString("guid");
				String ecNum 		= rs.getString("ecNum");
				String definition 	= rs.getString("definition");
				String xrefId		= rs.getString("toXrefId");
				String xrefDbName	= rs.getString("xrefDbName");

				// TODO: Awful hack here - in the future, make sure to specify the external id from the database from which this reaction was
				// TODO: originally sourced.  This will probably require a re-import (ugh.)
				if(definition.equals(definition.toUpperCase()))
					continue;

				Reaction reaction = def2Rxn.get(definition);
				if(reaction == null) {
					reaction = new Reaction();
					reaction.setGuid(guid);
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

		return addParticipatingCompounds(rxns);
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
		PersistentPathway result = (PersistentPathway) session.createCriteria(PersistentPathway.class)
		.add(Restrictions.eq("mapId", mapId))
		.uniqueResult();
		session.getTransaction().commit();

		if(result == null)
			return new HashSet<Reaction>();

		Set<String> rxnIds = new HashSet<String>();
		for(PersistentPwyElement element : result.getElements()) {
			if(element.getType() == PersistentPwyElement.Type.RXN)
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
