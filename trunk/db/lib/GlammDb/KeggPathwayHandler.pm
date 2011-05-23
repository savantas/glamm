package GlammDb::KeggPathwayHandler;

use strict;

use FileHandle;
use File::Basename;

our @ISA = qw(GlammDb::PathwayDbHandler);

################################################################################

sub pathwayForEntityId { 
	
	my ( $self, $entityId, $pathway ) = @_;
		
	$self->{pathwayForEntityId}->{$entityId} = $pathway
		if defined $pathway;
		
	return $self->{pathwayForEntityId}->{$entityId};
	
}

################################################################################

sub _hashEntityIds {
	my $self	= shift;
	map {
		my $pathway = $_;
		map {
			my $entityId = $_;
			$self->pathwayForEntityId( $entityId, $pathway );
		} @{$pathway->entityIds()} if defined $pathway->entityIds();
	} @{$self->entities()};
}

################################################################################

sub _parse {
	
	my $self 	= shift;
	my $path 	= $self->dataSource()->path()->[0];
	
	die ( "path invalid or not defined" )
		unless ( defined $path && $path ne "" );
		
	$self->_parseKeggMapTableFile();
	$self->_hashXrefs();	# do an early hashXrefs, as we'll need to refer to the mapIds
	$self->_parseKeggMapFile($self->dataSource()->path()->[1]);
	$self->_parseKeggMapFile($self->dataSource()->path()->[2]);
#	$self->_parseKeggMapFilesWithExtension( "rn" );
#	$self->_parseKeggMapFilesWithExtension( "cpd" );
	$self->_hashEntityIds();
		
}

################################################################################

sub _parseKeggMapTableFile {
	
	my $self 	= shift;
	my $path	= $self->dataSource()->path()->[0];
	
	open ( FILE, $path ) or
		die "Could not open $path for reading.";
	
	while ( my $line = <FILE> ) {
		my ( $id, $title ) = $line =~ /^(\d+)\s+(.*)/;
		my $pathway = GlammDb::Pathway->new();
		
		$pathway->id( $id );
		$pathway->title( $title );
		$pathway->xrefs( "KEGG_MAP_ID: $id" );
		$pathway->dataSource( $self->dataSource() );
		
		$self->entities( $pathway );
		
	}
	
	close ( FILE );
}

################################################################################

sub _parseKeggMapFile {
	
	my ( $self, $fileName ) = @_;
	
	open ( FILE, $fileName ) or
		die "Could not open $fileName for reading.";
	
	while ( my $line = <FILE> ) {
		my @ids = split(/\s+/, $line);
		my $entityId = shift(@ids);
		foreach my $id (@ids) {
			my $pathway = $self->entityForXref("KEGG_MAP_ID", $id);
			$pathway->entityIds($entityId);
		}
	}
	
	close ( FILE );
}

#sub _parseKeggMapFile {
#	
#	my ( $self, $fileName ) = @_;
#	
#	open ( FILE, $fileName );
#	
#	my ( $mapId ) = basename ( $fileName ) =~ /map(\d+)\.\S+/;
#	
#	while ( my $line = <FILE> ) {
#		my @entityIds = ();
#		my $pathway = $self->entityForXref( "KEGG_MAP_ID", $mapId );
#		
#		# deal with the fact that kegg files are dirty 
#		chomp ( $line );
#		$line =~ s/[\(\)\s+]//g;
#		@entityIds = split ( /,/, $line );
#		
#		map {
#			$pathway->entityIds( $_ );
#		} @entityIds;
#		
#	}
#	
#	close ( FILE );
#}

################################################################################

sub _parseKeggMapFilesWithExtension {
	
	my ( $self, $extension ) = @_;
	my $directory = $self->dataSource()->path()->[1];
	
	chomp $directory;
	
	map {
		$self->_parseKeggMapFile ( $_ );
	} <$directory/*.$extension>;
	
}

1;