package GlammDb::Pathway;

use strict;

our @ISA = qw(GlammDb::Entity);

# getter/setters
sub title		{ push (@{$_[0]->{title}}, $_[1]) 		if defined $_[1]; $_[0]->{title} }
sub entityIds	{ push (@{$_[0]->{entityIds}}, $_[1])	if defined $_[1]; $_[0]->{entityIds} };

sub serialize {
	
# -- GlammKeggMap
# 
# CREATE TABLE IF NOT EXISTS GlammKeggMap (
# 	guid bigint(10) unsigned NOT NULL, 
# 	version int(2) unsigned NOT NULL default '1',
# 	priority tinyint(3) unsigned default NULL,
# 	created date default NULL, 
# 	fromGuid bigint(10) default NULL,
# 	mapId  varchar(20) default NULL,
# 	title varchar(255) NOT NULL default '',
# 	KEY mapId (mapId),
# 	PRIMARY KEY (guid)
# ) TYPE=MyISAM;
	
	my $self 				= shift;
	my $parentDb			= shift;
	my $compoundDb 			= $parentDb->cpdDb();
	my $compoundXrefName 	= $parentDb->cpdDataSource()->dbName()->[0];
	my $reactionDb			= $parentDb->rxnDb();
	my $reactionXrefName	= $parentDb->rxnDataSource()->dbName()->[0];
	
	# serialize pathway proper
	my $version				= 1;
	my $priority			= 1;
	my $createdDate			= GlammDb::getDate();
	my $mapId				= $self->id()->[0];
	my $title				= $self->title->[0];
	
	map {
	
		my $entityId = $_;
		my $entity = $compoundDb->entityForXref( $compoundXrefName, $entityId );
		
		$entity = $reactionDb->entityForXref( $reactionXrefName, $entityId)
			unless defined $entity;
		
		if ( defined $entity ) {	
			
			my $guid 		= $GlammDb::gLastGuid++;
			my $fromGuid	= $entity->guid()->[0];
			
			# serialize data sources
			$self->_serializeDataSources( $guid );
			
			GlammDb::outputCsv	(	$GlammDb::gFileHandles{"keggMap"},
									$guid,
									$version,
									$priority,
									$createdDate,
									$fromGuid,
									$mapId,
									$title );
		}
	
	} @{$self->entityIds()} if defined $self->entityIds();
	
}

1;