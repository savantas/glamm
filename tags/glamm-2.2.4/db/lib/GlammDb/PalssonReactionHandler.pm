package GlammDb::PalssonReactionHandler;

use strict;

our @ISA = qw(GlammDb::ReactionDbHandler);

my $kPalssonReactionAbbreviation	= "ABBREV";
my $kPalssonReactionName			= "NAME";
my $kPalssonReactionEquation		= "EQUATION";
my $kPalssonReactionPathway			= "PATHWAY";
my $kPalssonReactionEc				= "EC";

sub _parse {
	
	my $self = shift;
	my $path = $self->dataSource()->path()->[0];
	
	open ( FILE, $path ) or
		die "Could not open $path for reading.";
		
	while ( my $line = <FILE> ) {
		
		my $reaction			= GlammDb::Reaction->new();
		my @fields 				= split ( '\t', $line );
		my ( $ec )				= $fields[4] =~ /EC-(\S+)/;
		my $normalizedEquation 	= $self->_normalizeEquation( $fields[2] );
		
		$reaction->id( $fields[0] );
		$reaction->xrefs ( "BIGG-RXN: " . $fields[0] );
		$reaction->synonyms( $fields[1] );
		$reaction->equation( $fields[2] );
		$reaction->normalizedEquation( $normalizedEquation );
		$reaction->enzymes( $ec );
		$reaction->dataSource( $self->dataSource() );
		
		$self->entities( $reaction )
			if ( $reaction->validateParticipants( $self ) );
			# 
			# push ( @{$self->{entities}}, $reaction )
			# 	if ( $reaction->validateParticipants( $self ) );
	}
		
	close ( FILE );
	
}

################################################################################

sub _normalizeEquation {
	my ( $self, $equation ) = @_;
	my $normalizedEquation = "";
	
	my @terms 		= split ( /\s+/, $equation );
	my $coefficient = 1;
	map {
		my $term = $_;
		
		# stip out compartments
		$term =~ s/(\[\S+\])//g;
		
		# remove charges from ions that don't appear in the compound DB!
		$term =~ s/(\S+)[+-]$/$1/g;
		
		$normalizedEquation .= "$term ";
		
	} @terms;
	
	# chop off trailing whitespace
	chop $normalizedEquation;
	
	return $normalizedEquation;
}

1;