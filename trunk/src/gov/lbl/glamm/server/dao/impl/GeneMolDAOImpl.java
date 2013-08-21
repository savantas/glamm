package gov.lbl.glamm.server.dao.impl;

import gov.lbl.glamm.server.ConfigurationManager;
import gov.lbl.glamm.server.GlammDbConnectionPool;
import gov.lbl.glamm.server.GlammSession;
import gov.lbl.glamm.server.dao.GeneDAO;
import gov.lbl.glamm.server.kbase.dao.KBTranslationDAO;
import gov.lbl.glamm.server.kbase.dao.impl.KBTranslationDAOImpl;
import gov.lbl.glamm.server.util.GlammUtils;
import gov.lbl.glamm.shared.DeploymentDomain;
import gov.lbl.glamm.shared.model.Gene;
import gov.lbl.glamm.shared.model.util.Synonym;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Implementation of the Gene DAO interface allowing access to genes in MicrobesOnline
 * @author jtbates
 *
 */
public class GeneMolDAOImpl implements GeneDAO {

	private GlammSession sm;

	/**
	 * Constructor.
	 * @param sm The GLAMM Session.
	 */
	public GeneMolDAOImpl(GlammSession sm) {
		this.sm = sm;
	}
	
	@Override
	public Set<String> getEcNumsForOrganism(String taxonomyId) {
		
		Set<String> ecNums = null;
		
		if(taxonomyId != null && !taxonomyId.isEmpty()) {
			String sql = "select distinct L2E.ecNum " +
			"from Locus2Ec L2E " + 
			"join Locus L on (L2E.locusId=L.locusId) " +
			"join Scaffold S on (L.scaffoldId=S.scaffoldId) " +
			"where L.priority=1 and " +
			"S.taxonomyId=?";
			
			try {

				Connection connection = GlammDbConnectionPool.getConnection(sm);
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
	
	

	@Override
	public Set<Gene> getGenesForEcNums(String taxonomyId, Collection<String> ecNums) {

		Set<Gene> genes = null;

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

				Connection connection = GlammDbConnectionPool.getConnection(sm);
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
	
	

	@Override
	public Set<Gene> getGenesForOrganism(String taxonomyId) {

		Set<Gene> genes = null;

		if(taxonomyId != null && !taxonomyId.isEmpty()) {
			
			String sql = "select distinct L2E.ecNum, L2E.locusId, Syn.name, Syn.type " +
			"from Locus2Ec L2E " + 
			"join Locus L on (L2E.locusId=L.locusId) " +
			"join Scaffold S on (L.scaffoldId=S.scaffoldId) " +
			"left outer join Synonym Syn on (Syn.locusId=L2E.locusId) " +
			"where S.taxonomyId=\"" + taxonomyId + "\" and " +
			"L.priority=1;";

			try {

				Connection connection = GlammDbConnectionPool.getConnection(sm);
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

	

	@Override
	public Set<Gene> getGenesForRxnIds(String taxonomyId, String[] rxnIds) {

		Set<Gene> genes = null;

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

				Connection connection = GlammDbConnectionPool.getConnection(sm);
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

	

	private Set<Gene> processResultSet(ResultSet rs) 
	throws SQLException {
		
		Set<Gene> genes = new HashSet<Gene>();
		Map<String, Gene> locusId2Gene = new HashMap<String, Gene>();

		while(rs.next()) {

			String locusId	= rs.getString("locusId");
			String ecNum 	= rs.getString("ecNum");
			String synName 	= rs.getString("name");
			String synType	= rs.getString("type");
			
			Gene gene = locusId2Gene.get(locusId);
			
			if(gene == null) {
				gene = new Gene();
				//gene.setVimmsId(locusId);
				gene.addSynonym(new Synonym(locusId, Gene.SYNONYM_TYPE_VIMSS));
				locusId2Gene.put(locusId, gene);
			}

			gene.addEcNum(ecNum);
			gene.addSynonym(new Synonym(synName, synType));
			
		}

		if (ConfigurationManager.getDeploymentDomain() == DeploymentDomain.KBASE) {
			// get the set of KBase fids and put them in all the genes as synonyms
			
			KBTranslationDAO translator = new KBTranslationDAOImpl(sm);
			
			List<String> locusIds = new ArrayList<String>();
			locusIds.addAll(locusId2Gene.keySet());
			Map<String, List<String>> locus2Fids = translator.locusIds2Fids(locusIds);
			
			// Some kinda clunky null checking here - saw some issues in production earlier.
			if (locus2Fids != null) {	// we have a non-null locus id
				for (String locusId : locus2Fids.keySet()) {
					if (locusId != null && locus2Fids.get(locusId) != null) {	// the list of fids is non null
						for (String fid : locus2Fids.get(locusId)) {
							if (locusId2Gene.containsKey(locusId))
								locusId2Gene.get(locusId).addSynonym(new Synonym(fid, Gene.SYNONYM_TYPE_KBASE));
						}
					}
				}
			}
		}

		genes.addAll(locusId2Gene.values());

		return genes;
	}

	

	@Override
	public Set<Gene> getGenesForSynonyms(String taxonomyId, Collection<String> synonyms) {
		Set<Gene> genes = null;
		
		if (synonyms != null && synonyms.size() > 0) {
			String sql = "SELECT DISTINCT L2E.ecNum, L.locusId, SY.name, SY.type " +
						 "FROM Synonym SY " +
						 "INNER JOIN Locus L ON (L.locusId = SY.locusId) AND (L.version = SY.version) " +
						 "INNER JOIN Scaffold SC ON (L.scaffoldId = SC.scaffoldId) " + 
						 "JOIN Locus2Ec L2E on (L2E.locusId = L.locusId) " +
						 "WHERE L.priority = 1 AND " +
						 "SC.isActive = 1 AND " +
						 "SC.isGenomic = 1 AND ";
			
			if (taxonomyId != null && !taxonomyId.isEmpty())
				sql += "SC.taxonomyId = " + taxonomyId + " AND ";
			
			sql += "(SY.name IN " + GlammUtils.genSQLPlaceholderList(synonyms.size()) + 
					"OR L.locusId IN " + GlammUtils.genSQLPlaceholderList(synonyms.size()) + ");";
			
			try {
				Connection connection = GlammDbConnectionPool.getConnection(sm);
				PreparedStatement statement = connection.prepareStatement(sql);
				
				String[] synArray = synonyms.toArray(new String[synonyms.size()]);
				for (int i=0; i<synonyms.size(); i++) {
					statement.setString(i+1, synArray[i]);
					statement.setString(i+1+synonyms.size(), synArray[i]);
				}
				
				ResultSet rs = statement.executeQuery();
				
				genes = processResultSet(rs);
				rs.close();
				statement.close();
				connection.close();
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}

		// The 'synonyms' might contain valid locusIds, too. Pretend that 
		
		return genes;
	}
	
	
	
	@Override
	public Set<Gene> getGenesForVimssIds(String taxonomyId, Collection<String> extIds) {
		Set<Gene> genes = null;

		if(extIds != null && extIds.size() > 0) {

			String sql = "select distinct L2E.ecNum, L2E.locusId, Syn.name, Syn.type " +
			"from Locus2Ec L2E " + 
			"join Locus L on (L2E.locusId=L.locusId) " +
			"join Scaffold S on (L.scaffoldId=S.scaffoldId) " +
			"left outer join Synonym Syn on (Syn.locusId=L2E.locusId) " +
			"where L.priority=1 and ";
			
			if (taxonomyId != null && !taxonomyId.isEmpty())
				sql += "S.taxonomyId=" + taxonomyId + " and ";
			
			sql += "L2E.locusId in (" + GlammUtils.joinArray(extIds.toArray()) + ");";

			try {

				Connection connection = GlammDbConnectionPool.getConnection(sm);
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
	
	@Override
	public Set<Gene> getGenesForVimssIds(Collection<String> ids) {
		if (ids == null || ids.size() == 0) {
			return new HashSet<Gene>();
		}
		
		String sql = "SELECT DISTINCT L2E.ecNum, L2E.locusId, Syn.name, Syn.type " +
					 "FROM Locus2Ec L2E " +
					 "JOIN Locus L on (L2E.locusId=L.locusId) " +
					 "JOIN Scaffold S on (L.scaffoldId=S.scaffoldId) " +
					 "LEFT OUTER JOIN Synonym Syn on (Syn.locusId=L2E.locusId) " +
					 "WHERE L.priority=1 AND " +
					 "L2E.locusId in (" + GlammUtils.joinArray(ids.toArray()) + ");";
		
		try {
			Connection connection = GlammDbConnectionPool.getConnection(sm);
			Statement statement = connection.createStatement();
			
			ResultSet rs = statement.executeQuery(sql);
			
			Set<Gene> genes = processResultSet(rs);
			
			rs.close();
			connection.close();
			
			return genes;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return new HashSet<Gene>();
	}
}
