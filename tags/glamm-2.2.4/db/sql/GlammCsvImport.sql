-- GLAMM db import from csv files

-- Use glamm
USE glamm;

-- GlammCitation
load data local infile '../data/output/GlammCitation.csv' into table GlammCitation
fields terminated by ','
enclosed by '"'
lines terminated by '\n'
(guid, version, priority, created, citation, dataSourceGuid);

-- GlammCompound

load data local infile '../data/output/GlammCompound.csv' into table GlammCompound
fields terminated by ','
enclosed by '"'
lines terminated by '\n'
(guid, version, priority, created, commonName, mass, formula, smiles, inchi);

-- GlammDataSource

load data local infile '../data/output/GlammDataSource.csv' into table GlammDataSource
fields terminated by ','
enclosed by '"'
lines terminated by '\n'
(guid, version, priority, created, description, organization, dbName, dbVersion);

-- GlammEntity2DataSource

load data local infile '../data/output/GlammEntity2DataSource.csv' into table GlammEntity2DataSource
fields terminated by ','
enclosed by '"'
lines terminated by '\n'
(guid, version, priority, created, entityGuid, dataSourceGuid);

-- GlammEnzyme

load data local infile '../data/output/GlammEnzyme.csv' into table GlammEnzyme
fields terminated by ','
enclosed by '"'
lines terminated by '\n'
(guid, version, priority, created, reactionGuid, ecNum, name);


-- GlammKeggMap

load data local infile '../data/output/GlammKeggMap.csv' into table GlammKeggMap
fields terminated by ','
enclosed by '"'
lines terminated by '\n'
(guid, version, priority, created, fromGuid, mapId, title);

-- GlammKeggRpair

load data local infile '../data/output/GlammKeggRpair.csv' into table GlammKeggRpair
fields terminated by ','
enclosed by '"'
lines terminated by '\n'
(guid, version, priority, created, reactionGuid, compound0Guid, compound1Guid, compound0KeggId, compound1KeggId, rpairRole);

-- GlammLastGuid

load data local infile '../data/output/GlammLastGuid.csv' into table GlammLastGuid
fields terminated by ','
enclosed by '"'
lines terminated by '\n'
(lastGuid);

-- GlammMapConn

load data local infile '../data/output/GlammMapConn.csv' into table GlammMapConn
fields terminated by ','
enclosed by '"'
lines terminated by '\n'
(guid, version, priority, created, mapTitle, cpd0ExtId, cpd0SvgId, cpd1ExtId, cpd1SvgId, rxnExtId, rxnSvgId);

-- GlammReaction

load data local infile '../data/output/GlammReaction.csv' into table GlammReaction
fields terminated by ','
enclosed by '"'
lines terminated by '\n'
(guid, version, priority, created, deltaG, equation, normalizedEquation, definition);

-- GlammReactionParticipant

load data local infile '../data/output/GlammReactionParticipant.csv' into table GlammReactionParticipant
fields terminated by ','
enclosed by '"'
lines terminated by '\n'
(guid, version, priority, created, reactionGuid, compoundGuid, coefficient, pType);

-- GlammSynonym

load data local infile '../data/output/GlammSynonym.csv' into table GlammSynonym
fields terminated by ','
enclosed by '"'
lines terminated by '\n'
(guid, version, priority, created, forGuid, synonym);

-- GlammXref

load data local infile '../data/output/GlammXref.csv' into table GlammXref
fields terminated by ','
enclosed by '"'
lines terminated by '\n'
(guid, version, priority, created, fromGuid, toXrefId, xrefDbName, xrefUrl);





