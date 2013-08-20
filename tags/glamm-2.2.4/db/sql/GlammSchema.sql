-- GLAMM database schema creation file for MySQL

-- Use glamm
USE glamm;

-- Drop tables

DROP TABLE IF EXISTS GlammCitation;
DROP TABLE IF EXISTS GlammCompound;     
DROP TABLE IF EXISTS GlammDataSource;
DROP TABLE IF EXISTS GlammEntity2DataSource;
DROP TABLE IF EXISTS GlammEnzyme;
DROP TABLE IF EXISTS GlammFormula;
DROP TABLE IF EXISTS GlammKeggElement2Map;      
DROP TABLE IF EXISTS GlammKeggMap;
DROP TABLE IF EXISTS GlammKeggRpair;    
DROP TABLE IF EXISTS GlammLastGuid;
DROP TABLE IF EXISTS GlammMapConn;
DROP TABLE IF EXISTS GlammMass;
DROP TABLE IF EXISTS GlammProduct;      
DROP TABLE IF EXISTS GlammReactant;     
DROP TABLE IF EXISTS GlammReaction;  
DROP TABLE IF EXISTS GlammReactionParticipant;   
DROP TABLE IF EXISTS GlammSynonym;      
DROP TABLE IF EXISTS GlammXref;  

-- GlammCitation

CREATE TABLE IF NOT EXISTS GlammCitation (
	guid bigint(10) unsigned NOT NULL, 
	version int(2) unsigned NOT NULL default '1',
	priority tinyint(3) unsigned default NULL,
	created date default NULL,
	citation text NOT NULL default '',
	dataSourceGuid bigint(10) unsigned NOT NULL,
	KEY dataSourceGuid (dataSourceGuid),
	PRIMARY KEY (guid)
) TYPE=MyISAM;

-- GlammCompound

CREATE TABLE IF NOT EXISTS GlammCompound (
	guid bigint(10) unsigned NOT NULL, 
	version int(2) unsigned NOT NULL default '1',
	priority tinyint(3) unsigned default NULL,
	created date default NULL,
	commonName text NOT NULL default '',
	mass float default NULL,
	formula varchar(255) default NULL,
	smiles text default NULL,
	inchi text default NULL,
	PRIMARY KEY (guid)
) TYPE=MyISAM;

-- GlammDataSource

CREATE TABLE IF NOT EXISTS GlammDataSource (
	guid bigint(10) unsigned NOT NULL, 
	version int(2) unsigned NOT NULL default '1',
	priority tinyint(3) unsigned default NULL,
	created date default NULL,
	description text default NULL,
	organization text default NULL,
 	dbName varchar(255) default NULL,
	dbVersion varchar(255) default NULL,
	KEY dbName (dbName),
	KEY organization (organization),
	PRIMARY KEY (guid)
) TYPE=MyISAM;

-- GlammEntity2DataSource
 
CREATE TABLE IF NOT EXISTS GlammEntity2DataSource (
 	guid bigint(10) unsigned NOT NULL, 
 	version int(2) unsigned NOT NULL default '1',
 	priority tinyint(3) unsigned default NULL,
 	created date default NULL,
 	entityGuid bigint(10) unsigned NOT NULL,
 	dataSourceGuid bigint(10) unsigned NOT NULL,
 	KEY entityGuid (entityGuid),
 	KEY dataSourceGuid (dataSourceGuid),
 	PRIMARY KEY (guid)
) TYPE=MyISAM;

-- GlammEnzyme

CREATE TABLE IF NOT EXISTS GlammEnzyme (
	guid bigint(10) unsigned NOT NULL, 
	version int(2) unsigned NOT NULL default '1',
	priority tinyint(3) unsigned default NULL,
	created date default NULL,
	reactionGuid bigint(10) unsigned NOT NULL, 
	ecNum varchar(20) NOT NULL default '',
	name text NOT NULL default '',
	KEY reactionGuid (reactionGuid),
	KEY ecNum (ecNum),
	PRIMARY KEY (guid)
) TYPE=MyISAM;

-- GlammKeggMap

CREATE TABLE IF NOT EXISTS GlammKeggMap (
	guid bigint(10) unsigned NOT NULL, 
	version int(2) unsigned NOT NULL default '1',
	priority tinyint(3) unsigned default NULL,
	created date default NULL, 
	fromGuid bigint(10) default NULL,
	mapId  varchar(20) default NULL,
	title varchar(255) NOT NULL default '',
	KEY mapId (mapId),
	PRIMARY KEY (guid)
) TYPE=MyISAM;

