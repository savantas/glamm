package GlammDb::DataSourceHandler;

use strict;
use XML::DOM;

our @ISA = qw(GlammDb::DbHandler);

################################################################################

sub init {
	my $self = shift;
	die ( "dataSource invalid or not defined" )
		unless ( defined $self->dataSource() );
}

################################################################################

#sub _parse () {
#
#	my $self = shift;
#	my $path = $self->dataSource()->path()->[0];
#	
#	open ( FILE, $path ) or
#		die "Could not open $path for reading";
#		
#	while ( my $line = <FILE> ) {
#		
#		my $dataSource	= GlammDb::DataSource->new();
#		my @values 		= split( ":", $line );
#		
#		$dataSource->type( shift( @values ) );
#		$dataSource->id( shift( @values ) );
#		$dataSource->description( shift( @values ) );
#		$dataSource->organization( shift( @values ) );
#		$dataSource->dbName( shift( @values ) );
#		$dataSource->dbVersion( shift( @values ) );
#		$dataSource->path( $GlammDb::gDbPath . "/" . shift( @values ) );	
#		$dataSource->path( $GlammDb::gDbPath . "/" . shift( @values ) ) 
#			if ( $dataSource->id()->[0] eq "keggPathway" );
#			 	
#		$dataSource->xrefs( "DATASOURCE: " . $dataSource->id()->[0] );
#		
#		$self->entities( $dataSource );
#	}
#	
#	close ( FILE );
#	
#}

sub _parse() {
	my $self = shift;
	my $path = $self->dataSource()->path()->[0];
	
	my $domParser = new XML::DOM::Parser;
	my $doc = $domParser->parsefile($path);
	
	my $dbs = $doc->getElementsByTagName("db");
	
	for(my $i = 0; $i < $dbs->getLength; $i++) {
		
		my $db = $dbs->item($i);
		my $dataSource = GlammDb::DataSource->new();
		
		# grab db attributes
		$dataSource->type($db->getAttribute("type"));
		$dataSource->id($db->getAttribute("id"));
		$dataSource->description($db->getAttribute("description"));
		$dataSource->dbName($db->getAttribute("name"));
		$dataSource->dbVersion($db->getAttribute("version"));
		$dataSource->organization($db->getAttribute("organization"));
		
		# grab citations
		my $citations = $db->getElementsByTagName("citation");
		for(my $j = 0; $j < $citations->getLength; $j++) {
			my $citation = $citations->item($j);
			my @children = $citation->getChildNodes;
			map {
				my $child = $_;
				$dataSource->citation($child->getNodeValue) 
					if $child->getNodeType eq TEXT_NODE;
			} @children;
		}
		
		# grab sources
		my $sources = $db->getElementsByTagName("source");
		for(my $j = 0; $j < $sources->getLength; $j++) {
			my $source = $sources->item($j);
			$dataSource->path($GlammDb::gDbPath . "/" . $source->getAttribute("path"));
		}
		
		$dataSource->xrefs( "DATASOURCE: " . $dataSource->id()->[0] );
		
		$self->entities( $dataSource );
		
	}
	
	$doc->dispose;
}




1;