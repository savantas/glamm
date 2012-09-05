package gov.lbl.glamm.server.dao.impl;

import gov.lbl.glamm.client.model.Gene;
import gov.lbl.glamm.client.model.util.Synonym;
import gov.lbl.glamm.server.GlammDbConnectionPool;
import gov.lbl.glamm.server.GlammSession;
import gov.lbl.glamm.server.dao.GeneDAO;
import gov.lbl.glamm.server.util.GlammUtils;

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
 * Implementation of the Gene DAO interface allowing access to genes stored in MetaMicrobesOnline.
 * @author jtbates
 *
 */
public class GeneMetaMolDAOImpl implements GeneDAO {

	private GlammSession sm;

	/**
	 * Constructor
	 * @param sm The GLAMM session.
	 */
	public GeneMetaMolDAOImpl(final GlammSession sm) {
		this.sm = sm;
	}

	@Override
	public Set<String> getEcNumsForOrganism(String taxonomyId) {
		Set<String> ecNums = null;

		if(!sm.getServerConfig().hasMetagenomeHost())
			return ecNums;

		if(taxonomyId != null && !taxonomyId.isEmpty()) {
			String sql = "select distinct L2E.ecNum " +
			"from meta2010jul.Locus2Ec L2E " + 
			"join meta2010jul.Locus L on (L2E.locusId=L.locusId) " +
			"where L.priority=1 and " +
			"L.taxonomyId=?";

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

		if(!sm.getServerConfig().hasMetagenomeHost())
			return new HashSet<Gene>();

		if(taxonomyId != null && !taxonomyId.isEmpty() && 
				ecNums != null && ecNums.size() > 0) {

			String sql = "select distinct L2E.ecNum, L2E.locusId, Syn.name, Syn.type " +
			"from meta2010jul.Locus2Ec L2E " + 
			"join meta2010jul.Locus L on (L2E.locusId=L.locusId) " +
			"left outer join meta2010jul.Synonym Syn on (Syn.locusId=L2E.locusId) " +
			"where L.taxonomyId=" + taxonomyId + " and " +
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
	public Set<Gene> getGenesForVimssIds(String taxonomyId, Collection<String> extIds) {
		Set<Gene> genes = null;

		if(!sm.getServerConfig().hasMetagenomeHost())
			return new HashSet<Gene>();

		if(taxonomyId != null && !taxonomyId.isEmpty() && 
				extIds != null && extIds.size() > 0) {

			String sql = "select distinct L2E.ecNum, L2E.locusId, Syn.name, Syn.type " +
			"from meta2010jul.Locus2Ec L2E " + 
			"join meta2010jul.Locus L on (L2E.locusId=L.locusId) " +
			"left outer join meta2010jul.Synonym Syn on (Syn.locusId=L2E.locusId) " +
			"where L.taxonomyId=" + taxonomyId + " and " +
			"L.priority=1 and " +
			"L2E.locusId in (" + GlammUtils.joinArray(extIds.toArray()) + ");";

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
		if (!sm.getServerConfig().hasMetagenomeHost() || ids == null || ids.size() == 0)
			return new HashSet<Gene>();
		
		String sql = "SELECT DISTINCT L2E.ecNum, L2E.locusId, Syn.name, Syn.type " +
					 "FROM meta2010jul.Locus2Ec L2E " +
					 "JOIN meta2010jul.Locus L on (L2E.locusId=L.locusId) " +
					 "LEFT OUTER JOIN meta2010jul.Synonym Syn on (Syn.locusId=L2E.locusId) " +
					 "WHERE L.priority=1 AND" +
					 "L2E.locusId in (" + GlammUtils.joinArray(ids.toArray()) + ");";
		
		try {
			Connection connection = GlammDbConnectionPool.getConnection(sm);
			Statement statement = connection.createStatement();
			
			ResultSet rs = statement.executeQuery(sql);
			
			Set<Gene> genes = processResultSet(rs);
			
			rs.close();
			statement.close();
			connection.close();

			return genes;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return new HashSet<Gene>();
	}

	@Override
	public Set<Gene> getGenesForOrganism(String taxonomyId) {

		Set<Gene> genes = null;

		if(!sm.getServerConfig().hasMetagenomeHost())
			return new HashSet<Gene>();

		if(taxonomyId != null && !taxonomyId.isEmpty()) {

			String sql = "select distinct L2E.ecNum, L2E.locusId, Syn.name, Syn.type " +
			"from meta2010jul.Locus2Ec L2E " + 
			"join meta2010jul.Locus L on (L2E.locusId=L.locusId) " +
			"left outer join meta2010jul.Synonym Syn on (Syn.locusId=L2E.locusId) " +
			"where L.taxonomyId=\"" + taxonomyId + "\" and " +
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

		if(!sm.getServerConfig().hasMetagenomeHost())
			return new HashSet<Gene>();

		if(taxonomyId != null && !taxonomyId.isEmpty() && 
				rxnIds != null && rxnIds.length > 0) {

			String sql = "select distinct L2E.locusId, L2E.ecNum, Syn.name, Syn.type " +
			"from meta2010jul.Locus2Ec L2E " +
			"join glamm.GlammEnzyme E on (E.ecNum=L2E.ecNum) " +
			"join glamm.GlammXref X on (E.reactionGuid=X.fromGuid) " +
			"left outer join meta2010jul.Synonym Syn on (Syn.locusId=L2E.locusId) " +
			"join meta2010jul.Locus L on (L2E.locusId=L.locusId) " +
			"where L.taxonomyId=" + taxonomyId + " and " +
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

	@Override
	public Set<Gene> getGenesForSynonyms(String taxonomyId,
			Collection<String> synonyms) {
		//TODO get genes for synonyms other than VIMSS ids
		return getGenesForVimssIds(taxonomyId, synonyms);
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

	/**
	 * Gets the set of taxonomy ids for a collection of VIMSS ids (MicrobesOnline locus ids.)
	 * @param vimssIds The collection of VIMSS ids.
	 * @return The set of taxonomy ids.
	 */
	public Set<String> getTaxonomyIdsForVimssIds(Collection<String> vimssIds) {
		if(vimssIds == null || vimssIds.isEmpty())
			return null;

		Set<String> taxonomyIds = null;

		if(!sm.getServerConfig().hasMetagenomeHost())
			return taxonomyIds;

		String sql = "select distinct(L.taxonomyId) " +
		"from meta2010jul.Locus L " +
		"where L.locusId in ("+ GlammUtils.joinCollection(vimssIds) + ") " +
		"and L.isActive=1;";

		try {
			Connection connection = GlammDbConnectionPool.getConnection(sm);
			Statement statement = connection.createStatement();

			ResultSet rs = statement.executeQuery(sql);

			while(rs.next()) {

				String taxonomyId = rs.getString("taxonomyId");

				if(taxonomyIds == null)
					taxonomyIds = new HashSet<String>();

				taxonomyIds.add(taxonomyId);
			}
			
			rs.close();
			connection.close();

		} catch(Exception e) {
			e.printStackTrace();
		}

		return taxonomyIds;
	}

}