-- GlammKeggRpair

CREATE TABLE IF NOT EXISTS GlammKeggRpair (
	guid bigint(10) unsigned NOT NULL, 
	version int(2) unsigned NOT NULL default '1',
	priority tinyint(3) unsigned default NULL,
	created date default NULL,
	reactionGuid bigint(10) unsigned NOT NULL,
	compound0Guid bigint(10) unsigned NOT NULL,
	compound1Guid bigint(10) unsigned NOT NULL,
	compound0KeggId varchar(8) NOT NULL default '',
	compound1KeggId varchar(8) NOT NULL default '',
	rpairRole varchar(32) NOT NULL default '',
	KEY reactionGuid (reactionGuid),
	KEY compound0Guid (compound0Guid),
	KEY compound1Guid (compound1Guid),
	KEY compound0KeggId (compound0KeggId),
	KEY compound1KeggId (compound1KeggId),
	KEY rpairRole (rpairRole),
	PRIMARY KEY (guid)
) TYPE=MyISAM;

-- GlammLastGuid

CREATE TABLE IF NOT EXISTS GlammLastGuid (
	lastGuid bigint(10) unsigned NOT NULL, 
	PRIMARY KEY (lastGuid)
) TYPE=MyISAM;

-- GlammMapConn

CREATE TABLE IF NOT EXISTS GlammMapConn (
	guid bigint(10) unsigned NOT NULL, 
	version int(2) unsigned NOT NULL default '1',
	priority tinyint(3) unsigned default NULL,
	created date default NULL,
	mapTitle text NOT NULL default '',
	cpd0ExtId varchar(50) NOT NULL default '',
	cpd0SvgId varchar(50) NOT NULL default '',
	cpd1ExtId varchar(50) NOT NULL default '',
	cpd1SvgId varchar(50) NOT NULL default '',
	rxnExtId varchar(50) NOT NULL default '',
	rxnSvgId varchar(50) NOT NULL default '',
	PRIMARY KEY (guid)
) TYPE=MyISAM;

-- GlammReaction

CREATE TABLE IF NOT EXISTS GlammReaction (
	guid bigint(10) unsigned NOT NULL, 
	version int(2) unsigned NOT NULL default '1',
	priority tinyint(3) unsigned default NULL,
	created date default NULL,
	deltaG float default NULL,
	equation text NOT NULL default '',
	normalizedEquation text NOT NULL default '',
	definition text NOT NULL default '',
	PRIMARY KEY (guid)
) TYPE=MyISAM;

-- GlammReactionParticipant

CREATE TABLE IF NOT EXISTS GlammReactionParticipant (
	guid bigint(10) unsigned NOT NULL, 
	version int(2) unsigned NOT NULL default '1',
	priority tinyint(3) unsigned default NULL,
	created date default NULL,
	reactionGuid bigint(10) unsigned NOT NULL,
	compoundGuid bigint(10) unsigned NOT NULL,
	coefficient tinyint(3) unsigned NOT NULL default '1',
	pType enum ('REACTANT', 'PRODUCT') NOT NULL,
	KEY reactionGuid (reactionGuid),
	KEY compoundGuid (compoundGuid),
	KEY pType (pType),
	PRIMARY KEY (guid)
) TYPE=MyISAM;

-- GlammSynonym

CREATE TABLE IF NOT EXISTS GlammSynonym (
	guid bigint(10) unsigned NOT NULL, 
	version int(2) unsigned NOT NULL default '1',
	priority tinyint(3) unsigned default NULL,
	created date default NULL,
	forGuid bigint(10) unsigned NOT NULL,
	synonym varchar(255) NOT NULL default '',
	KEY forGuid (forGuid),
	PRIMARY KEY (guid)
) TYPE=MyISAM;

-- GlammXref

CREATE TABLE IF NOT EXISTS GlammXref (
	guid bigint(10) unsigned NOT NULL, 
	version int(2) unsigned NOT NULL default '1',
	priority tinyint(3) unsigned default NULL,
	created date default NULL,
	fromGuid bigint(10) unsigned NOT NULL,
	toXrefId varchar(50) NOT NULL default '',
	xrefDbName varchar(50) NOT NULL default '',
	xrefUrl text default NULL,
	KEY fromGuid (fromGuid),
	KEY toXrefId (toXrefId),
	KEY xrefDbName (xrefDbName),
	PRIMARY KEY (guid)
) TYPE=MyISAM;

