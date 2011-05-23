package GlammDb;

use strict;
use FileHandle;
use File::Basename;

# entities
use GlammDb::Entity;
use GlammDb::DataSource;
use GlammDb::Reaction;
use GlammDb::Compound;
use GlammDb::Pathway;

# db handlers
# base class - do not instantiate
use GlammDb::DbHandler;

# data source handler
use GlammDb::DataSourceHandler;

# compound db handlers
use GlammDb::CompoundDbHandler;
use GlammDb::KeggCompoundHandler;
use GlammDb::KeggGlycanHandler;
use GlammDb::BiocycCompoundHandler;
use GlammDb::PalssonCompoundHandler;

# reaction db handlers
use GlammDb::ReactionDbHandler;
use GlammDb::KeggReactionHandler;
use GlammDb::BiocycReactionHandler;
use GlammDb::PalssonReactionHandler;

# pathway db handlers
use GlammDb::PathwayDbHandler;
use GlammDb::KeggPathwayHandler;

################################################################################
# globals
################################################################################

our $gLastGuid = 1;

our %gXrefDbName2XrefUrlBase		= (
	"3DMET"        	=>	"http://www.3dmet.dna.affrc.go.jp/bin2/show_data.e?acc=",
	"BIOCYC-CPD"    =>	"http://BioCyc.org/META/NEW-IMAGE?type=COMPOUND&object=",
	"CHEBI"        	=>	"http://www.ebi.ac.uk/chebi/searchId.do?chebiId=CHEBI:",	
	"KNAPSACK"     	=>	"http://kanaya.naist.jp/knapsack_jsp/information.jsp?sname=C_ID&word=",
	"LIGAND-CPD"   	=>	"http://www.genome.jp/dbget-bin/www_bget?compound+",		
	"LIPIDBANK"    	=>	"http://lipidbank.jp/cgi-bin/detail.cgi?id=",			
	"LIPIDMAPS"    	=>	"http://www.lipidmaps.org/data/LMSDRecord.php?LMID=",	
	"NCI"          	=>	"http://cactus.nci.nih.gov/ncidb2/?nsc=",				
	"PDB-CCD"      	=>	"http://www.ebi.ac.uk/msd-srv/msdchem/cgi-bin/cgi.pl?FUNCTION=getByCode&CODE=",
	"PUBCHEM"      	=>	"http://pubchem.ncbi.nlm.nih.gov/summary/summary.cgi?sid=",
	"PubChem"      	=>	"http://pubchem.ncbi.nlm.nih.gov/summary/summary.cgi?sid=",
	"UM-BBD-CPD"   	=>	"http://umbbd.msi.umn.edu/servlets/pageservlet?ptype=c&compID=",												
);

our $gDbPath = "";
our $gOutputPath = "";
our %gFileNames = ();
our %gFileHandles = ();

################################################################################

sub init {
	
	my ( $dbPath, $outputPath ) = @_;
	
	$gDbPath = $dbPath;
	$gOutputPath = $outputPath;

	%gFileNames = (
		"citation"				=>	"$gOutputPath/GlammCitation.csv",
		"compound"				=>	"$gOutputPath/GlammCompound.csv",
		"dataSource"			=>	"$gOutputPath/GlammDataSource.csv",
		"entity2DataSource"		=>	"$gOutputPath/GlammEntity2DataSource.csv",
		"enzyme"				=>	"$gOutputPath/GlammEnzyme.csv",
#		"formula"				=>	"$gOutputPath/GlammFormula.csv",
		"keggMap"				=>	"$gOutputPath/GlammKeggMap.csv",
		"keggRpair"				=>	"$gOutputPath/GlammKeggRpair.csv",
		"lastGuid"				=>	"$gOutputPath/GlammLastGuid.csv",
#		"mass"					=>	"$gOutputPath/GlammMass.csv",
		"product"				=>	"$gOutputPath/GlammProduct.csv",
		"reactant"				=>	"$gOutputPath/GlammReactant.csv",
		"reaction"				=>	"$gOutputPath/GlammReaction.csv",
		"reactionParticipant"	=>	"$gOutputPath/GlammReactionParticipant.csv",
		"synonym"				=>	"$gOutputPath/GlammSynonym.csv",
		"xref"					=>	"$gOutputPath/GlammXref.csv",
	);	
}

################################################################################
# utility methods
################################################################################

sub trim {
	
	my ( $in ) = @_;
	
	my $out = $in;
	
	$out =~ s/^\s+//g;
	$out =~ s/\s+$//g;
	
	return $out;	
}

################################################################################

sub getDate {
	
	my $date = "";
	my ( $sec, $min, $hour, $mday, $mon, $year, $wday, $yday, $isdst ) = localtime(time);

	$date = sprintf ( "%4d-%02d-%02d", $year+1900, $mon+1, $mday );
		
	return $date;
}

################################################################################

sub openFileHandles {
	
	while ( my ($key, $fileName) = each %gFileNames ) {
		$gFileHandles{$key} = new FileHandle;
		$gFileHandles{$key}->open( "> $fileName" ) or
			die "Could not open $fileName for writing."
	}
	
}

################################################################################

sub closeFileHandles {

	map {
		$_->close();
	} values %gFileHandles;
	%gFileHandles = ();
}

################################################################################

sub outputCsv {
	my $fh = shift;
	my @qString = map { "\"$_\""; } @_;
	my $csv = join ( ",", @qString );
	print $fh "$csv\n";
}

################################################################################

#-- GlammLastGuid
#
#CREATE TABLE IF NOT EXISTS GlammLastGuid (
#	lastGuid bigint(10) unsigned NOT NULL, 
#	PRIMARY KEY (lastGuid)
#) TYPE=MyISAM;

sub serializeLastGuid {
	outputCsv	(	$gFileHandles{"lastGuid"}, ( $gLastGuid - 1 ) );
}

1;