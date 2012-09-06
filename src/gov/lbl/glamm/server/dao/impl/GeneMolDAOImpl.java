package gov.lbl.glamm.server.dao.impl;

import gov.lbl.glamm.server.GlammDbConnectionPool;
import gov.lbl.glamm.server.GlammSession;
import gov.lbl.glamm.server.dao.GeneDAO;
import gov.lbl.glamm.server.util.GlammUtils;
import gov.lbl.glamm.shared.model.Gene;
import gov.lbl.glamm.shared.model.util.Synonym;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
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

		genes.addAll(locusId2Gene.values());

		return genes;
	}

	

	@Override
	public Set<Gene> getGenesForSynonyms(String taxonomyId,
			Collection<String> synonyms) {
		//TODO get genes for synonyms other than VIMSS ids
		return getGenesForVimssIds(taxonomyId, synonyms);
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
