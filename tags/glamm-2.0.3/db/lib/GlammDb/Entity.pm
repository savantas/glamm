package GlammDb::Entity;

use strict;
use Tie::RefHash;

# getter/setters
sub guid			{ push (@{$_[0]->{guid}}, $_[1])			if defined $_[1]; $_[0]->{guid} }
sub dataSource		{ push (@{$_[0]->{dataSource}}, $_[1])		if defined $_[1]; $_[0]->{dataSource} }
sub id 				{ push (@{$_[0]->{id}}, $_[1]) 				if defined $_[1]; $_[0]->{id} }
sub parent			{ push (@{$_[0]->{parent}}, $_[1])			if defined $_[1]; $_[0]->{parent} }
sub synonyms 		{ push (@{$_[0]->{synonyms}}, $_[1])		if defined $_[1]; $_[0]->{synonyms} }
sub xrefs 			{ push (@{$_[0]->{xrefs}}, $_[1]) 			if defined $_[1]; $_[0]->{xrefs} }

################################################################################

sub new {
	my $class 	= shift;
	my $self 	= {@_};
	bless ( $self, $class );
	return $self;
}

################################################################################

sub print {
	my $self = shift;
	
	while ( my ($k, $v) = each %$self ) {
		print "$k:\n";
		map {
			print "\t$_\n";
		} @$v;
	}
	print "-----------------------------------------------\n"
}

################################################################################

sub _mergeArrays {
	my ( $self, $a0Ref, $a1Ref )	= @_;
	my %a					= ();
	tie %a, 'Tie::RefHash';
	
	map { $a{$_} = 0; } @$a0Ref;
	map { $a{$_} = 0; } @$a1Ref;
	
	return keys %a;
}

################################################################################

sub merge {
	my ( $e0, $e1 ) = @_;
	my $self = $e0;

	# we're going through these contortions to ensure that $e is the correct subclass of Entity
	my $e = {};
	my $e0Class = ref( $e0 );
	my $e1Class	= ref( $e1 );
	
	die "Attempting to merge classes of different type ($e0 and $e1)"
		unless ( $e0Class eq $e1Class );
		
	bless ( $e, $e0Class );
	
	my @keys = $self->_mergeArrays( [keys %$e0], [keys %$e1] );
	
	map {
		
		my $key 	= $_;
		my $v0Ref 	= $e0->{$key};
		my $v1Ref	= $e1->{$key};
		my @v		= ();
		
		if ( defined $v0Ref && defined $v1Ref ) {
			@v = $self->_mergeArrays( $v0Ref, $v1Ref );
		}
		 
		elsif ( defined $v0Ref ) {
			@v = @$v0Ref;
		}
		
		elsif ( defined $v1Ref ) {
			@v = @$v1Ref;
		}
		
		map { push( @{$e->{$key}}, $_ ); } @v;  
		
	} @keys;
	
	return $e; 
}

################################################################################

sub getXrefId {
	
	my ( $self, $xrefName ) = @_;
	my $xrefId = "";
	
	map {
		if ( /$xrefName:\s+(\S+)/ ) {
			$xrefId = $1;
		}	
	} $self->xrefs();	
	
	return $xrefId;
}

################################################################################

sub resolveAgainstDb {
	return;
}

################################################################################

sub serialize {
	my $self	= shift;
}

################################################################################

# -- GlammEntity2DataSource
# 
# CREATE TABLE IF NOT EXISTS GlammEntity2DataSource (
# 	guid bigint(10) unsigned NOT NULL, 
# 	version int(2) unsigned NOT NULL default '1',
# 	priority tinyint(3) unsigned default NULL,
# 	created date default NULL,
# 	entityGuid bigint(10) unsigned NOT NULL,
# 	dataSourceGuid bigint(10) unsigned NOT NULL,
# 	KEY entityGuid (entityGuid),
# 	KEY dataSourceGuid (dataSourceGuid),
# 	PRIMARY KEY (guid)
# ) TYPE=MyISAM;

