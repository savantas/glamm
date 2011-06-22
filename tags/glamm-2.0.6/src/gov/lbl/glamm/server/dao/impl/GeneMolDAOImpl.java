package gov.lbl.glamm.server.dao.impl;

import gov.lbl.glamm.client.model.Gene;
import gov.lbl.glamm.server.GlammDbConnectionPool;
import gov.lbl.glamm.server.dao.GeneDAO;
import gov.lbl.glamm.shared.GlammUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

public class GeneMolDAOImpl implements GeneDAO {

	//********************************************************************************
	
	@Override
	public HashSet<String> getEcNumsForOrganism(String taxonomyId) {
		HashSet<String> ecNums = null;
		
		if(taxonomyId != null && !taxonomyId.isEmpty()) {
			String sql = "select distinct L2E.ecNum " +
			"from Locus2Ec L2E " + 
			"join Locus L on (L2E.locusId=L.locusId) " +
			"join Scaffold S on (L.scaffoldId=S.scaffoldId) " +
			"where L.priority=1 and " +
			"S.taxonomyId=?";
			
			try {

				Connection connection = GlammDbConnectionPool.getConnection();
				PreparedStatement ps = connection.prepareStatement(sql);

				ps.setString(1, taxonomyId);

				ResultSet rs = ps.executeQuery();

				while(rs.next()) {
					
					String ecNum 		= rs.getString("ecNum");
					
					if(ecNums == null)
						ecNums = new HashSet<String>();
					
					ecNums.add(ecNum);
					
				}

				rs.close();
				connection.close();

			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		
		return ecNums;
	}
	
	//********************************************************************************

	@Override
	public ArrayList<Gene> getGenesForEcNums(String taxonomyId, Collection<String> ecNums) {

		ArrayList<Gene> genes = null;

		if(taxonomyId != null && !taxonomyId.isEmpty() && 
				ecNums != null && ecNums.size() > 0) {

			String sql = "select distinct L2E.ecNum, L2E.locusId, Syn.name, Syn.type " +
			"from Locus2Ec L2E " + 
			"join Locus L on (L2E.locusId=L.locusId) " +
			"join Scaffold S on (L.scaffoldId=S.scaffoldId) " +
			"left outer join Synonym Syn on (Syn.locusId=L2E.locusId) " +
			"where S.taxonomyId=" + taxonomyId + " and " +
			"L.priority=1 and " +
			"L2E.ecNum in (" + GlammUtils.joinArray(ecNums.toArray()) + ");";

			try {

				Connection connection = GlammDbConnectionPool.getConnection();
				Statement  statement = connection.createStatement();


				ResultSet rs = statement.executeQuery(sql);

				genes = processResultSet(rs);

				rs.close();
				connection.close();

			} catch(Exception e) {
				e.printStackTrace();
			}
		}

		return genes;
	}
	
	//********************************************************************************

	@Override
	public ArrayList<Gene> getGenesForOrganism(String taxonomyId) {

		ArrayList<Gene> genes = null;

		if(taxonomyId != null && !taxonomyId.isEmpty()) {
			
			String sql = "select distinct L2E.ecNum, L2E.locusId, Syn.name, Syn.type " +
			"from Locus2Ec L2E " + 
			"join Locus L on (L2E.locusId=L.locusId) " +
			"join Scaffold S on (L.scaffoldId=S.scaffoldId) " +
			"left outer join Synonym Syn on (Syn.locusId=L2E.locusId) " +
			"where S.taxonomyId=\"" + taxonomyId + "\" and " +
			"L.priority=1;";

			try {

				Connection connection = GlammDbConnectionPool.getConnection();
				Statement  statement = connection.createStatement();


				ResultSet rs = statement.executeQuery(sql);

				genes = processResultSet(rs);

				rs.close();
				connection.close();

			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		
		return genes;
	}

	//********************************************************************************

	@Override
	public ArrayList<Gene> getGenesForRxnIds(String taxonomyId, String[] rxnIds) {

		ArrayList<Gene> genes = null;

		if(taxonomyId != null && !taxonomyId.isEmpty() && 
				rxnIds != null && rxnIds.length > 0) {

			String sql = "select distinct L2E.locusId, L2E.ecNum, Syn.name, Syn.type " +
					"from Locus2Ec L2E " +
					"join Scaffold S on (L2E.scaffoldId=S.scaffoldId) " +
					"join glamm.GlammEnzyme E on (E.ecNum=L2E.ecNum) " +
					"join glamm.GlammXref X on (E.reactionGuid=X.fromGuid) " +
					"left outer join Synonym Syn on (Syn.locusId=L2E.locusId) " +
					"join Locus L on (L2E.locusId=L.locusId) " +
					"where S.taxonomyId=" + taxonomyId + " and " +
					"L.priority=1 and " +
					"X.toXrefId in (" + GlammUtils.joinArray(rxnIds) + ");";

			try {

				Connection connection = GlammDbConnectionPool.getConnection();
				Statement statement = connection.createStatement();

				ResultSet rs = statement.executeQuery(sql);

				genes = processResultSet(rs);

				rs.close();
				connection.close();

			} catch(Exception e) {
				e.printStackTrace();
			}
		}

		return genes;
	}

	//********************************************************************************

	private ArrayList<Gene> processResultSet(ResultSet rs) 
	throws SQLException {
		ArrayList<Gene> genes = null;


		HashMap<String, Gene> locusId2Gene = new HashMap<String, Gene>();

		while(rs.next()) {

			String locusId	= rs.getString("locusId");
			String ecNum 	= rs.getString("ecNum");
			String synName 	= rs.getString("name");
			String synType	= rs.getString("type");
			
			Gene gene = locusId2Gene.get(locusId);
			
			if(gene == null) {
				gene = new Gene();
				//gene.setVimmsId(locusId);
				gene.addSynonym(locusId, Gene.SYNONYM_TYPE_VIMSS);
				locusId2Gene.put(locusId, gene);
			}

			gene.addEcNum(ecNum);
			gene.addSynonym(synName, synType);
			
		}

		if(locusId2Gene.values().size() > 0) {
			genes = new ArrayList<Gene>();
			genes.addAll(locusId2Gene.values());
		}

		return genes;
	}

	//********************************************************************************

	@Override
	public ArrayList<Gene> getGenesForSynonyms(String taxonomyId,
			Collection<String> synonyms) {
		//TODO get genes for synonyms other than VIMSS ids
		return getGenesForVimssIds(taxonomyId, synonyms);
	}
	
	//********************************************************************************
	
	@Override
	public ArrayList<Gene> getGenesForVimssIds(String taxonomyId, Collection<String> extIds) {
		ArrayList<Gene> genes = null;

		if(taxonomyId != null && !taxonomyId.isEmpty() && 
				extIds != null && extIds.size() > 0) {

			String sql = "select distinct L2E.ecNum, L2E.locusId, Syn.name, Syn.type " +
			"from Locus2Ec L2E " + 
			"join Locus L on (L2E.locusId=L.locusId) " +
			"join Scaffold S on (L.scaffoldId=S.scaffoldId) " +
			"left outer join Synonym Syn on (Syn.locusId=L2E.locusId) " +
			"where S.taxonomyId=" + taxonomyId + " and " +
			"L.priority=1 and " +
			"L2E.locusId in (" + GlammUtils.joinArray(extIds.toArray()) + ");";

			try {

				Connection connection = GlammDbConnectionPool.getConnection();
				Statement  statement = connection.createStatement();


				ResultSet rs = statement.executeQuery(sql);

				genes = processResultSet(rs);

				rs.close();
				connection.close();

			} catch(Exception e) {
				e.printStackTrace();
			}
		}

		return genes;
	}


	//********************************************************************************
	
	public HashMap<String, String> getVimssId2TaxonomyIdMapping(Collection<String> vimssIds) {
		if(vimssIds == null || vimssIds.isEmpty())
			return null;
		
		HashMap<String, String> mapping = null;
		String sql = "select L.locusId, S.taxonomyId " +
				"from Locus L " +
				"join Scaffold S on (L.scaffoldId=S.scaffoldId) " +
				"where L.locusId in ("+ GlammUtils.joinCollection(vimssIds) + ") " +
				"and S.isActive=1;";
		
		try {
			Connection connection = GlammDbConnectionPool.getConnection();
			Statement statement = connection.createStatement();
			
			ResultSet rs = statement.executeQuery(sql);
			
			while(rs.next()) {
				String locusId = rs.getString("locusId");
				String taxonomyId = rs.getString("taxonomyId");
				
				if(mapping == null)
					mapping = new HashMap<String, String>();
				
				mapping.put(locusId, taxonomyId);
			}
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return mapping;
	}
	
}
