package gov.lbl.glamm.server.dao.impl;

import gov.lbl.glamm.client.model.Compound;
import gov.lbl.glamm.client.model.util.Xref;
import gov.lbl.glamm.server.GlammDbConnectionPool;
import gov.lbl.glamm.server.GlammSession;
import gov.lbl.glamm.server.dao.CompoundDAO;
import gov.lbl.glamm.server.util.GlammUtils;
import gov.lbl.glammdb.domain.PersistentPathway;
import gov.lbl.glammdb.domain.PersistentPwyElement;
import gov.lbl.glammdb.util.HibernateUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

public class CompoundGlammDAOImpl implements CompoundDAO {

	private GlammSession sm;
	
	public CompoundGlammDAOImpl(final GlammSession sm) {
		this.sm = sm;
	}
	
	@Override
	public Set<Compound> getCompounds(Set<String> ids) {

		Set<Compound> cpds = new HashSet<Compound>();
		
		if(ids == null || ids.isEmpty())
			return cpds;
		
		String idsString = GlammUtils.joinCollection(ids);
		String sql 	=	"select C.guid, C.commonName, C.mass, C.formula, C.smiles, C.inchi, X.toXrefId, X.xrefDbName " + 
		"from glamm.GlammCompound C " +
		"join glamm.GlammXref X on (X.fromGuid=C.guid) " +
		"where X.toXrefId in (" + idsString + ");";

		try {

			Connection connection = GlammDbConnectionPool.getConnection(sm);
			PreparedStatement ps = connection.prepareStatement(sql);

			ResultSet rs = ps.executeQuery();

			while(rs.next()) {

				Compound cpd = new Compound();

				cpd.setName(rs.getString("commonName"));
				cpd.setMass(rs.getString("mass"));
				cpd.setFormula(rs.getString("formula"));
				cpd.setSmiles(rs.getString("smiles"));
				cpd.setInchi(rs.getString("inchi"));
				cpd.addXref(new Xref(rs.getString("toXrefId"), rs.getString("xrefDbName")));

				cpds.add(cpd);
			}

			rs.close();
			connection.close();

		} catch(Exception e) {
			e.printStackTrace();
		}

		return cpds;
	}
	
	@Override
	public Compound getCompound(String id, String dbName) {

		Compound cpd = null;
		String sql 	=	"select C.guid, C.commonName, C.mass, C.formula, C.smiles, C.inchi " + 
		"from glamm.GlammCompound C " +
		"join glamm.GlammXref X on (X.fromGuid=C.guid) " +
		"join glamm.GlammEntity2DataSource E2DS on (E2DS.entityGuid=C.guid) " +
		"join glamm.GlammDataSource DS on (E2DS.dataSourceGuid=DS.guid) " +
		"where X.toXrefId=? and DS.dbName=?;";

		try {

			Connection connection = GlammDbConnectionPool.getConnection(sm);
			PreparedStatement ps = connection.prepareStatement(sql);

			ps.setString(1, id);
			ps.setString(2, dbName);

			ResultSet rs = ps.executeQuery();

			if(rs.next()) {

				cpd = new Compound();

				cpd.setName(rs.getString("commonName"));
				cpd.setMass(rs.getString("mass"));
				cpd.setFormula(rs.getString("formula"));
				cpd.setSmiles(rs.getString("smiles"));
				cpd.setInchi(rs.getString("inchi"));
				cpd.addXref(new Xref(id, dbName));

			}



			rs.close();
			connection.close();

		} catch(Exception e) {
			e.printStackTrace();
		}

		return cpd;
	}
	
	@Override
	public Set<Compound> getCompoundsForSearch(String mapId) {
		
		Session session = HibernateUtil.getSessionFactory(sm).getCurrentSession();
		session.beginTransaction();
		PersistentPathway result = (PersistentPathway) session.createCriteria(PersistentPathway.class)
		.add(Restrictions.eq("mapId", mapId))
		.uniqueResult();
		session.getTransaction().commit();
		
		if(result == null)
			return new HashSet<Compound>();
		
		Set<String> cpdIds = new HashSet<String>();
		for(PersistentPwyElement element : result.getElements()) {
			if(element.getType() == PersistentPwyElement.Type.CPD)
				cpdIds.add(element.getXrefId());
		}
		
		return getCompoundsForSearch(cpdIds);
	}
	
	private Set<Compound> getCompoundsForSearch(Collection<String> cpdIds) {
		
		Set<Compound> cpds = new HashSet<Compound>();
		String cpdIdsString = GlammUtils.joinCollection(cpdIds);
		
		String sql = "select X.toXrefId, X.xrefDbName, C.commonName as synonym " +
		"from glamm.GlammCompound C " +
		"join glamm.GlammXref X on (C.guid=X.fromGuid) " +
		"where X.toXrefId in (" + cpdIdsString + ") " +
		"union " +
		"select X.toXrefId, X.xrefDbName, S.synonym " +
		"from glamm.GlammXref X " +
		"join glamm.GlammSynonym S on (X.fromGuid=S.forGuid) " +
		"where S.synonym not like \"%<%\" " +
		"and S.synonym not like \"%>%\" " +
		"and S.synonym not like \"%&%\" " +
		"and X.toXrefId in (" + cpdIdsString + ");";
		
		try {
			
			Connection connection = GlammDbConnectionPool.getConnection(sm);
			Statement statement = connection.createStatement();

			ResultSet rs = statement.executeQuery(sql);

			while(rs.next()) {

				Compound cpd = new Compound();

				cpd.setName(rs.getString("synonym"));
				cpd.addXref(new Xref(rs.getString("toXrefId"), rs.getString("xrefDbName")));
				cpds.add(cpd);

			}

			rs.close();
			connection.close();
		
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return cpds;
	}

}