sub _serializeDataSources {
	my $self 			= shift;
	my $entityGuid		= shift;
	
	die "ERROR: Attempted to serialize dataSource without GUID."
		unless defined $entityGuid;
		
	my $version 		= 1;
	my $priority		= 1;
	my $createdDate		= GlammDb::getDate();

	map {	
		my $dataSourceGuid	= $_->guid()->[0];
		my $guid 			= $GlammDb::gLastGuid++;
		
		GlammDb::outputCsv	(	$GlammDb::gFileHandles{"entity2DataSource"},
								$guid,
								$version,
								$priority,
								$createdDate,
								$entityGuid,
								$dataSourceGuid );
	
	} @{$self->dataSource()} if defined $self->dataSource();
}

################################################################################

# -- GlammSynonym
# 
# CREATE TABLE IF NOT EXISTS GlammSynonym (
# 	guid bigint(10) unsigned NOT NULL, 
# 	version int(2) unsigned NOT NULL default '1',
# 	priority tinyint(3) unsigned default NULL,
# 	created date default NULL,
# 	forGuid bigint(10) unsigned NOT NULL,
# 	synonym varchar(255) NOT NULL default '',
# 	KEY forGuid (forGuid),
# 	PRIMARY KEY (guid)
# ) TYPE=MyISAM;

sub _serializeSynonyms {
	my $self 			= shift;
	
	die "ERROR: Attempted to serialize synonyms without entity GUID."
		unless defined $self->guid();
		
	my $forGuid 		= $self->guid()->[0];
	my $version 		= 1;
	my $priority		= 1;
	my $createdDate		= GlammDb::getDate();

	map {	
		my $synonym			= $_;
		my $guid 			= $GlammDb::gLastGuid++;
		
		GlammDb::outputCsv	(	$GlammDb::gFileHandles{"synonym"},
								$guid,
								$version,
								$priority,
								$createdDate,
								$forGuid,
								$synonym );
		
	} @{$self->synonyms()} if defined $self->synonyms();
}

################################################################################

# -- GlammXref
# 
# CREATE TABLE IF NOT EXISTS GlammXref (
# 	guid bigint(10) unsigned NOT NULL, 
# 	version int(2) unsigned NOT NULL default '1',
# 	priority tinyint(3) unsigned default NULL,
# 	created date default NULL,
# 	fromGuid bigint(10) unsigned NOT NULL,
# 	toXrefId varchar(50) NOT NULL default '',
# 	xrefDbName varchar(50) NOT NULL default '',
# 	xrefUrl text default NULL,
# 	KEY fromGuid (fromGuid),
# 	KEY toXrefId (toXrefId),
# 	KEY xrefDbName (xrefDbName),
# 	PRIMARY KEY (guid)
# ) TYPE=MyISAM;

sub _serializeXrefs {
	my $self 		= shift;
	
	
	die "ERROR: Attempted to serialize xrefs without entity GUID."
		unless defined $self->guid();
		
	my $fromGuid 		= $self->guid()->[0];	
	my $version 		= 1;
	my $priority		= 1;
	my $createdDate		= GlammDb::getDate();

	map {	
		my $xref						= $_;
		my ( $xrefDbName, $toXrefId)	= $xref =~ /(\S+):\s+(\S+)/;
		my $xrefUrl						= "\\N";
		my $xrefUrlBase					= $GlammDb::gXrefDbName2XrefUrlBase{$xrefDbName};
		my $guid 						= $GlammDb::gLastGuid++;
		
		$xrefUrl						= $xrefUrlBase . $toXrefId
			if defined $xrefUrlBase;
		
		GlammDb::outputCsv	(	$GlammDb::gFileHandles{"xref"},
								$guid,
								$version,
								$priority,
								$createdDate,
								$fromGuid,
								$toXrefId,
								$xrefDbName,
								$xrefUrl );
								
	} @{$self->xrefs()} if defined $self->xrefs();
}

################################################################################

1;