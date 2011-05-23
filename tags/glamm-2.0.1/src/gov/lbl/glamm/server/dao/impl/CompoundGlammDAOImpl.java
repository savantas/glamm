package gov.lbl.glamm.server.dao.impl;

import gov.lbl.glamm.client.model.Compound;
import gov.lbl.glamm.server.GlammDbConnectionPool;
import gov.lbl.glamm.server.dao.CompoundDAO;
import gov.lbl.glamm.shared.GlammConstants;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class CompoundGlammDAOImpl implements CompoundDAO {

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

			Connection connection = GlammDbConnectionPool.getConnection();
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
				cpd.addXref(id, dbName);

			}



			rs.close();
			connection.close();

		} catch(Exception e) {
			e.printStackTrace();
		}

		return cpd;
	}
	
	@Override
	public ArrayList<Compound> getCompoundsForSearch(String dbName) {
		
		ArrayList<Compound> cpds = null;
		String query = "select X.toXrefId, C.commonName as synonym " +
		"from glamm.GlammCompound C " +
		"join glamm.GlammXref X on (C.guid=X.fromGuid) " +
		"where X.xrefDbName in (?) " +
		"and X.toXrefId in (" + GlammConstants.GLOBAL_MAP_CPD_IDS + ") " +
		"union " +
		"select X.toXrefId, S.synonym " +
		"from glamm.GlammXref X " +
		"join glamm.GlammSynonym S on (X.fromGuid=S.forGuid) " +
		"where X.xrefDbName in (?) " +
		"and S.synonym not like \"%<%\" " +
		"and S.synonym not like \"%>%\" " +
		"and S.synonym not like \"%&%\" " +
		"and X.toXrefId in (" + GlammConstants.GLOBAL_MAP_CPD_IDS + ");";
		
		try {
			
			Connection connection = GlammDbConnectionPool.getConnection();
			PreparedStatement ps = connection.prepareStatement(query);

			ps.setString(1, dbName);
			ps.setString(2, dbName);

			ResultSet rs = ps.executeQuery();

			while(rs.next()) {

				Compound cpd = new Compound();

				cpd.setName(rs.getString("synonym"));
				cpd.addXref(rs.getString("toXrefId"), dbName);
				
				if(cpds == null)
					cpds = new ArrayList<Compound>();
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
