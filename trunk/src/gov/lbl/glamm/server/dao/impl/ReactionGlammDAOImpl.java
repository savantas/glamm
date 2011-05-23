package gov.lbl.glamm.server.dao.impl;

import gov.lbl.glamm.client.model.GlammPrimitive.Xref;
import gov.lbl.glamm.client.model.Reaction;
import gov.lbl.glamm.server.GlammDbConnectionPool;
import gov.lbl.glamm.server.dao.ReactionDAO;
import gov.lbl.glamm.shared.GlammConstants;
import gov.lbl.glamm.shared.GlammUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

public class ReactionGlammDAOImpl implements ReactionDAO {

	@Override
	public ArrayList<Reaction> getReactions(Collection<String> rxnIds, String dbName) {

		HashMap<String, Reaction> guid2Reaction = new HashMap<String, Reaction>();
		
		if(rxnIds != null && !rxnIds.isEmpty() && 
				dbName != null && !dbName.isEmpty()) {

			String sql = "select R.guid, E.ecNum, R.definition, X.toXrefId " +
			"from glamm.GlammEnzyme E " +
			"right outer join glamm.GlammReaction R on (E.reactionGuid=R.guid) " +
			"join glamm.GlammXref X on (X.fromGuid=R.guid) " +
			"join glamm.GlammEntity2DataSource E2DS on (E2DS.entityGuid=R.guid) " +
			"join glamm.GlammDataSource DS on (DS.guid=E2DS.dataSourceGuid) " +
			"where X.toXrefId in (" + GlammUtils.joinCollection(rxnIds) + ") " + 
			"and DS.dbName=\"" + dbName + "\";";

			try {

				Connection connection = GlammDbConnectionPool.getConnection();
				Statement statement = connection.createStatement();

				ResultSet rs = statement.executeQuery(sql);

				while(rs.next()) {

					String guid 		= rs.getString("guid");
					String ecNum 		= rs.getString("ecNum");
					String definition 	= rs.getString("definition");
					String xrefId		= rs.getString("toXrefId");
					
					Reaction reaction = guid2Reaction.get(guid);
					if(reaction == null) { 
						reaction = new Reaction();
						reaction.addXref(xrefId, dbName);
						guid2Reaction.put(guid, reaction);
					}
				
					reaction.addEcNum(ecNum);
					reaction.setDefinition(definition);
					
				}

				rs.close();
				connection.close();

			} catch(Exception e) {
				e.printStackTrace();
			}
		}

		if(guid2Reaction.isEmpty())
			return null;
		
		return new ArrayList<Reaction>(guid2Reaction.values());
	}

	@Override
	public ArrayList<Reaction> getReactionsForEcNums(Collection<String> ecNums, String dbName) {
		ArrayList<Reaction> reactions = null;
		
		if(ecNums != null && !ecNums.isEmpty()) {
			String sql = "select distinct(X.toXrefId), E.ecNum " +
			"from glamm.GlammEnzyme E " +
			"join glamm.GlammXref X on (E.reactionGuid=X.fromGuid) " +
			"where E.ecNum in (" + GlammUtils.joinCollection(ecNums) + ") " +
			"and X.xrefDbName=\"" + dbName + "\";";
			
			try {

				Connection connection = GlammDbConnectionPool.getConnection();
				Statement statement = connection.createStatement();

				ResultSet rs = statement.executeQuery(sql);

				while(rs.next()) {

					Reaction reaction = new Reaction();
					reaction.addXref(rs.getString("toXrefId"), dbName);
					reaction.addEcNum(rs.getString("ecNum"));

					if(reactions == null)
						reactions = new ArrayList<Reaction>();
					reactions.add(reaction);
				}

				rs.close();
				connection.close();

			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		
		return reactions;
	}
	
	

	@Override
	public ArrayList<Reaction> getReactionsForSearch(String dbName) {
		final int MAX_RETRIES = 5;
		
		for(int i = 0; i < MAX_RETRIES; i++) {
			ArrayList<Reaction> rxns = _getReactionsForSearch(dbName);
			if(rxns != null && !rxns.isEmpty()) 
				return rxns;
		}
		
		return null;
	}

	private ArrayList<Reaction> _getReactionsForSearch(String dbName) {
		
		// TODO: Generalize for other maps besides global

		HashMap<String, Reaction> guid2Reaction = new HashMap<String, Reaction>();

		if(dbName != null && !dbName.isEmpty()) {

			String sql = "select E.reactionGuid, E.ecNum, X.toXrefId " +
			"from glamm.GlammEnzyme E " +
			"join glamm.GlammXref X on (E.reactionGuid=X.fromGuid) " +
			"where X.xrefDbName=? " +
			"and X.toXrefId in (" + GlammConstants.GLOBAL_MAP_RXN_IDS + ");";

			try {

				Connection connection = GlammDbConnectionPool.getConnection();
				PreparedStatement ps = connection.prepareStatement(sql);

				ps.setString(1, dbName);

				ResultSet rs = ps.executeQuery();

				while(rs.next()) {


					String guid 		= rs.getString("reactionGuid");
					String ecNum 		= rs.getString("ecNum");
					String xrefId		= rs.getString("toXrefId");
					
					Reaction reaction = guid2Reaction.get(guid);
					if(reaction == null) { 
						reaction = new Reaction();
						guid2Reaction.put(guid, reaction);
					}
				
					reaction.addEcNum(ecNum);
					reaction.addXref(xrefId, dbName);
					
				}

				rs.close();
				connection.close();

			} catch(Exception e) {
				e.printStackTrace();
			}
		}

		if(guid2Reaction.isEmpty())
			return null;
		
		return new ArrayList<Reaction>(guid2Reaction.values());

	}

	public ArrayList<Reaction> getRxn2EcMapping(String mapId, String dbName) {
		// TODO: Generalize for other maps besides global
		return getReactionsForSearch(dbName);
	}

	@Override
	public HashSet<String> getRxnIdsForEcNums(Collection<String> ecNums,
			String dbName) {
		HashSet<String> rxnIds = null;
		
		if(ecNums != null && !ecNums.isEmpty() && dbName != null && !dbName.isEmpty()) {
			ArrayList<Reaction> rxns = getReactionsForEcNums(ecNums, dbName);
			for(Reaction rxn : rxns) {
				HashSet<Xref> xrefs = rxn.getXrefs();
				for(Xref xref : xrefs) {
					if(xref.getXrefDbName().equals(dbName)) {
						if(rxnIds == null)
							rxnIds = new HashSet<String>();
						rxnIds.add(xref.getXrefId());
					}
				}
			}
		}
		
		return rxnIds;
	}



}
