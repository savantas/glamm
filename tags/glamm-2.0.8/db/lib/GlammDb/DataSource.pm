package GlammDb::DataSource;

use strict;

our @ISA = qw(GlammDb::Entity);

################################################################################

sub id				{ push (@{$_[0]->{id}}, $_[1])				if defined $_[1]; $_[0]->{id} };
sub description		{ push (@{$_[0]->{description}}, $_[1])		if defined $_[1]; $_[0]->{description} };
sub organization	{ push (@{$_[0]->{organization}}, $_[1])	if defined $_[1]; $_[0]->{organization} };
sub dbName			{ push (@{$_[0]->{dbName}}, $_[1])			if defined $_[1]; $_[0]->{dbName} };
sub dbVersion		{ push (@{$_[0]->{dbVersion}}, $_[1])		if defined $_[1]; $_[0]->{dbVersion} };
sub path			{ push (@{$_[0]->{path}}, $_[1])			if defined $_[1]; $_[0]->{path} };
sub type			{ push (@{$_[0]->{type}}, $_[1])			if defined $_[1]; $_[0]->{type} };
sub citation		{ push (@{$_[0]->{citation}}, $_[1])		if defined $_[1]; $_[0]->{citation}	};

#-- GlammDataSource
#
#CREATE TABLE IF NOT EXISTS GlammDataSource (
#	guid bigint(10) unsigned NOT NULL, 
#	version int(2) unsigned NOT NULL default '1',
#	priority tinyint(3) unsigned default NULL,
#	created date default NULL,
#	description text default NULL,
#	organization text default NULL,
# 	dbName varchar(255) default NULL,
#	dbVersion varchar(255) default NULL,
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
	my $description		= "";
	my $organization	= "";
	my $dbName			= "";
	my $dbVersion		= "";
	
	$description 	= 	$self->description()->[0] 	if defined $self->description();
	$organization	=	$self->organization()->[0]	if defined $self->organization();
	$dbName 		= 	$self->dbName()->[0] 		if defined $self->dbName();
	$dbVersion 		= 	$self->dbVersion()->[0] 	if defined $self->dbVersion();
	
	$self->_serializeCitations($guid);
	
	# output csv data
	GlammDb::outputCsv (	$GlammDb::gFileHandles{"dataSource"}, 
							$guid, 
							$version, 
							$priority, 
							$createdDate, 
							$description,
							$organization,
							$dbName,
							$dbVersion );
}

#-- GlammCitation
#
#CREATE TABLE IF NOT EXISTS GlammCitation (
#	guid bigint(10) unsigned NOT NULL, 
#	version int(2) unsigned NOT NULL default '1',
#	priority tinyint(3) unsigned default NULL,
#	created date default NULL,
#	citation text NOT NULL default '',
#	dataSourceGuid bigint(10) unsigned NOT NULL,
#	KEY dataSourceGuid (dataSourceGuid),
#	PRIMARY KEY (guid)
#) TYPE=MyISAM;

sub _serializeCitations {
	my $self 			= shift;
	my $dataSourceGuid 	= shift;
	
	# serialize singletons
	my $version			= 1;
	my $priority		= 1;
	my $createdDate		= GlammDb::getDate();
	
	map {
		
		my $citationText		= $_;
		my $guid 				= $GlammDb::gLastGuid++;
		
		GlammDb::outputCsv	(	$GlammDb::gFileHandles{"citation"},
								$guid,
								$version,
								$priority,
								$createdDate,
								$citationText,
								$dataSourceGuid);
								
		
	} @{$self->citation()} if defined $self->citation();
}


1;