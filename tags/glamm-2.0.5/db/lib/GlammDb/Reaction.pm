package GlammDb::Reaction;

use strict;

our @ISA = qw(GlammDb::Entity);

# getter/setters
sub definition 	{ push (@{$_[0]->{definition}}, $_[1]) 	if defined $_[1]; $_[0]->{definition} }
sub enzymes 	{ push (@{$_[0]->{enzymes}}, $_[1]) 	if defined $_[1]; $_[0]->{enzymes} }
sub equation 	{ push (@{$_[0]->{equation}}, $_[1]) 	if defined $_[1]; $_[0]->{equation} }
sub pathway 	{ push (@{$_[0]->{pathway}}, $_[1]) 	if defined $_[1]; $_[0]->{pathway} }
sub products	{ push (@{$_[0]->{products}}, $_[1]) 	if defined $_[1]; $_[0]->{products} }
sub reactants	{ push (@{$_[0]->{reactants}}, $_[1]) 	if defined $_[1]; $_[0]->{reactants} }
sub rpair 		{ push (@{$_[0]->{rpair}}, $_[1]) 		if defined $_[1]; $_[0]->{rpair} }

################################################################################

# normalizedEquation getter/setter is special - it also extracts reaction participants
sub normalizedEquation {
	my ( $self, $normalizedEquation ) = @_; 
	
	if ( defined $normalizedEquation ) {
		push ( @{$self->{normalizedEquation}}, $normalizedEquation );
		$self->_extractParticipants( $normalizedEquation );
	}
	
	return $self->{normalizedEquation}; 
}

################################################################################

sub _extractParticipants {
	my ( $self, $normalizedEquation ) = @_;
	
	my @terms 		= split ( /\s+/, $normalizedEquation);
	my $coefficient	= 1;
	my $isReactant 	= 1;
	
	map {
		my $term = $_;
	
		if ( $term =~ /\((\S+)\)/ ) {
			$coefficient 	= $1;
		}
		
		elsif ( $term =~ /(<=>|<==>|-->)/ ) {
			$isReactant 	= 0;
			$coefficient	= 1;
		}
		
		elsif ( $term eq "+" ) {
			$coefficient 	= 1;
		}
		
		else {
			my $participant = "$coefficient: $term";
			if ( $isReactant ) {
				$self->reactants( $participant );
			}
			else {
				$self->products( $participant );
			}
		}
		
	} @terms;
}

################################################################################

sub validateParticipants {
	
	my $self		= shift;
	my $parentDb 	= shift;
	my $isValid		= 1;
	
	my $id 						= $self->id()->[0];
	my $compoundDb				= $parentDb->cpdDb();
	my $compoundXrefName		= $parentDb->cpdDataSource()->dbName()->[0];
	my $compoundDbDescription	= $parentDb->cpdDataSource()->description()->[0];
			
	
	if ( !defined $self->reactants() || @{$self->reactants()} <= 0 ) {
		print "Reaction $id has no reactants.\n";
		return 0;
	}
		
	
	if ( !defined $self->products() || @{$self->products()} <= 0 ) {
		print "Reaction $id has no products.\n";
		return 0;
	}
			
	map {
		my ( $coefficient, $compoundId )	= /(\S+):\s+(\S+)/;
		if ( !defined ( $compoundDb->entityForXref( $compoundXrefName, $compoundId ) ) ) {
			print "Could not find compound $compoundId ($compoundXrefName) for reaction $id in $compoundDbDescription.\n";
			$isValid = 0;
		}
	} @{$self->reactants()};
	
	map {
		my ( $coefficient, $compoundId )	= /(\S+):\s+(\S+)/;
		if ( !defined ( $compoundDb->entityForXref( $compoundXrefName, $compoundId ) ) ) {
			print "Could not find compound $compoundId ($compoundXrefName) for reaction $id in $compoundDbDescription.\n";
			$isValid = 0;
		}
	} @{$self->products()};
	
	return $isValid;
}

################################################################################

