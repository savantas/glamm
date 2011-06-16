package gov.lbl.glamm.server.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import gov.lbl.glamm.client.model.Organism;
import gov.lbl.glamm.client.model.Sample;
import gov.lbl.glamm.server.GlammDbConnectionPool;
import gov.lbl.glamm.server.dao.OrganismDAO;
import gov.lbl.glamm.shared.GlammUtils;

public class OrganismMetaMolDAOImpl implements OrganismDAO {

	@Override
	public ArrayList<Organism> getAllOrganisms() {
		return getAllOrganismsWithDataForType(null);
	}

	@Override
	public ArrayList<Organism> getAllOrganismsWithDataForType(String dataType) {
		ArrayList<Organism> organisms = null;

		String sql = "";
		if(dataType == null || dataType.isEmpty() || dataType.equals(Sample.DATA_TYPE_NONE)) {
			if(GlammDbConnectionPool.getDbConfig().isFilterOnAcl())
				sql = "select distinct(taxonomyId), name " +
				"from meta2010jul.Taxonomy t " +
				"join meta2010jul.Scaffold s using (taxonomyId) " +
				"join meta2010jul.ACL a on (a.resourceId=s.scaffoldId and a.resourceType='scaffold') " +
				"where t.taxonomyId >= 1000000000000 " +
				"and s.isGenomic=1 and s.isActive=1 and s.length >= 1000 " +
				"and a.requesterId=1 and a.requesterType='group' and a.read=1 " +
				"order by t.name;";
			else
				sql = "select distinct(taxonomyId), name " +
				"from meta2010jul.Taxonomy t " +
				"join meta2010jul.Scaffold s " +
				"using (taxonomyId) " +
				"where t.taxonomyId >= 1000000000000 " +
				"and s.isGenomic=1 and s.isActive=1 and s.length >= 1000 order by t.name;";
		}
		else 
			return null;

		try {

			Connection connection = GlammDbConnectionPool.getConnection();
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
	public HashMap<String, HashSet<Organism>> getTransgenicCandidatesForEcNums(HashSet<String> ecNums) {
		HashMap<String, HashSet<Organism>> ecNum2Organisms = null;

		if(ecNums == null || ecNums.isEmpty())
			return null;

		String sql = "";
		if(GlammDbConnectionPool.getDbConfig().isFilterOnAcl())
			sql = "select distinct T.taxonomyId, T.name, L2E.ecNum " +
			"from meta2010jul.Taxonomy T " +
			"join meta2010jul.Scaffold S on (S.taxonomyId=T.taxonomyId) " +
			"join meta2010jul.Locus2Ec L2E on (L2E.scaffoldId=S.scaffoldId) " +
			"join meta2010jul.ACL A on(A.resourceId=S.ScaffoldId and A.resourceType='scaffold') " +
			"where S.isGenomic=1 and S.isActive=1 and S.length >= 1000 and " +
			"A.requesterId=1 and A.requesterType='group' and A.read=1 and " +
			"L2E.ecNum in (" + GlammUtils.joinCollection(ecNums) + ") " +
			"order by T.name;";
		else
			sql = "select distinct T.taxonomyId, T.name, L2E.ecNum " +
			"from meta2010jul.Taxonomy T " +
			"join meta2010jul.Scaffold S on (S.taxonomyId=T.taxonomyId) " +
			"join meta2010jul.Locus2Ec L2E on (L2E.scaffoldId=S.scaffoldId) " +
			"where S.isGenomic=1 and S.isActive=1 and S.length >= 1000 and " +
			"L2E.ecNum in (" + GlammUtils.joinCollection(ecNums) + ") " +
			"order by T.name;";
		try {

			Connection connection = GlammDbConnectionPool.getConnection();
			Statement statement = connection.createStatement();

			ResultSet rs = statement.executeQuery(sql);

			while(rs.next()) {

				if(ecNum2Organisms == null)
					ecNum2Organisms = new HashMap<String, HashSet<Organism>>();

				String taxonomyId	= rs.getString("taxonomyId");
				String name			= rs.getString("name");
				String ecNum 		= rs.getString("ecNum");

				HashSet<Organism> organisms = ecNum2Organisms.get(ecNum);
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

}
