package gov.lbl.glamm.server.dao.impl;

import gov.lbl.glamm.client.model.Organism;
import gov.lbl.glamm.client.model.Sample;
import gov.lbl.glamm.server.GlammDbConnectionPool;
import gov.lbl.glamm.server.GlammSession;
import gov.lbl.glamm.server.dao.OrganismDAO;
import gov.lbl.glamm.server.util.GlammUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class OrganismMetaMolDAOImpl implements OrganismDAO {

	private GlammSession sm = null;

	public OrganismMetaMolDAOImpl(GlammSession sm) {
		this.sm = sm;
	}

	@Override
	public List<Organism> getAllOrganisms() {
		return getAllOrganismsWithDataForType(null);
	}

	@Override
	public List<Organism> getAllOrganismsWithDataForType(Sample.DataType dataType) {
		
		ArrayList<Organism> organisms = null;
		
		if(!sm.getServerConfig().hasMetagenomeHost() || dataType != Sample.DataType.NONE)
			return organisms;

		String sql = "select distinct(taxonomyId), name " +
		"from meta2010jul.Taxonomy t " +
		"join meta2010jul.Scaffold s using (taxonomyId) " +
		"join meta2010jul.ACL A on (A.resourceId=s.scaffoldId and A.resourceType='scaffold') " +
		"where t.taxonomyId >= 1000000000000 " +
		"and s.isGenomic=1 and s.isActive=1 and s.length >= 1000 " +
		"and A.requesterId in (" + (sm != null ? GlammUtils.joinCollection(sm.getUser().getGroupIds()) : "1") + ") and A.requesterType='group' and A.read=1 " +
		"order by t.name;";
		
		try {

			Connection connection = GlammDbConnectionPool.getConnection(sm);
			PreparedStatement ps = connection.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();

			while(rs.next()) {

				String name = rs.getString("name");
				String taxonomyId = rs.getString("taxonomyId");

				if(name != null && taxonomyId != null) {
					Organism organism = new Organism(taxonomyId, name, false);
					if(organisms == null)
						organisms = new ArrayList<Organism>();
					if(!organisms.contains(organism))
						organisms.add(organism);
				}
			}

			rs.close();
			connection.close();

		} catch(Exception e) {
			e.printStackTrace();
		}

		return organisms;
	}

	@Override
	public Map<String, Set<Organism>> getTransgenicCandidatesForEcNums(Set<String> ecNums) {

		Map<String, Set<Organism>> ecNum2Organisms = null;

		if(!sm.getServerConfig().hasMetagenomeHost() || ecNums == null || ecNums.isEmpty())
			return ecNum2Organisms;

		String sql = "select distinct T.taxonomyId, T.name, L2E.ecNum " +
			"from meta2010jul.Taxonomy T " +
			"join meta2010jul.Scaffold S on (S.taxonomyId=T.taxonomyId) " +
			"join meta2010jul.Locus2Ec L2E on (L2E.scaffoldId=S.scaffoldId) " +
			"join meta2010jul.ACL A on(A.resourceId=S.ScaffoldId and A.resourceType='scaffold') " +
			"where S.isGenomic=1 and S.isActive=1 and S.length >= 1000 " +
			"and A.requesterId in (" + (sm != null ? GlammUtils.joinCollection(sm.getUser().getGroupIds()) : "1") + ") and A.requesterType='group' and A.read=1 " +
			"and L2E.ecNum in (" + GlammUtils.joinCollection(ecNums) + ") " +
			"order by T.name;";
		
		try {

			Connection connection = GlammDbConnectionPool.getConnection(sm);
			Statement statement = connection.createStatement();

			ResultSet rs = statement.executeQuery(sql);

			while(rs.next()) {

				if(ecNum2Organisms == null)
					ecNum2Organisms = new HashMap<String, Set<Organism>>();

				String taxonomyId	= rs.getString("taxonomyId");
				String name			= rs.getString("name");
				String ecNum 		= rs.getString("ecNum");

				Set<Organism> organisms = ecNum2Organisms.get(ecNum);
				if(organisms == null) {
					organisms = new HashSet<Organism>();
					ecNum2Organisms.put(ecNum, organisms);
				}

				Organism organism = new Organism(taxonomyId, name, false);
				organisms.add(organism);

			}

			rs.close();
			connection.close();

		} catch(Exception e) {
			e.printStackTrace();
		}

		return ecNum2Organisms;
	}

	@Override
	public Organism getOrganismForTaxonomyId(final String taxonomyId) {
		
		Organism organism = null;

		if(!sm.getServerConfig().hasMetagenomeHost())
			return organism;
		
		String sql = "select distinct(name) " +
			"from meta2010jul.Taxonomy t " +
			"join meta2010jul.Scaffold s " +
			"using (taxonomyId) " +
			"where t.taxonomyId=? " +
			"and s.isGenomic=1 and s.isActive=1 and s.length >= 1000 order by t.name;";


		try {

			Connection connection = GlammDbConnectionPool.getConnection(sm);
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setString(1, taxonomyId);

			ResultSet rs = ps.executeQuery();

			if(rs.next()) {
				String name = rs.getString("name");
				organism = new Organism(taxonomyId, name, false);
			}

			rs.close();
			connection.close();

		} catch(Exception e) {
			e.printStackTrace();
		}

		return organism;
	}

}