# -- GlammReaction
# 
# CREATE TABLE IF NOT EXISTS GlammReaction (
# 	guid bigint(10) unsigned NOT NULL, 
# 	version int(2) unsigned NOT NULL default '1',
# 	priority tinyint(3) unsigned default NULL,
# 	created date default NULL,
# 	deltaG float default NULL,
# 	equation text NOT NULL default '',
#	normalizedEquation text NOT NULL default '',
# 	definition text NOT NULL default '',
# 	PRIMARY KEY (guid)
# ) TYPE=MyISAM;

sub serialize {
	
	my $self 				= shift;
	my $parentDb			= shift;
	my $compoundDb 			= $parentDb->cpdDb();
	my $compoundXrefName 	= $parentDb->cpdDataSource()->dbName()->[0];
	
	# serialize reaction proper
	my $guid 				= $self->guid( $GlammDb::gLastGuid++ )->[0];
	my $version				= 1;
	my $priority			= 1;
	my $createdDate			= GlammDb::getDate();
	my $deltaG				= "\\N";				# probably going to deprecate this - no one seems to have it
	my $equation			= ( defined $self->equation() ? $self->equation()->[0] : "" );
	my $normalizedEquation	= ( defined $self->normalizedEquation() ? $self->normalizedEquation()->[0] : "" );
	my $definition			= ( defined $self->definition() ? $self->definition()->[0] : "" );
	
	# serialize data sources
	$self->_serializeDataSources( $guid );
	
	# serialize synonyms
	$self->_serializeSynonyms();
	
	# serialize enzymes
	$self->_serializeEnzymes();
	
	# serialize participants
	$self->_serializeParticipants( $compoundDb, $compoundXrefName );
	
	# serialize rpair
	$self->_serializeKeggRpair( $compoundDb, $compoundXrefName );
	
	# serialize xrefs
	$self->_serializeXrefs();
	
	GlammDb::outputCsv	(	$GlammDb::gFileHandles{"reaction"},
							$guid,
							$version,
							$priority,
							$createdDate,
							$deltaG,
							$equation,
							$normalizedEquation,
							$definition );
	
}

################################################################################

# -- GlammEnzyme
# 
# CREATE TABLE IF NOT EXISTS GlammEnzyme (
# 	guid bigint(10) unsigned NOT NULL, 
# 	version int(2) unsigned NOT NULL default '1',
# 	priority tinyint(3) unsigned default NULL,
# 	created date default NULL,
# 	reactionGuid bigint(10) unsigned NOT NULL, 
# 	ecNum varchar(20) NOT NULL default '',
# 	name text NOT NULL default '',				# may be deprecated
# 	KEY reactionGuid (reactionGuid),
# 	KEY ecNum (ecNum),
# 	PRIMARY KEY (guid)
# ) TYPE=MyISAM;

sub _serializeEnzymes {
	
	my $self 				= shift;
	
	die "ERROR: Attempted to serialize enzymes without reaction GUID."
		unless defined $self->guid();
	
	my $reactionGuid		= $self->guid()->[0];
	my $version				= 1;
	my $priority			= 1;
	my $createdDate			= GlammDb::getDate();
	my $name				= "";
	
	map {
		
		my $ecNum				= $_;
		my $guid 				= $GlammDb::gLastGuid++;
		
		GlammDb::outputCsv	(	$GlammDb::gFileHandles{"enzyme"},
								$guid,
								$version,
								$priority,
								$createdDate,
								$reactionGuid,
								$ecNum,
								$name );
		
	} @{$self->enzymes()} if defined $self->enzymes();
	
}

################################################################################
# 
# -- GlammReactionParticipant
# 
# CREATE TABLE IF NOT EXISTS GlammReactionParticipant (
# 	guid bigint(10) unsigned NOT NULL, 
# 	version int(2) unsigned NOT NULL default '1',
# 	priority tinyint(3) unsigned default NULL,
# 	created date default NULL,
# 	reactionGuid bigint(10) unsigned NOT NULL,
# 	compoundGuid bigint(10) unsigned NOT NULL,
# 	coefficient tinyint(3) unsigned NOT NULL default '1',
# 	pType enum ('REACTANT', 'PRODUCT') NOT NULL,
# 	KEY reactionGuid (reactionGuid),
# 	KEY compoundGuid (compoundGuid),
# 	KEY pType (pType),
# 	PRIMARY KEY (guid)
# ) TYPE=MyISAM;

