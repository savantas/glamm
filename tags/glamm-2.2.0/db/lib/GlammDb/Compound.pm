package GlammDb::Compound;

use strict;

our @ISA = qw(GlammDb::Entity);

# getter/setters
sub formula 		{ push (@{$_[0]->{formula}}, $_[1]) 		if defined $_[1]; $_[0]->{formula} }
sub prefFormula 	{ push (@{$_[0]->{prefFormula}}, $_[1]) 	if defined $_[1]; $_[0]->{prefFormula} }
sub mass 			{ push (@{$_[0]->{mass}}, $_[1]) 			if defined $_[1]; $_[0]->{mass} }
sub prefMass 		{ push (@{$_[0]->{prefMass}}, $_[1]) 		if defined $_[1]; $_[0]->{prefMass} }
sub smiles 			{ push (@{$_[0]->{smiles}}, $_[1]) 			if defined $_[1]; $_[0]->{smiles} }
sub inchi 			{ push (@{$_[0]->{inchi}}, $_[1]) 			if defined $_[1]; $_[0]->{inchi} }
sub charge 			{ push (@{$_[0]->{charge}}, $_[1]) 			if defined $_[1]; $_[0]->{charge} }
sub compartments	{ push (@{$_[0]->{compartments}}, $_[1])	if defined $_[1]; $_[0]->{compartments} }
sub pathway			{ push (@{$_[0]->{pathways}}, $_[1])		if defined $_[1]; $_[0]->{pathways} }
sub reactions		{ push (@{$_[0]->{reactions}}, $_[1])		if defined $_[1]; $_[0]->{reactions} }
sub types			{ push (@{$_[0]->{types}}, $_[1]) 			if defined $_[1]; $_[0]->{types} }
sub enzymes			{ push (@{$_[0]->{enzymes}}, $_[1])			if defined $_[1]; $_[0]->{enzymes} }
sub commonName		{ push (@{$_[0]->{commonName}}, $_[1])		if defined $_[1]; $_[0]->{commonName} };
sub shortName		{ push (@{$_[0]->{shortName}}, $_[1])		if defined $_[1]; $_[0]->{shortName} };


################################################################################

sub resolveAgainstDb {
	
	my $self 		= shift;
	my $targetDb 	= shift;
	my $resolved;

	# first, try to resolve against db based on KEGG Compound db membership (LIGAND-CPD)
	if( !defined $resolved ) {
		map {
			my ( $xrefName, $xrefId ) = /(\S+):\s+(\S+)/;
		
			$resolved = $targetDb->entityForXref( $xrefName, $xrefId )
				if (!defined $resolved && $xrefName eq "LIGAND-CPD");
		
		} @{ $self->xrefs() } if defined $self->xrefs();
	}
	
	return $resolved;
}
	

################################################################################

#-- GlammCompound
#
#CREATE TABLE IF NOT EXISTS GlammCompound (
#	guid bigint(10) unsigned NOT NULL, 
#	version int(2) unsigned NOT NULL default '1',
#	priority tinyint(3) unsigned default NULL,
#	created date default NULL,
#	commonName text NOT NULL default '',		NOTE:  This field should be primarily sourced from KEGG
#	mass float default NULL,					NOTE:  This field should be primarily sourced from KEGG
#	formula varchar(255) default NULL,			NOTE:  This field should be primarily sourced from KEGG
#	smiles text default NULL,
#	inchi text default NULL,
#	PRIMARY KEY (guid)
#) TYPE=MyISAM;

sub serialize {
	my $self = shift;
	
	$self->guid($GlammDb::gLastGuid++);
	
	# serialize singletons
	my $guid 			= $self->guid()->[0];
	my $version			= 1;
	my $priority		= 1;
	my $createdDate		= GlammDb::getDate();
	my $commonName		= "";
	my $mass			= "";
	my $formula			= "";
	my $smiles			= "";
	my $inchi			= "";
	
	# grab the commonName - if defined, use the commonName getter
	# otherwise, use the first synonym
	$commonName 	= $self->synonyms()->[0] if defined $self->synonyms();
	$commonName 	= $self->commonName()->[0] if defined $self->commonName();
	
	# grab preferred mass and formula if available, otherwise use the first mass and formula
	$mass			= $self->mass()->[0]		if defined $self->mass();
	$mass			= $self->prefMass()->[0]	if defined $self->prefMass();
	
	$formula		= $self->formula()->[0]		if defined $self->formula();
	$formula		= $self->prefFormula()->[0]	if defined $self->prefFormula();
	
	$smiles			= $self->smiles()->[0]		if defined $self->smiles();
	$inchi			= $self->inchi()->[0]		if defined $self->inchi();
		
	# serialize data sources
	$self->_serializeDataSources( $guid );
	
	# serialize synonyms
	$self->_serializeSynonyms();
	
	# serialize xrefs
	$self->_serializeXrefs();
	
	# output csv data
	GlammDb::outputCsv (	$GlammDb::gFileHandles{"compound"}, 
							$guid, 
							$version, 
							$priority, 
							$createdDate, 
							$commonName,
							$mass, 
							$formula, 
							$smiles, 
							$inchi );
}


################################################################################

1;