sub _serializeParticipants {
	
	my ( $self, $compoundDb, $compoundXrefName ) = @_;
	
	die "ERROR:  Attempted to serialize reaction participants without reaction GUID."
		unless defined $self->guid();
		
	my $reactionGuid		= $self->guid()->[0];
	my $version				= 1;
	my $priority			= 1;
	my $createdDate			= GlammDb::getDate();
	
	# serialize reactants
	map {
		
		my ( $coefficient, $compoundId )	= /(\S+):\s+(\S+)/;
		my $guid 							= $GlammDb::gLastGuid++;
		my $compoundGuid 					= $compoundDb->entityForXref( $compoundXrefName, $compoundId )->guid()->[0];
		my $pType 							= "REACTANT";
		
		GlammDb::outputCsv	(	$GlammDb::gFileHandles{"reactionParticipant"},
								$guid,
								$version,
								$priority,
								$createdDate,
								$reactionGuid,
								$compoundGuid,
								$coefficient,
								$pType );
		
		
	} @{$self->reactants()} if defined $self->reactants();
	
	# serialize products
	map {
		
		my ( $coefficient, $compoundId )	= /(\S+):\s+(\S+)/;
		my $guid 							= $GlammDb::gLastGuid++;
		my $compoundGuid 					= $compoundDb->entityForXref( $compoundXrefName, $compoundId )->guid()->[0];
		my $pType 							= "PRODUCT";
		
		GlammDb::outputCsv	(	$GlammDb::gFileHandles{"reactionParticipant"},
								$guid,
								$version,
								$priority,
								$createdDate,
								$reactionGuid,
								$compoundGuid,
								$coefficient,
								$pType );
		
		
	} @{$self->products()} if defined $self->products();
	
}

################################################################################

#CREATE TABLE IF NOT EXISTS GlammKeggRpair (
#	guid bigint(10) unsigned NOT NULL, 
#	version int(2) unsigned NOT NULL default '1',
#	priority tinyint(3) unsigned default NULL,
#	created date default NULL,
#	reactionGuid bigint(10) unsigned NOT NULL,
#	compound0Guid bigint(10) unsigned NOT NULL,
#	compound1Guid bigint(10) unsigned NOT NULL,
#	compound0KeggId varchar(8) NOT NULL default '',
#	compound1KeggId varchar(8) NOT NULL default '',
#	rpairRole varchar(32) NOT NULL default '',
#	KEY reactionGuid (reactionGuid),
#	KEY compound0Guid (compound0Guid),
#	KEY compound1Guid (compound1Guid),
#	KEY compound0KeggId (compound0KeggId),
#	KEY compound1KeggId (compound1KeggId),
#	KEY rpairRole (rpairRole),
#	PRIMARY KEY (guid)
#) TYPE=MyISAM;

sub _serializeKeggRpair {
	
	my ( $self, $compoundDb, $compoundXrefName ) = @_;
	
	die "ERROR:  Attempted to serialize kegg rpair without reaction GUID."
		unless defined $self->guid();
	
	my $reactionGuid	= $self->guid()->[0];
	my $version 		= 1;
	my $priority 		= 1;
	my $createdDate		= GlammDb::getDate ();
	
	map {
		my ( $compound0KeggId, $compound1KeggId, $rpairRole ) = /RP\S+\s+(\S+)_(\S+)\s+(\S+)/;
		
		my $guid 			= $GlammDb::gLastGuid++; 
		my $compound0Guid 	= $compoundDb->entityForXref( $compoundXrefName, $compound0KeggId )->guid()->[0];
		my $compound1Guid 	= $compoundDb->entityForXref( $compoundXrefName, $compound1KeggId )->guid()->[0];
	
		GlammDb::outputCsv	(	$GlammDb::gFileHandles{"keggRpair"}, 
								$guid, 
								$version, 
								$priority, 
								$createdDate, 
								$reactionGuid, 
								$compound0Guid, 
								$compound1Guid, 
								$compound0KeggId, 
								$compound1KeggId, 
								$rpairRole );
								
	} @{$self->rpair()} if defined $self->rpair();
}

################################################################################
1;